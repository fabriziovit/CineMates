package com.example.cinemates.ui.CineMates.views.fragments;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.presenters.fragments.InfoFilmPresenter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.varunest.sparkbutton.SparkButton;

public class InfoFilmFragment extends Fragment  {
    public int id;
    public ProgressBar progressBar;
    public Chip chip;
    public static String movieName;
    public TextView titoloFilm;
    public TextView tramaFilm;
    public TextView punteggioVoto;
    public ImageView locandinaFilm;
    public TextView registaText;
    public ChipGroup chipGroup;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    public String currUser;
    public SparkButton daVedereButton;
    public SparkButton preferitiButton;
    private InfoFilmPresenter infoFilmPresenter;

    public InfoFilmFragment() {
        // Required empty public constructor
    }

    public InfoFilmFragment(int id){
        this.id = id;
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
        preferitiButton = view.findViewById(R.id.aggiungiPreferiti_SparkButton_schedaFilm);
        daVedereButton = view.findViewById(R.id.davedere_sparkButton_schedaFilm);
        titoloFilm = view.findViewById(R.id.titoloFIlm_schedaFilm);
        tramaFilm = view.findViewById(R.id.trama_schedaFilm_textView);
        locandinaFilm = view.findViewById(R.id.locandinaFilm_schedaFilm);
        registaText = view.findViewById(R.id.nomeRegistaFilm_schedaFilm_textView);
        punteggioVoto = view.findViewById(R.id.percentualeVoto_schedaFilm_TextView);
        chipGroup = view.findViewById(R.id.genereFilm_schedaFilm_chipGroup);
        tramaFilm.setMovementMethod(new ScrollingMovementMethod());
        preferitiButton.setChecked(false);
        daVedereButton.setChecked(false);

        infoFilmPresenter = new InfoFilmPresenter(this, db);

        infoFilmPresenter.preferitiClick();
        infoFilmPresenter.daVedereClick();
        return view;
    }
}