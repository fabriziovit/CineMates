package com.example.cinemates.ui.CineMates.presenters.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.ApiMovie.Contract.MovieDetailsContract;
import com.example.cinemates.ui.CineMates.ApiMovie.Presenter.MovieDetailsPresenter;
import com.example.cinemates.ui.CineMates.ApiMovie.model.DetailedMovie;
import com.example.cinemates.ui.CineMates.ApiMovie.model.Genere;
import com.example.cinemates.ui.CineMates.model.PreferitiModel;
import com.example.cinemates.ui.CineMates.views.fragments.InfoFilmFragment;
import com.example.cinemates.ui.CineMates.views.fragments.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.varunest.sparkbutton.SparkEventListener;

import java.util.ArrayList;

import static com.example.cinemates.ui.CineMates.ApiMovie.ApiClient.IMAGE_BASE_URL;

public class InfoFilmPresenter implements MovieDetailsContract.View{
    private final InfoFilmFragment infoFilmFragment;
    private FirebaseFirestore db;
    private MovieDetailsPresenter movieDetailsPresenter;
    private ArrayList<Genere> generelist;

    public InfoFilmPresenter(InfoFilmFragment infoFilmFragment, FirebaseFirestore db) {
        this.infoFilmFragment = infoFilmFragment;
        this.db = db;
        init();
    }

    private void init(){
        movieDetailsPresenter = new MovieDetailsPresenter(this);
        movieDetailsPresenter.requestMovieData(infoFilmFragment.id);

        //controllo lista da vedere
        new Thread(()-> {
            DocumentReference documentReference = db.collection("da vedere").document(infoFilmFragment.currUser).collection(infoFilmFragment.currUser).document(String.valueOf(infoFilmFragment.id));
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            infoFilmFragment.daVedereButton.setChecked(true);
                        }
                    }
                }
            });
        }).start();

        //Controllo lista preferiti
        new Thread(()-> {
            DocumentReference documentReference = db.collection("favorites").document(infoFilmFragment.currUser).collection(infoFilmFragment.currUser).document(String.valueOf(infoFilmFragment.id));
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            infoFilmFragment.preferitiButton.setChecked(true);
                        }
                    }
                }
            });
        }).start();
    }

    public void preferitiClick(){
        infoFilmFragment.preferitiButton.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {

                //Controllo lista preferiti
                new Thread(()-> {
                    DocumentReference documentReference = db.collection("favorites").document(infoFilmFragment.currUser).collection(infoFilmFragment.currUser).document(String.valueOf(infoFilmFragment.id));
                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if(document.exists()){
                                    infoFilmFragment.preferitiButton.setChecked(true);
                                }else {
                                    infoFilmFragment.preferitiButton.setChecked(false);
                                }
                            }
                        }
                    });
                }).start();
                if(buttonState){
                    PreferitiModel preferitiModel = new PreferitiModel(infoFilmFragment.currUser, infoFilmFragment.id);
                    db.collection("favorites").document(infoFilmFragment.currUser).collection(infoFilmFragment.currUser).document(String.valueOf(infoFilmFragment.id)).set(preferitiModel);
                    infoFilmFragment.preferitiButton.setChecked(true);
                }else{
                    db.collection("favorites").document(infoFilmFragment.currUser).collection(infoFilmFragment.currUser).document(String.valueOf(infoFilmFragment.id)).delete();
                    infoFilmFragment.preferitiButton.setChecked(false);
                }
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });
    }

    public void daVedereClick(){
        infoFilmFragment.daVedereButton.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {

                //controllo la lista da vedere
                new Thread(()-> {
                    DocumentReference documentReference = db.collection("da vedere").document(infoFilmFragment.currUser).collection(infoFilmFragment.currUser).document(String.valueOf(infoFilmFragment.id));
                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if(document.exists()){
                                    infoFilmFragment.daVedereButton.setChecked(true);
                                }else {
                                    infoFilmFragment.daVedereButton.setChecked(false);
                                }
                            }
                        }
                    });
                }).start();
                if(buttonState){
                    PreferitiModel preferitiModel = new PreferitiModel(infoFilmFragment.currUser, infoFilmFragment.id);
                    db.collection("da vedere").document(infoFilmFragment.currUser).collection(infoFilmFragment.currUser).document(String.valueOf(infoFilmFragment.id)).set(preferitiModel);
                    infoFilmFragment.daVedereButton.setChecked(true);
                }else{
                    db.collection("da vedere").document(infoFilmFragment.currUser).collection(infoFilmFragment.currUser).document(String.valueOf(infoFilmFragment.id)).delete();
                    infoFilmFragment.daVedereButton.setChecked(false);
                }
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });
    }

    @Override
    public void showProgress() {
        infoFilmFragment.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        infoFilmFragment.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setDataToViews(DetailedMovie movie) {
        if (movie != null) {
            float voto = movie.getVote_average() / 2;
            LayoutInflater inflater = LayoutInflater.from(infoFilmFragment.getActivity());
            infoFilmFragment.movieName = movie.getTitle();
            infoFilmFragment.titoloFilm.setText(movie.getTitle() + " (" + movie.getRelease_date().substring(0, 4) + ")");
            infoFilmFragment.tramaFilm.setText(movie.getOverview());
            infoFilmFragment.punteggioVoto.setText(String.format("%.1f", voto));
            infoFilmFragment.locandinaFilm.setImageBitmap(ProfileFragment.getBitmapFromdownload(IMAGE_BASE_URL + movie.getPoster_path()));
            generelist = movie.getGenere();
            for (Genere genere : generelist) {
                infoFilmFragment.chip = (Chip) inflater.inflate(R.layout.item_chip, null, false);
                infoFilmFragment.chip.setText(genere.getNome());
                infoFilmFragment.chipGroup.addView(infoFilmFragment.chip);
            }
        }
    }

    @Override
    public void setDataCredits(String regista) {
        if(!regista.equals("")){
            infoFilmFragment.registaText.setText(regista);
        }
    }

    @Override
    public void setDataLista(DetailedMovie movie) {

    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Toast.makeText(infoFilmFragment.getActivity(), "Errore nel caricamento", Toast.LENGTH_SHORT).show();
    }
}
