package com.example.cinemates.ui.CineMates;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cinemates.R;
import com.example.cinemates.databinding.ActivitySchedaFilmBinding;
import com.example.cinemates.ui.CineMates.ApiMovie.CreditsMovieApi;
import com.example.cinemates.ui.CineMates.ApiMovie.DetailedMovieApi;
import com.example.cinemates.ui.CineMates.ApiMovie.model.CreditsMovie;
import com.example.cinemates.ui.CineMates.ApiMovie.model.Crew;
import com.example.cinemates.ui.CineMates.ApiMovie.model.DetailedMovie;
import com.example.cinemates.ui.CineMates.ApiMovie.model.Genere;
import com.example.cinemates.ui.CineMates.Fragment.ProfileFragment;
import com.example.cinemates.ui.CineMates.adapter.RecycleViewAdapter_Recensioni;
import com.example.cinemates.ui.CineMates.model.ItemRecensione;
import com.example.cinemates.ui.CineMates.model.ReviewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import Intefaces.UpdateableFragmentListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SchedaFilmActivity extends AppCompatActivity implements RecycleViewAdapter_Recensioni.OnClickListener, UpdateableFragmentListener, RatingDialogListener {
    private ActivitySchedaFilmBinding binding;
    private int id;
    private DetailedMovie detailedMovie;
    private CreditsMovie creditsMovie;
    private ArrayList<Genere> generelist;
    private ArrayList<Crew> crewlist;
    private List<ItemRecensione> recensioniList;
    private String regista;
    private Chip chip;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private boolean presente = false;
    private String currUser;
    private int cont;
    private int somma;
    private double mediaPunteggio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currUser = auth.getCurrentUser().getUid();
        binding = ActivitySchedaFilmBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        recensioniList = new ArrayList<>();
        cont = 0;
        somma = 0;
        binding.tramaFilmSchedaFilmTextView.setMovementMethod(new ScrollingMovementMethod());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
        }
        new Thread(()-> {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            CreditsMovieApi creditsMovieApi = retrofit.create(CreditsMovieApi.class);
            Call<CreditsMovie> callCredits = creditsMovieApi.creditsMovie("3/movie/" + id + "/credits?api_key=03941baf012eb2cd38196f9df8751df6");
            callCredits.enqueue(new Callback<CreditsMovie>() {
                @Override
                public void onResponse(Call<CreditsMovie> call, Response<CreditsMovie> response) {
                    creditsMovie = response.body();
                    crewlist = creditsMovie.getCrew();
                    for (Crew crew : crewlist) {
                        if (crew.getJob().equals("Director"))
                            if (regista == null)
                                regista = crew.getName();
                            else
                                regista = regista + ", " + crew.getName();
                    }
                    binding.nomeRegistaFilmSchedaFilmTextView.setText(regista);
                }

                @Override
                public void onFailure(Call<CreditsMovie> call, Throwable t) {
                    Log.e("ERRORE", "caricamento Api non riuscito");
                }
            });

            DetailedMovieApi detailedMovieApi = retrofit.create(DetailedMovieApi.class);
            Call<DetailedMovie> call = detailedMovieApi.detailedMovie("3/movie/" + id + "?api_key=03941baf012eb2cd38196f9df8751df6");
            call.enqueue(new Callback<DetailedMovie>() {
                @Override
                public void onResponse(Call<DetailedMovie> call, Response<DetailedMovie> response) {
                    detailedMovie = response.body();
                    LayoutInflater inflater = LayoutInflater.from(SchedaFilmActivity.this);
                    generelist = detailedMovie.getGenere();
                    for (Genere genere : generelist) {
                        chip = (Chip) inflater.inflate(R.layout.item_chip, null, false);
                        chip.setText(genere.getNome());
                        binding.genereFilmSchedaFilmChipGroup.addView(chip);
                    }
                    binding.titoloFIlmSchedaFilm.setText(detailedMovie.getTitle() + " (" + detailedMovie.getRelease_date().substring(0, 4) + ")");
                    binding.tramaFilmSchedaFilmTextView.setText(detailedMovie.getOverview());
                    binding.locandinaFilmSchedaFilm.setImageBitmap(ProfileFragment.getBitmapFromdownload("https://image.tmdb.org/t/p/w185" + detailedMovie.getPoster_path()));
                }

                @Override
                public void onFailure(Call<DetailedMovie> call, Throwable t) {
                    Log.e("ERRORE", "caricamento Api non riuscito");
                }
            });
        }).start();

        new Thread(()-> {
            CollectionReference collectionReference1 = db.collection("users");
            collectionReference1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        DocumentReference documentReference = db.collection("reviews").document(String.valueOf(id)).collection(String.valueOf(id))
                                .document(documentSnapshot.getString("uid"));
                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        if (currUser.equals(documentSnapshot.getString("uid")))
                                            presente = true;
                                        Bitmap profilePic = ProfileFragment.getBitmapFromdownload(documentSnapshot.getString("imageUrl"));
                                        String username = documentSnapshot.getString("username");
                                        int voto = document.getLong("star").intValue();
                                        //int somma = somma+voto;
                                        //cont++
                                        //double mediaPunteggio = somma/cont;
                                        recensioniList.add(new ItemRecensione(username, document.getString("review"), voto, profilePic, documentSnapshot.getId()));
                                        //setText di voto medio con mediaPunteggio
                                        RecycleViewAdapter_Recensioni recycleViewAdapterRecensioni = new RecycleViewAdapter_Recensioni(SchedaFilmActivity.this, recensioniList, SchedaFilmActivity.this);
                                        binding.recycleViewRecensioniSchedaFilm.setLayoutManager(new LinearLayoutManager(SchedaFilmActivity.this));
                                        binding.recycleViewRecensioniSchedaFilm.setAdapter(recycleViewAdapterRecensioni);
                                    }
                                }
                            }
                        });
                    }
                }
            });
        }).start();

        Keyboard(binding);
        BackButton(binding);
        Preferiti(binding);
        recensioneButton(binding);
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
            }
        });
    }

    @Override
    public void update() {
        binding.recycleViewRecensioniSchedaFilm.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void OnClick(int position) {
        recensioniList.get(position);
    }

    public void recensioneButton(ActivitySchedaFilmBinding binding){
        binding.recensioneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    public void showDialog(){
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Invia")
                .setNegativeButtonText("Cancella")
                .setNumberOfStars(5)
                .setDefaultRating(1)
                .setTitle("Recensisci "+detailedMovie.getTitle())
                .setDescription("Seleziona il punteggio e scrivi la tua recensione")
                .setCommentInputEnabled(true)
                .setStarColor(R.color.colorIcons)
                .setNoteDescriptionTextColor(R.color.colorBack)
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorBack)
                .setHint("Scrivi la tua recensione qui...")
                .setHintTextColor(R.color.hintColorText)
                .setCommentTextColor(R.color.colorPrimary)
                .setCommentBackgroundColor(R.color.griogioChiaro)
                .setWindowAnimation(R.style.MyDialogFadeAnimation)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .create(SchedaFilmActivity.this)
                .show();
    }

    @Override
    public void onNegativeButtonClicked() {
    }

    @Override
    public void onNeutralButtonClicked() {
    }

    @Override
    public void onPositiveButtonClicked(int i, @NotNull String s) {
        ReviewModel reviewModel = new ReviewModel(currUser, s, i);
        db.collection("reviews").document(String.valueOf(id)).collection(String.valueOf(id)).document(currUser).set(reviewModel);
        if(presente){
            for(ItemRecensione itemRecensione : recensioniList){
                if(itemRecensione.getUid().equals(currUser)){
                    itemRecensione.setRecensione(s);
                    //somma = somma- itemRecensione.getvoto();
                    //somma = somma + i;
                    itemRecensione.setVoto(i);
                    update();
                }
                //mediaPunteggio = somma/cont
                //setText
            }
        }else{
            DocumentReference documentReference = db.collection("users").document(currUser);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        recensioniList.add(new ItemRecensione(documentSnapshot.getString("username"), s, i, ProfileFragment.getBitmapFromdownload(documentSnapshot.getString("imageUrl")), documentSnapshot.getString("uid")));
                        //somma=+i;
                        //cont++
                        //mediaPunteggio = somma/cont;
                        //setText
                        RecycleViewAdapter_Recensioni recycleViewAdapterRecensioni = new RecycleViewAdapter_Recensioni(SchedaFilmActivity.this, recensioniList, SchedaFilmActivity.this);
                        binding.recycleViewRecensioniSchedaFilm.setLayoutManager(new LinearLayoutManager(SchedaFilmActivity.this));
                        binding.recycleViewRecensioniSchedaFilm.setAdapter(recycleViewAdapterRecensioni);
                    }
                }
            });
        }
    }

}