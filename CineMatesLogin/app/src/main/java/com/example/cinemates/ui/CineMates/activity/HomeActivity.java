package com.example.cinemates.ui.CineMates.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.cinemates.R;
import com.example.cinemates.databinding.ActivityHomeBinding;
import com.example.cinemates.ui.CineMates.Fragment.FriendsFragment;
import com.example.cinemates.ui.CineMates.Fragment.HomeFragment;
import com.example.cinemates.ui.CineMates.Fragment.ProfileFragment;
import com.example.cinemates.ui.CineMates.Fragment.SearchFragment;
import com.example.cinemates.ui.CineMates.MovieListContract;
import com.example.cinemates.ui.CineMates.MovieListPresenter;
import com.example.cinemates.ui.CineMates.friends.model.ItemFriend;
import com.example.cinemates.ui.CineMates.friends.model.ItemUser;
import com.example.cinemates.ui.CineMates.model.ItemFilm;
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
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements MovieListContract.View {
    private ActivityHomeBinding binding;
    private ChipNavigationBar bottomNav;
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private FirebaseFirestore db;
    //private PopularFilms popularFilms;
    //private UpComingFilms upComingFilms;
    //private NowPlayingFilms nowPlayingFilms;
    private boolean pop = false;
    private Handler handler;
    private int userNumber;
    private FirebaseAuth auth;
    private Bitmap profilePic;
    private int rapporto;
    private String currUser;
    private ItemUser itemUser;
    private boolean notifiche = false;
    private MovieListPresenter movieListPresenter;
    final LoadingDialog loadingDialog = new LoadingDialog(HomeActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ArrayList<ItemFilm> filmsPopular = new ArrayList<>();
        //ArrayList<ItemFilm> filmsUpcoming = new ArrayList<>();
        //ArrayList<ItemFilm> filmsNowplaying = new ArrayList<>();
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        handler = new Handler();
        movieListPresenter = new MovieListPresenter(this);
        movieListPresenter.requestDataFromServer();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currUser = auth.getCurrentUser().getUid();

        //final LoadingDialog loadingDialog = new LoadingDialog(HomeActivity.this);

        CollectionReference collectionReference = db.collection("users");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    DocumentReference documentReference = db.collection("friend request").document(documentSnapshot.getString("uid")).collection(currUser).document(currUser);
                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    notifiche = true;
                                }
                            }
                        }
                    });
                }
            }
        });

        /*Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MoviesApiNowPlaying moviesApiNowPlaying = retrofit.create(MoviesApiNowPlaying.class);
        Call<NowPlayingFilms> callNp = moviesApiNowPlaying.movieList();
        callNp.enqueue(new Callback<NowPlayingFilms>() {
            @Override
            public void onResponse(Call<NowPlayingFilms> call, Response<NowPlayingFilms> response) {
                HomeActivity.this.nowPlayingFilms = response.body();
                for (Movie movie : HomeActivity.this.nowPlayingFilms.getResults())
                    filmsNowplaying.add(new ItemFilm(movie.getTitle(), ProfileFragment.getBitmapFromdownload(
                            "https://image.tmdb.org/t/p/w185" + movie.getPoster_path()), movie.getId()));
            }

            @Override
            public void onFailure(Call<NowPlayingFilms> call, Throwable t) {
                Log.e("Errore", "Errore nel caricamento delle api.");
            }
        });

        MoviesApiUpcoming moviesApiUpcoming = retrofit.create(MoviesApiUpcoming.class);
        Call<UpComingFilms> callUp = moviesApiUpcoming.movieList();
        callUp.enqueue(new Callback<UpComingFilms>() {
            @Override
            public void onResponse(Call<UpComingFilms> call, Response<UpComingFilms> response) {
                HomeActivity.this.upComingFilms = response.body();
                for (Movie movie : HomeActivity.this.upComingFilms.getResults())
                    filmsUpcoming.add(new ItemFilm(movie.getTitle(), ProfileFragment.getBitmapFromdownload(
                            "https://image.tmdb.org/t/p/w185" + movie.getPoster_path()), movie.getId()));
            }

            @Override
            public void onFailure(Call<UpComingFilms> call, Throwable t) {
                Log.e("Errore", "Errore nel caricamento delle api.");
            }
        });


        MoviesApiPopular moviesApiPopular = retrofit.create(MoviesApiPopular.class);
        Call<PopularFilms> callPop = moviesApiPopular.movieList();
        callPop.enqueue(new Callback<PopularFilms>() {
            @Override
            public void onResponse(Call<PopularFilms> call, Response<PopularFilms> response) {
                HomeActivity.this.popularFilms = response.body();
                for (Movie movie : HomeActivity.this.popularFilms.getResults())
                    filmsPopular.add(new ItemFilm(movie.getTitle(), ProfileFragment.getBitmapFromdownload(
                            "https://image.tmdb.org/t/p/w185" + movie.getPoster_path()), movie.getId()));
                pop = true;
            }

            @Override
            public void onFailure(Call<PopularFilms> call, Throwable t) {
                Log.e("Errore", "Errore nel caricamento delle api.");
                pop = true;
            }
        });*/

        bottomNav =  binding.navHomeMenu;

        if(savedInstanceState == null){
            bottomNav.setItemSelected(R.id.main, true);
            fragmentManager = getSupportFragmentManager();
            /*loadingDialog.startLoadingDialog();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadingDialog.dismissDialog();
                }
            }, 3000);*/
            new Thread(()-> {
                while(!pop){}
                fragment = new HomeFragment();
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
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.dismissDialog();
                            }
                        }, 3000);

                        new Thread(()-> {
                            while (!pop){}
                            fragment = new HomeFragment();
                            fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_home_container, fragment)
                                    .commit();
                        }).start();
                        break;
                    case R.id.search:
                        fragment = new SearchFragment();
                        loadingDialog.startLoadingDialog();
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
                        List<ItemUser> userList = new ArrayList<>();
                        List<ItemFriend> friendList = new ArrayList<>();
                        loadingDialog.startLoadingDialog();
                        String currUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.dismissDialog();
                            }
                        }, 2500);

                        new Thread(()->{
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
                                                            ItemFriend friend = new ItemFriend(username, ProfileFragment.getBitmapFromdownload(imageUrl), documentSnapshot.getString("uid"));
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
                                                                rapporto = 1;
                                                                itemUser = new ItemUser(documentSnapshot.getString("username"), ProfileFragment.getBitmapFromdownload(documentSnapshot.getString("imageUrl")), documentSnapshot.getString("uid"), rapporto);
                                                                userList.add(itemUser);
                                                            } else {
                                                                rapporto = 0;
                                                                itemUser = new ItemUser(documentSnapshot.getString("username"), ProfileFragment.getBitmapFromdownload(documentSnapshot.getString("imageUrl")), documentSnapshot.getString("uid"), rapporto);
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
                            fragment = new FriendsFragment(userList, friendList, notifiche);
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

    @Override
    public void showProgress() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setDataToRecyclerView(List<ItemFilm> movieArrayList) {
        pop = true;
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Log.e("Errore", throwable.getMessage());
        Toast.makeText(this, "Errore", Toast.LENGTH_LONG).show();
    }

}