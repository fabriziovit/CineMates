package com.example.cinemates.ui.CineMates;

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
import com.example.cinemates.ui.CineMates.ApiMovie.MoviesApiService;
import com.example.cinemates.ui.CineMates.ApiMovie.PopularFilms;
import com.example.cinemates.ui.CineMates.Fragment.FriendsFragment;
import com.example.cinemates.ui.CineMates.Fragment.HomeFragment;
import com.example.cinemates.ui.CineMates.Fragment.ProfileFragment;
import com.example.cinemates.ui.CineMates.Fragment.SearchFragment;
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
    ChipNavigationBar bottomNav;
    FragmentManager fragmentManager;
    Fragment fragment;
    HomeFragment homeFragment;
    FirebaseFirestore db;
    public PopularFilms popularFilms;
    boolean pop = false;
    Handler handler;
    int userNumber;
    private StorageReference storageRef;
    private FirebaseStorage storage;
    private Bitmap profilePic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
            }, 3000);
            new Thread(()-> {
                while(!pop){}
                homeFragment = new HomeFragment(popularFilms);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_home_container, homeFragment)
                        .commit();
            }).start();
        }

        bottomNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                boolean isProfile = false;
                boolean isHome = false;
                boolean isFriends = false;
                fragment = null;
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
                        }, 3000);

                        new Thread(()-> {
                            while (!pop){}
                            fragment = new HomeFragment(popularFilms);
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
                        }, 3000);
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
                        }, 3000);

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
                        loadingDialog.startLoadingDialog();
                        handler = new Handler();
                        String currUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.dismissDialog();
                            }
                        }, 3000);

                        new Thread(()->{
                            List<ItemUser> userList = new ArrayList<>();
                            CollectionReference collectionReference = db.collection("users");
                            collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    userNumber = queryDocumentSnapshots.size();
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        if (!currUser.equals(documentSnapshot.getString("uid"))) {
                                            /*if (documentSnapshot.getString("imageUrl").equals("default")) {
                                                ItemUser itemUser = new ItemUser(documentSnapshot.getString("username"), ProfileFragment.getBitmapFromdownload("https://image.flaticon.com/icons/png/128/1077/1077114.png"));

                                                new Thread(()-> {
                                                    DocumentReference documentReference = db.collection("friend request").document(documentSnapshot.getString("uid"));
                                                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                DocumentSnapshot document = task.getResult();
                                                                if (document.exists() && document.getString("uIdMittente").equals(currUser)) {
                                                                    itemUser.setRapporto(1);
                                                                    userList.add(itemUser);
                                                                } else {
                                                                    //Controllo non presente in amici, se è presente aggiungere 2
                                                                    itemUser.setRapporto(0);
                                                                    userList.add(itemUser);
                                                                    //altrimenti aggiunta al rapporto 0 in quanto non amici e non inviata nesdsuna richiesta
                                                                }
                                                            }
                                                        }
                                                    });
                                                }).start();
                                            } else {*/

                                                new Thread(()->{
                                                    ItemUser itemUser = new ItemUser(documentSnapshot.getString("username"), ProfileFragment.getBitmapFromdownload(documentSnapshot.getString("imageUrl")));
                                                    DocumentReference documentReference = db.collection("friend request").document(documentSnapshot.getString("uid"));
                                                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                DocumentSnapshot document = task.getResult();
                                                                if (document.exists() && document.getString("uIdMittente").equals(currUser)) {
                                                                    itemUser.setRapporto(1);
                                                                    userList.add(itemUser);
                                                                } else {
                                                                    //Controllo non presente in amici, se è presente aggiungere 2
                                                                    itemUser.setRapporto(0);
                                                                    userList.add(itemUser);
                                                                    //altrimenti aggiunta al rapporto 0 in quanto non amici e non inviata nesdsuna richiesta
                                                                }
                                                            }
                                                        }
                                                    });
                                                }).start();
                                            }
                                        }
                                    }
                                //}
                            });
                            while (userList.size() != userNumber-1) {
                            }
                            fragment = new FriendsFragment(userList);
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