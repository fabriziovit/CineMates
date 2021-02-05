package com.example.cinemates.ui.CineMates;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemates.R;
import com.example.cinemates.databinding.ActivityListeUtenteBinding;
import com.example.cinemates.ui.CineMates.ApiMovie.DetailedMovieApi;
import com.example.cinemates.ui.CineMates.ApiMovie.model.DetailedMovie;
import com.example.cinemates.ui.CineMates.Fragment.DaVedereFragment;
import com.example.cinemates.ui.CineMates.Fragment.PreferitiFragment;
import com.example.cinemates.ui.CineMates.Fragment.ProfileFragment;
import com.example.cinemates.ui.CineMates.Fragment.ViewPagerAdapter;
import com.example.cinemates.ui.CineMates.model.ItemFilm;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListeUtenteActivity extends AppCompatActivity {
    ActivityListeUtenteBinding binding;
    private List<ItemFilm> filmPreferiti;
    private List<ItemFilm> filmDavedere;
    private ViewPagerAdapter adapter;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String currUser;
    private DetailedMovie detailedMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListeUtenteBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currUser = auth.getCurrentUser().getUid();
        filmPreferiti = new ArrayList<>();
        filmDavedere = new ArrayList<>();

        new Thread(() -> {
            CollectionReference collectionReference = db.collection("favorites").document(currUser).collection(currUser);
            collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("https://api.themoviedb.org")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        DetailedMovieApi detailedMovieApi = retrofit.create(DetailedMovieApi.class);
                        Call<DetailedMovie> call = detailedMovieApi.detailedMovie("3/movie/" + documentSnapshot.getLong("idFilm") + "?api_key=03941baf012eb2cd38196f9df8751df6");
                        call.enqueue(new Callback<DetailedMovie>() {
                            @Override
                            public void onResponse(Call<DetailedMovie> call, Response<DetailedMovie> response) {
                                detailedMovie = response.body();
                                filmPreferiti.add(new ItemFilm(detailedMovie.getTitle(), ProfileFragment.getBitmapFromdownload("https://image.tmdb.org/t/p/w185" + detailedMovie.getPoster_path()), detailedMovie.getId()));
                            }

                            @Override
                            public void onFailure(Call<DetailedMovie> call, Throwable t) {
                                Log.e("ERRORE", "caricamento Api non riuscito");
                            }
                        });
                    }
                }
            });
        }).start();

        new Thread(() -> {
            CollectionReference collectionReference = db.collection("da vedere").document(currUser).collection(currUser);
            collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("https://api.themoviedb.org")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        DetailedMovieApi detailedMovieApi = retrofit.create(DetailedMovieApi.class);
                        Call<DetailedMovie> call = detailedMovieApi.detailedMovie("3/movie/" + documentSnapshot.getLong("idFilm") + "?api_key=03941baf012eb2cd38196f9df8751df6");
                        call.enqueue(new Callback<DetailedMovie>() {
                            @Override
                            public void onResponse(Call<DetailedMovie> call, Response<DetailedMovie> response) {
                                detailedMovie = response.body();
                                filmDavedere.add(new ItemFilm(detailedMovie.getTitle(), ProfileFragment.getBitmapFromdownload("https://image.tmdb.org/t/p/w185" + detailedMovie.getPoster_path()), detailedMovie.getId()));
                            }

                            @Override
                            public void onFailure(Call<DetailedMovie> call, Throwable t) {
                                Log.e("ERRORE", "caricamento Api non riuscito");
                            }
                        });
                    }
                }
            });
        }).start();

        while (filmPreferiti.size() == 0){}
        adapter = new ViewPagerAdapter(ListeUtenteActivity.this.getSupportFragmentManager());
        binding.tabLayoutListeUtente.setupWithViewPager(binding.viewPagerListeUtente);
        adapter.AddFragment(new PreferitiFragment(filmPreferiti), "Preferiti");
        adapter.AddFragment(new DaVedereFragment(filmDavedere), "Da Vedere");
        binding.viewPagerListeUtente.setAdapter(adapter);

        BackButton(binding);
        TabLayout();
    }

    private void BackButton(ActivityListeUtenteBinding binding){
        binding.backButtonListeUtente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void TabLayout(){
        binding.tabLayoutListeUtente.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.tabLayoutListeUtente.getTabAt(0).setIcon(R.drawable.ic_favorite);
                binding.tabLayoutListeUtente.getTabAt(1).setIcon(R.drawable.ic_next);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                binding.tabLayoutListeUtente.getTabAt(0).setIcon(R.drawable.ic_favorite);
                binding.tabLayoutListeUtente.getTabAt(1).setIcon(R.drawable.ic_next);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                binding.tabLayoutListeUtente.getTabAt(0).setIcon(R.drawable.ic_favorite);
                binding.tabLayoutListeUtente.getTabAt(1).setIcon(R.drawable.ic_next);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void update(){
        adapter.update();
    }
}