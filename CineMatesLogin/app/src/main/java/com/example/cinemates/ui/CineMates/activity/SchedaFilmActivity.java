package com.example.cinemates.ui.CineMates.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemates.R;
import com.example.cinemates.databinding.ActivitySchedaFilmBinding;
import com.example.cinemates.ui.CineMates.ApiMovie.Contract.MovieDetailsContract;
import com.example.cinemates.ui.CineMates.ApiMovie.Presenter.MovieDetailsPresenter;
import com.example.cinemates.ui.CineMates.ApiMovie.model.Genere;
import com.example.cinemates.ui.CineMates.Fragment.InfoFilmFragment;
import com.example.cinemates.ui.CineMates.Fragment.RecensioniFilmFragment;
import com.example.cinemates.ui.CineMates.Fragment.ViewPagerAdapter;
import com.example.cinemates.ui.CineMates.adapter.RecycleViewAdapter_Recensioni;
import com.example.cinemates.ui.CineMates.model.ItemRecensione;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.ArrayList;
import java.util.List;

import Intefaces.UpdateableFragmentListener;

public class SchedaFilmActivity extends AppCompatActivity implements UpdateableFragmentListener, MovieDetailsContract.View, RecycleViewAdapter_Recensioni.OnClickListener, UpdateableFragmentListener, RatingDialogListener {
    private ActivitySchedaFilmBinding binding;
    private int id;
    private ArrayList<Genere> generelist;
    private List<ItemRecensione> recensioniList;
    private Chip chip;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private boolean presente;
    private String currUser;
    private int preferiti;
    private int daVedere;
    private String movieName;
    private MovieDetailsPresenter movieDetailsPresenter;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySchedaFilmBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        adapter = new ViewPagerAdapter(this.getSupportFragmentManager());
        adapter.AddFragment(new InfoFilmFragment(), "Scheda Film");
        adapter.AddFragment(new RecensioniFilmFragment(), "Recensioni");

        binding.viewPagerSchedaFilm.setAdapter(adapter);
        binding.tabLayoutSchedaFilm.setupWithViewPager(binding.viewPagerSchedaFilm);
        binding.tabLayoutSchedaFilm.getTabAt(0).setIcon(R.drawable.ic_info);
        binding.tabLayoutSchedaFilm.getTabAt(1).setIcon(R.drawable.ic_stella);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currUser = auth.getCurrentUser().getUid();
        recensioniList = new ArrayList<>();
        presente = false;
        preferiti = 0;
        daVedere = 0;

       /* //Prendo l'id del film dalla scheda da cui provengo
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt(KEY_MOVIE_ID);
        }

        movieDetailsPresenter = new MovieDetailsPresenter(this);
        movieDetailsPresenter.requestMovieData(id);

        //controllo lista da vedere
        new Thread(()-> {
            DocumentReference documentReference = db.collection("da vedere").document(currUser).collection(currUser).document(String.valueOf(id));
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            daVedere = 1;
                            binding.davedereImageViewSchedaFilm.setImageResource(R.drawable.ic_davedere);
                        }
                    }
                }
            });
        }).start();

        //Controllo lista preferiti
        new Thread(()-> {
            DocumentReference documentReference = db.collection("favorites").document(currUser).collection(currUser).document(String.valueOf(id));
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            preferiti = 1;
                            binding.aggiungiPreferitiSchefaFilmImageView.setImageResource(R.drawable.ic_favorite);
                        }
                    }
                }
            });
        }).start();

        //Controllo recensioni
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
                                        recensioniList.add(new ItemRecensione(username, document.getString("review"), voto, profilePic, documentSnapshot.getId()));
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
        }).start();*/

        Keyboard(binding);
        BackButton(binding);
        update();
        /*daVedereClick(binding);
        Preferiti(binding);
        recensioneButton(binding);*/
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

    @Override
    public void update() {
        adapter.notifyDataSetChanged();
    }


    /*private void daVedereClick(ActivitySchedaFilmBinding binding){
        binding.davedereImageViewSchedaFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Controllo lista da vedere
                new Thread(()-> {
                    DocumentReference documentReference = db.collection("da vedere").document(currUser).collection(currUser).document(String.valueOf(id));
                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if(document.exists()){
                                    daVedere = 1;
                                    binding.davedereImageViewSchedaFilm.setImageResource(R.drawable.ic_davedere);
                                }else
                                    preferiti = 0;
                            }
                        }
                    });
                }).start();

                if(daVedere == 0){
                    PreferitiModel preferitiModel = new PreferitiModel(currUser, id);
                    db.collection("da vedere").document(currUser).collection(currUser).document(String.valueOf(id)).set(preferitiModel);
                    binding.davedereImageViewSchedaFilm.setImageResource(R.drawable.ic_davedere);
                    Toast.makeText(SchedaFilmActivity.this, "Film aggiunto alla lista \"da vedere\"", Toast.LENGTH_SHORT).show();
                }else{
                    db.collection("da vedere").document(currUser).collection(currUser).document(String.valueOf(id)).delete();
                    binding.davedereImageViewSchedaFilm.setImageResource(R.drawable.ic_davedere_nonpresente);
                    Toast.makeText(SchedaFilmActivity.this, "Film rimosso dalla lista \"da vedere\"", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void Preferiti(ActivitySchedaFilmBinding binding){
        binding.aggiungiPreferitiSchefaFilmImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Controllo lista preferiti
                new Thread(()-> {
                    DocumentReference documentReference = db.collection("favorites").document(currUser).collection(currUser).document(String.valueOf(id));
                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if(document.exists()){
                                    preferiti = 1;
                                    binding.aggiungiPreferitiSchefaFilmImageView.setImageResource(R.drawable.ic_favorite);
                                }else
                                    preferiti = 0;
                            }
                        }
                    });
                }).start();

                if(preferiti == 0){
                    PreferitiModel preferitiModel = new PreferitiModel(currUser, id);
                    db.collection("favorites").document(currUser).collection(currUser).document(String.valueOf(id)).set(preferitiModel);
                    binding.aggiungiPreferitiSchefaFilmImageView.setImageResource(R.drawable.ic_favorite);
                }else{
                    db.collection("favorites").document(currUser).collection(currUser).document(String.valueOf(id)).delete();
                    binding.aggiungiPreferitiSchefaFilmImageView.setImageResource(R.drawable.ic_favorite_border);
                }
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
                .setTitle("Recensisci " + movieName)
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
                    itemRecensione.setVoto(i);
                }
                RecycleViewAdapter_Recensioni recycleViewAdapterRecensioni = new RecycleViewAdapter_Recensioni(SchedaFilmActivity.this, recensioniList, SchedaFilmActivity.this);
                binding.recycleViewRecensioniSchedaFilm.setLayoutManager(new LinearLayoutManager(SchedaFilmActivity.this));
                binding.recycleViewRecensioniSchedaFilm.setAdapter(recycleViewAdapterRecensioni);
            }
        }else{
            DocumentReference documentReference = db.collection("users").document(currUser);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        recensioniList.add(new ItemRecensione(documentSnapshot.getString("username"), s, i, ProfileFragment.getBitmapFromdownload(documentSnapshot.getString("imageUrl")), documentSnapshot.getString("uid")));
                        presente = true;
                        RecycleViewAdapter_Recensioni recycleViewAdapterRecensioni = new RecycleViewAdapter_Recensioni(SchedaFilmActivity.this, recensioniList, SchedaFilmActivity.this);
                        binding.recycleViewRecensioniSchedaFilm.setLayoutManager(new LinearLayoutManager(SchedaFilmActivity.this));
                        binding.recycleViewRecensioniSchedaFilm.setAdapter(recycleViewAdapterRecensioni);
                    }
                }
            });
        }
    }

    @Override
    public void showProgress() {
        binding.progressBarSchedaFilm.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        binding.progressBarSchedaFilm.setVisibility(View.GONE);
    }

    @Override
    public void setDataCredits(String regista) {
        if(!regista.equals("")){
            binding.nomeRegistaFilmSchedaFilmTextView.setText(regista);
        }
    }

    @Override
    public void setDataLista(DetailedMovie movie) {

    }

    @Override
    public void setDataToViews(DetailedMovie movie) {
        if (movie != null) {
            float voto = movie.getVote_average() / 2;
            LayoutInflater inflater = LayoutInflater.from(SchedaFilmActivity.this);
            movieName = movie.getTitle();
            binding.titoloFIlmSchedaFilm.setText(movie.getTitle() + " (" + movie.getRelease_date().substring(0, 4) + ")");
            binding.tramaSchedaFilmTextView.setText(movie.getOverview());
            binding.percentualeVotoSchedaFilmTextView.setText(String.format("%.1f", voto));
            binding.locandinaFilmSchedaFilm.setImageBitmap(ProfileFragment.getBitmapFromdownload(IMAGE_BASE_URL + movie.getPoster_path()));
            generelist = movie.getGenere();
            for (Genere genere : generelist) {
                chip = (Chip) inflater.inflate(R.layout.item_chip, null, false);
                chip.setText(genere.getNome());
                binding.genereFilmSchedaFilmChipGroup.addView(chip);
            }
        }
    }

    @Override
    public void onResponseFailure (Throwable throwable){
        Toast.makeText(this, "Errore nel caricamento", Toast.LENGTH_SHORT).show();
    }*/
}