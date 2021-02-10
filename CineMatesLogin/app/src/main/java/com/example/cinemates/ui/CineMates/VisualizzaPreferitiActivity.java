package com.example.cinemates.ui.CineMates;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.cinemates.databinding.ActivityVisualizzaPreferitiBinding;
import com.example.cinemates.ui.CineMates.ApiMovie.DetailedMovieApi;
import com.example.cinemates.ui.CineMates.ApiMovie.model.DetailedMovie;
import com.example.cinemates.ui.CineMates.Fragment.ProfileFragment;
import com.example.cinemates.ui.CineMates.adapter.RecycleViewAdapter_Film_ListaPreferiti_Amico;
import com.example.cinemates.ui.CineMates.model.ItemFilm;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import Intefaces.UpdateableFragmentListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VisualizzaPreferitiActivity extends AppCompatActivity implements RecycleViewAdapter_Film_ListaPreferiti_Amico.OnClickListener, UpdateableFragmentListener {
    private ActivityVisualizzaPreferitiBinding binding;
    private List<ItemFilm> preferitiList;
    private String username;
    private FirebaseFirestore db;
    //private FirebaseAuth auth;
    //private String currUser;
    private List<ItemFilm> searchList;
    private DetailedMovie detailedMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVisualizzaPreferitiBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        db = FirebaseFirestore.getInstance();
        //auth = FirebaseAuth.getInstance();
        //currUser = auth.getCurrentUser().getUid();
        preferitiList = new ArrayList<>();
        searchList = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
                username = extras.getString("username");
                binding.visualizzaPreferitiTextviewVisualizzaPreferiti.setText("Lista preferiti di "+username);
        }

            new Thread(()-> {
                CollectionReference collectionReference = db.collection("users");
                collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (username.equals(documentSnapshot.getString("username"))) {
                                String uid = documentSnapshot.getString("uid");

                                new Thread(() -> {
                                    CollectionReference collectionReference = db.collection("favorites").document(uid).collection(uid);
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
                                                        preferitiList.add(new ItemFilm(detailedMovie.getTitle(), ProfileFragment.getBitmapFromdownload("https://image.tmdb.org/t/p/w185" + detailedMovie.getPoster_path()), detailedMovie.getId()));
                                                        searchList.add(new ItemFilm(detailedMovie.getTitle(), ProfileFragment.getBitmapFromdownload("https://image.tmdb.org/t/p/w185" + detailedMovie.getPoster_path()), detailedMovie.getId()));
                                                        RecycleViewAdapter_Film_ListaPreferiti_Amico recycleViewAdapter_film_listaPreferiti_amico = new RecycleViewAdapter_Film_ListaPreferiti_Amico(VisualizzaPreferitiActivity.this, searchList, VisualizzaPreferitiActivity.this);
                                                        binding.filmVisualizzaPreferitiRecycleView.setLayoutManager(new GridLayoutManager(VisualizzaPreferitiActivity.this, 2, GridLayoutManager.VERTICAL, false));
                                                        binding.filmVisualizzaPreferitiRecycleView.setAdapter(recycleViewAdapter_film_listaPreferiti_amico);
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
                            }
                        }
                    }
                });
            }).start();

        Keyboard(binding);
        HomeButton(binding);
        BackButton(binding);
        SearchButton(binding);
    }

    private void Keyboard(ActivityVisualizzaPreferitiBinding binding) {
        binding.containerVisualizzaPreferiti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.containerVisualizzaPreferiti.getWindowToken(), 0);
                binding.searchBarVisualizzaPreferiti.clearFocus();
            }
        });
    }

    private void HomeButton(ActivityVisualizzaPreferitiBinding binding){
        binding.homeButtonVisualizzaPreferiti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VisualizzaPreferitiActivity.this, HomeActivity.class));
            }
        });
    }

    private void BackButton(ActivityVisualizzaPreferitiBinding binding){
        binding.backButtonVisualizzaPreferiti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void OnClickScheda(int position) {
        Intent i = new Intent(this, SchedaFilmActivity.class);
        i.putExtra("id", searchList.get(position).getId());
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void update() {
        binding.filmVisualizzaPreferitiRecycleView.getAdapter().notifyDataSetChanged();
    }

    public void SearchButton(ActivityVisualizzaPreferitiBinding binding){
        binding.searchButtonVisualizzaPreferiti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchList = new ArrayList<>();
                InputMethodManager imm = (InputMethodManager) VisualizzaPreferitiActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.containerVisualizzaPreferiti.getWindowToken(), 0);
                binding.searchBarVisualizzaPreferiti.clearFocus();
                String ricerca = binding.searchBarVisualizzaPreferiti.getText().toString().toLowerCase();
                if(ricerca.length() != 0) {
                    for (int i = 0; i < preferitiList.size(); i++) {
                        String titolo = preferitiList.get(i).getTitolo().toLowerCase();
                        if (titolo.contains(ricerca)) {
                            ItemFilm newFilm = new ItemFilm(preferitiList.get(i).getTitolo(), preferitiList.get(i).getBitmap(), preferitiList.get(i).getId());
                            searchList.add(newFilm);
                        }
                    }
                    if(searchList.size() != 0) {
                        RecycleViewAdapter_Film_ListaPreferiti_Amico recycleViewAdapter_film_listaPreferiti_amico = new RecycleViewAdapter_Film_ListaPreferiti_Amico(VisualizzaPreferitiActivity.this, searchList, VisualizzaPreferitiActivity.this);
                        binding.filmVisualizzaPreferitiRecycleView.setLayoutManager(new GridLayoutManager(VisualizzaPreferitiActivity.this, 2, GridLayoutManager.VERTICAL, false));
                        binding.filmVisualizzaPreferitiRecycleView.setAdapter(recycleViewAdapter_film_listaPreferiti_amico);
                        update();
                    }else{
                        RecycleViewAdapter_Film_ListaPreferiti_Amico recycleViewAdapter_film_listaPreferiti_amico = new RecycleViewAdapter_Film_ListaPreferiti_Amico(VisualizzaPreferitiActivity.this, searchList, VisualizzaPreferitiActivity.this);
                        binding.filmVisualizzaPreferitiRecycleView.setLayoutManager(new GridLayoutManager(VisualizzaPreferitiActivity.this, 2, GridLayoutManager.VERTICAL, false));
                        binding.filmVisualizzaPreferitiRecycleView.setAdapter(recycleViewAdapter_film_listaPreferiti_amico);
                        update();
                        Toast.makeText(VisualizzaPreferitiActivity.this, "Nessun film trovato con quel titolo!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    searchList = preferitiList;
                    RecycleViewAdapter_Film_ListaPreferiti_Amico recycleViewAdapter_film_listaPreferiti_amico = new RecycleViewAdapter_Film_ListaPreferiti_Amico(VisualizzaPreferitiActivity.this, searchList, VisualizzaPreferitiActivity.this);
                    binding.filmVisualizzaPreferitiRecycleView.setLayoutManager(new GridLayoutManager(VisualizzaPreferitiActivity.this, 2, GridLayoutManager.VERTICAL, false));
                    binding.filmVisualizzaPreferitiRecycleView.setAdapter(recycleViewAdapter_film_listaPreferiti_amico);
                    update();
                    Toast.makeText(VisualizzaPreferitiActivity.this, "Nessun parametro di ricerca inserito!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}