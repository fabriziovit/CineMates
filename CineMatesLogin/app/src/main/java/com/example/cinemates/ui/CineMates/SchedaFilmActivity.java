package com.example.cinemates.ui.CineMates;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemates.R;
import com.example.cinemates.databinding.ActivitySchedaFilmBinding;
import com.example.cinemates.ui.CineMates.ApiMovie.CreditsMovie;
import com.example.cinemates.ui.CineMates.ApiMovie.CreditsMovieApi;
import com.example.cinemates.ui.CineMates.ApiMovie.Crew;
import com.example.cinemates.ui.CineMates.ApiMovie.DetailedMovie;
import com.example.cinemates.ui.CineMates.ApiMovie.DetailedMovieApi;
import com.example.cinemates.ui.CineMates.ApiMovie.Genere;
import com.example.cinemates.ui.CineMates.Fragment.ProfileFragment;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SchedaFilmActivity extends AppCompatActivity {
    private ActivitySchedaFilmBinding binding;
    private int id;
    private DetailedMovie detailedMovie;
    private CreditsMovie creditsMovie;
    private ArrayList<Genere> generelist;
    private ArrayList<Crew> crewlist;
    private String regista;
    private String generi;
    private Chip chip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySchedaFilmBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.tramaFilmSchedaFilmTextView.setMovementMethod(new ScrollingMovementMethod());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CreditsMovieApi creditsMovieApi = retrofit.create(CreditsMovieApi.class);
        Call<CreditsMovie> callCredits = creditsMovieApi.creditsMovie("3/movie/"+id+"/credits?api_key=03941baf012eb2cd38196f9df8751df6");
        callCredits.enqueue(new Callback<CreditsMovie>() {
            @Override
            public void onResponse(Call<CreditsMovie> call, Response<CreditsMovie> response) {
                creditsMovie = response.body();
                crewlist = creditsMovie.getCrew();
                for(Crew crew: crewlist){
                    if(crew.getJob().equals("Director"))
                        if(regista == null)
                            regista = crew.getName();
                        else
                            regista = regista+", "+crew.getName();
                }
                binding.nomeRegistaFilmSchedaFilmTextView.setText(regista);
            }
            @Override
            public void onFailure(Call<CreditsMovie> call, Throwable t) {
                Log.e("ERRORE", "caricamento Api non riuscito");
            }
        });

        DetailedMovieApi detailedMovieApi = retrofit.create(DetailedMovieApi.class);
        Call<DetailedMovie> call = detailedMovieApi.detailedMovie("3/movie/"+id+"?api_key=03941baf012eb2cd38196f9df8751df6");
        call.enqueue(new Callback<DetailedMovie>() {
            @Override
            public void onResponse(Call<DetailedMovie> call, Response<DetailedMovie> response) {
                detailedMovie = response.body();
                LayoutInflater inflater = LayoutInflater.from(SchedaFilmActivity.this);
                generelist = detailedMovie.getGenere();
                for(Genere genere: generelist){
                    chip = (Chip)inflater.inflate(R.layout.item_chip, null, false);
                    chip.setText(genere.getNome());
                    binding.genereFilmSchedaFilmChipGroup.addView(chip);
                }
                binding.titoloFIlmSchedaFilm.setText(detailedMovie.getTitle()+" ("+detailedMovie.getRelease_date().substring(0,4)+")");
                binding.tramaFilmSchedaFilmTextView.setText(detailedMovie.getOverview());
                binding.locandinaFilmSchedaFilm.setImageBitmap(ProfileFragment.getBitmapFromdownload("https://image.tmdb.org/t/p/w185" +detailedMovie.getPoster_path()));
            }

            @Override
            public void onFailure(Call<DetailedMovie> call, Throwable t) {
                Log.e("ERRORE", "caricamento Api non riuscito");
            }
        });



        Keyboard(binding);
        BackButton(binding);
        Preferiti(binding);
    }

    private void Keyboard(ActivitySchedaFilmBinding binding) {
        binding.containerSchedaFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.containerSchedaFilm.getWindowToken(), 0);
            }
        });
    }

    private void BackButton(ActivitySchedaFilmBinding binding){
        binding.backButtonSchedaFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void Preferiti(ActivitySchedaFilmBinding binding){
        binding.aggiungiPreferitiSchefaFilmImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //query per aggiungere il film ai preferiti
                binding.aggiungiPreferitiSchefaFilmImageView.setImageResource(R.drawable.ic_favorite);
            }
        });
    }
}