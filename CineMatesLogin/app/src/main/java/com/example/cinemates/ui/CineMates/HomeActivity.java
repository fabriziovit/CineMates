package com.example.cinemates.ui.CineMates;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        db = FirebaseFirestore.getInstance();
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
            }, 6000);
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
                            Bitmap profilePic = null;
                            String url = ProfileFragment.getUrlImage(db, FirebaseAuth.getInstance().getUid());
                            String usernameText = ProfileFragment.getUsernameText(db, FirebaseAuth.getInstance().getUid());
                            String emailText = ProfileFragment.getEmailText(db, FirebaseAuth.getInstance().getUid());
                            if(url.equals("default"))
                                profilePic = ProfileFragment.getBitmapFromdownload("https://image.flaticon.com/icons/png/128/1077/1077114.png");
                            else
                                profilePic = ProfileFragment.getBitmapFromdownload(url);
                            fragment = new ProfileFragment(profilePic, usernameText, emailText);
                            fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_home_container, fragment)
                                    .commit();

                        }).start();
                        break;
                    case R.id.amici:
                        loadingDialog.startLoadingDialog();
                        handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.dismissDialog();
                            }
                        }, 3000);
                        fragment = new FriendsFragment();
                        break;
                }

                if(fragment!=null && !isProfile && !isHome){
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_home_container, fragment)
                            .commit();
                }
            }
        });
    }
}