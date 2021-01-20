package com.example.cinemates.ui.CineMates;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.cinemates.R;
import com.example.cinemates.databinding.ActivityHomeBinding;
import com.example.cinemates.ui.CineMates.ApiMovie.Movie;
import com.example.cinemates.ui.CineMates.ApiMovie.MoviesApiService;
import com.example.cinemates.ui.CineMates.ApiMovie.PopularFilms;
import com.example.cinemates.ui.CineMates.Fragment.FriendsFragment;
import com.example.cinemates.ui.CineMates.Fragment.HomeFragment;
import com.example.cinemates.ui.CineMates.Fragment.ProfileFragment;
import com.example.cinemates.ui.CineMates.Fragment.SearchFragment;
import com.example.cinemates.ui.CineMates.friends.ItemFriend;
import com.example.cinemates.ui.CineMates.friends.ItemUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = HomeActivity.class.getSimpleName();
    private ActivityHomeBinding binding;
    private ChipNavigationBar bottomNav;
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private HomeFragment homeFragment;
    private FirebaseFirestore db;
    private PopularFilms popularFilms;
    private boolean pop = false;
    private Handler handler;
    private int userNumber;
    private StorageReference storageRef;
    private FirebaseStorage storage;
    private Bitmap profilePic;
    private int rapporto;
    private ItemUser itemUser;
    Activity activity;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ArrayList<ItemFilm> films = new ArrayList<>();
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        final LoadingDialog loadingDialog = new LoadingDialog(HomeActivity.this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MoviesApiService moviesApiService = retrofit.create(MoviesApiService.class);
        Call<PopularFilms> call = moviesApiService.movieList();
        call.enqueue(new Callback<PopularFilms>() {
            @Override
            public void onResponse(Call<PopularFilms> call, Response<PopularFilms> response) {
                popularFilms = response.body();
                for (Movie movie : popularFilms.getResults())
                    films.add(new ItemFilm(movie.getTitle(), ProfileFragment.getBitmapFromdownload(
                            "https://image.tmdb.org/t/p/w185" + movie.getPoster_path())));
                pop = true;
            }

            @Override
            public void onFailure(Call<PopularFilms> call, Throwable t) {
                Log.e("Errore", "Errore nel caricamento delle api.");
            }
        });

        bottomNav =  binding.navHomeMenu;

        if(savedInstanceState == null){
            bottomNav.setItemSelected(R.id.main, true);
            fragmentManager = getSupportFragmentManager();
            loadingDialog.startLoadingDialog();
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadingDialog.dismissDialog();
                }
            }, 2500);

            new Thread(()-> {
                while(!pop){}
                fragment = new HomeFragment(popularFilms, films);
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_home_container, fragment)
                        .commit();
            }).start();
        }
        bottomNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                fragment = null;
                boolean isProfile = false;
                boolean isHome = false;
                boolean isFriends = false;
                switch (id){
                    case R.id.main:
                        isHome = true;
                        loadingDialog.startLoadingDialog();
                        handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.dismissDialog();
                            }
                        }, 2500);

                        new Thread(()-> {
                            while (!pop){}
                            fragment = new HomeFragment(popularFilms, films);
                            fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_home_container, fragment)
                                    .commit();
                        }).start();
                        break;
                    case R.id.search:
                        fragment = new SearchFragment();
                        loadingDialog.startLoadingDialog();
                        handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.dismissDialog();
                            }
                        }, 2500);
                        break;
                    case R.id.profilo:
                        isProfile = true;
                        loadingDialog.startLoadingDialog();
                        handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.dismissDialog();
                            }
                        }, 2500);

                        new Thread(()->{
                            profilePic = null;
                            String url = ProfileFragment.getUrlImage(db, FirebaseAuth.getInstance().getUid());
                            String usernameText = ProfileFragment.getUsernameText(db, FirebaseAuth.getInstance().getUid());
                            String emailText = ProfileFragment.getEmailText(db, FirebaseAuth.getInstance().getUid());
                            profilePic = ProfileFragment.getBitmapFromdownload(url);
                            fragment = new ProfileFragment(profilePic, usernameText, emailText);
                            fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_home_container, fragment)
                                    .commit();

                        }).start();
                        break;
                    case R.id.amici:
                        isFriends = true;
                        List<ItemUser> userList = new ArrayList<>();
                        List<ItemFriend> friendList = new ArrayList<>();
                        loadingDialog.startLoadingDialog();
                        handler = new Handler();
                        String currUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.dismissDialog();
                            }
                        }, 2500);

                        new Thread(()->{

                            //new Thread(()->{
                            CollectionReference collectionReference1 = db.collection("users");
                            collectionReference1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                    new Thread(()-> {
                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            String uidAmico = documentSnapshot.getString("uid");
                                            DocumentReference documentReference = db.collection("friends").document(currUser).collection(uidAmico).document(uidAmico);
                                            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if(document.exists()) {
                                                            String username = documentSnapshot.getString("username");
                                                            String imageUrl = documentSnapshot.getString("imageUrl");
                                                            ItemFriend friend = new ItemFriend(username, ProfileFragment.getBitmapFromdownload(imageUrl));
                                                            friend.setUid(documentSnapshot.getString("uid"));
                                                            friendList.add(friend);
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    }).start();
                                }
                            });

                            CollectionReference collectionReference = db.collection("users");
                            collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    userNumber = queryDocumentSnapshots.size();
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        if (!currUser.equals(documentSnapshot.getString("uid"))) {

                                            new Thread(()->{
                                                String uidDestinatario = documentSnapshot.getString("uid");
                                                DocumentReference documentReference = db.collection("friend request").document(currUser).collection(uidDestinatario).document(uidDestinatario);
                                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot document = task.getResult();
                                                            if (document.exists()) {
                                                                itemUser = new ItemUser(documentSnapshot.getString("username"), ProfileFragment.getBitmapFromdownload(documentSnapshot.getString("imageUrl")));
                                                                rapporto = 1;
                                                                itemUser.setRapporto(rapporto);
                                                                itemUser.setUid(documentSnapshot.getString("uid"));
                                                                userList.add(itemUser);
                                                            } else {
                                                                itemUser = new ItemUser(documentSnapshot.getString("username"), ProfileFragment.getBitmapFromdownload(documentSnapshot.getString("imageUrl")));
                                                                rapporto = 0;
                                                                itemUser.setRapporto(rapporto);
                                                                itemUser.setUid(documentSnapshot.getString("uid"));
                                                                userList.add(itemUser);
                                                            }
                                                        }
                                                    }
                                                });
                                            }).start();
                                        }
                                    }
                                }
                            });
                            while (userList.size() != userNumber - 1) { }
                            for(int i=0;i<userList.size(); i++){
                                for(int j=0; j<friendList.size();j++) {
                                    if (friendList.get(j).getUsername() == userList.get(i).getUsername()){
                                        userList.get(i).setRapporto(2);
                                    }
                                }
                            }
                            while (userList.size() != userNumber - 1) { }
                            fragment = new FriendsFragment(userList, friendList);
                            fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_home_container, fragment)
                                    .commit();
                        }).start();
                        break;
                }
                if(fragment!=null && !isProfile && !isHome && !isFriends){
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_home_container, fragment)
                            .commit();
                }
            }
        });
    }
}