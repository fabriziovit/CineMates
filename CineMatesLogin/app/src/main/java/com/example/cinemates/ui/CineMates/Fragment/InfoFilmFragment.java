package com.example.cinemates.ui.CineMates.Fragment;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.ApiMovie.Contract.MovieDetailsContract;
import com.example.cinemates.ui.CineMates.ApiMovie.Presenter.MovieDetailsPresenter;
import com.example.cinemates.ui.CineMates.ApiMovie.model.DetailedMovie;
import com.example.cinemates.ui.CineMates.ApiMovie.model.Genere;
import com.example.cinemates.ui.CineMates.model.PreferitiModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static com.example.cinemates.ui.CineMates.ApiMovie.ApiClient.IMAGE_BASE_URL;

public class InfoFilmFragment extends Fragment implements MovieDetailsContract.View {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ImageView preferitiButton;
    private ImageView daVedereButton;
    private int preferiti;
    private int daVedere;
    private int id;
    private MovieDetailsPresenter movieDetailsPresenter;
    private ProgressBar progressBar;
    private ArrayList<Genere> generelist;
    private Chip chip;
    public static String movieName;
    private TextView titoloFilm;
    private TextView tramaFilm;
    private TextView punteggioVoto;
    private ImageView locandinaFilm;
    private TextView registaText;
    private ChipGroup chipGroup;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String currUser;

    public InfoFilmFragment() {
        // Required empty public constructor
    }

    public InfoFilmFragment(int id){
        this.id = id;
    }

    public static InfoFilmFragment newInstance(String param1, String param2) {
        InfoFilmFragment fragment = new InfoFilmFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currUser = auth.getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_film, container, false);
        progressBar = view.findViewById(R.id.progressBar_SchedaFilm);
        preferitiButton = view.findViewById(R.id.aggiungiPreferiti_schefaFilm_imageView);
        daVedereButton = view.findViewById(R.id.davedere_imageView_schedaFilm);
        titoloFilm = view.findViewById(R.id.titoloFIlm_schedaFilm);
        tramaFilm = view.findViewById(R.id.trama_schedaFilm_textView);
        locandinaFilm = view.findViewById(R.id.locandinaFilm_schedaFilm);
        registaText = view.findViewById(R.id.nomeRegistaFilm_schedaFilm_textView);
        punteggioVoto = view.findViewById(R.id.percentualeVoto_schedaFilm_TextView);
        chipGroup = view.findViewById(R.id.genereFilm_schedaFilm_chipGroup);

        tramaFilm.setMovementMethod(new ScrollingMovementMethod());
        movieDetailsPresenter = new MovieDetailsPresenter(this);
        movieDetailsPresenter.requestMovieData(id);

        preferiti = 0;
        daVedere = 0;

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
                            daVedereButton.setImageResource(R.drawable.ic_davedere);
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
                            preferitiButton.setImageResource(R.drawable.ic_favorite);
                        }
                    }
                }
            });
        }).start();

        preferitiClick();
        daVedereClick();

        return view;
    }

    private void preferitiClick(){
        preferitiButton.setOnClickListener(new View.OnClickListener() {
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
                                    preferitiButton.setImageResource(R.drawable.ic_favorite);
                                }else
                                    preferiti = 0;
                            }
                        }
                    });
                }).start();

                if(preferiti == 0){
                    PreferitiModel preferitiModel = new PreferitiModel(currUser, id);
                    db.collection("favorites").document(currUser).collection(currUser).document(String.valueOf(id)).set(preferitiModel);
                    preferitiButton.setImageResource(R.drawable.ic_favorite);
                    Toast.makeText(getActivity(), "Film aggiunto alla lista preferiti", Toast.LENGTH_SHORT).show();
                }else{
                    db.collection("favorites").document(currUser).collection(currUser).document(String.valueOf(id)).delete();
                    preferitiButton.setImageResource(R.drawable.ic_favorite_border);
                    Toast.makeText(getActivity(), "Film aggiunto alla lista preferiti", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void daVedereClick(){
        daVedereButton.setOnClickListener(new View.OnClickListener() {
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
                                    daVedereButton.setImageResource(R.drawable.ic_davedere);
                                }else
                                    preferiti = 0;
                            }
                        }
                    });
                }).start();

                if(daVedere == 0){
                    PreferitiModel preferitiModel = new PreferitiModel(currUser, id);
                    db.collection("da vedere").document(currUser).collection(currUser).document(String.valueOf(id)).set(preferitiModel);
                    daVedereButton.setImageResource(R.drawable.ic_davedere);
                    Toast.makeText(getActivity(), "Film aggiunto alla lista \"da vedere\"", Toast.LENGTH_SHORT).show();
                }else{
                    db.collection("da vedere").document(currUser).collection(currUser).document(String.valueOf(id)).delete();
                    daVedereButton.setImageResource(R.drawable.ic_davedere_nonpresente);
                    Toast.makeText(getActivity(), "Film rimosso dalla lista \"da vedere\"", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setDataToViews(DetailedMovie movie) {
        if (movie != null) {
            float voto = movie.getVote_average() / 2;
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            movieName = movie.getTitle();
            titoloFilm.setText(movie.getTitle() + " (" + movie.getRelease_date().substring(0, 4) + ")");
            tramaFilm.setText(movie.getOverview());
            punteggioVoto.setText(String.format("%.1f", voto));
            locandinaFilm.setImageBitmap(ProfileFragment.getBitmapFromdownload(IMAGE_BASE_URL + movie.getPoster_path()));
            generelist = movie.getGenere();
            for (Genere genere : generelist) {
                chip = (Chip) inflater.inflate(R.layout.item_chip, null, false);
                chip.setText(genere.getNome());
                chipGroup.addView(chip);
            }
        }
    }

    @Override
    public void setDataCredits(String regista) {
        if(!regista.equals("")){
            registaText.setText(regista);
        }
    }

    @Override
    public void setDataLista(DetailedMovie movie) {

    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Toast.makeText(getActivity(), "Errore nel caricamento", Toast.LENGTH_SHORT).show();
    }
}