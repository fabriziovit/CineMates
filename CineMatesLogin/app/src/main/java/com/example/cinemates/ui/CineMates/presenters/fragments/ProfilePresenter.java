package com.example.cinemates.ui.CineMates.presenters.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.cinemates.ui.CineMates.ApiMovie.Contract.MovieDetailsContract;
import com.example.cinemates.ui.CineMates.ApiMovie.Presenter.MovieDetailsPresenter;
import com.example.cinemates.ui.CineMates.ApiMovie.model.DetailedMovie;
import com.example.cinemates.ui.CineMates.model.ItemFilm;
import com.example.cinemates.ui.CineMates.presenters.activities.ListeUtentePresenter;
import com.example.cinemates.ui.CineMates.views.activities.CredenzialiProfiloActivity;
import com.example.cinemates.ui.CineMates.views.activities.ListeUtenteActivity;
import com.example.cinemates.ui.CineMates.views.activities.LoginActivity;
import com.example.cinemates.ui.CineMates.views.fragments.ProfileFragment;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import static com.example.cinemates.ui.CineMates.ApiMovie.ApiClient.IMAGE_BASE_URL;

public class ProfilePresenter implements MovieDetailsContract.View{
    private final ProfileFragment profileFragment;
    private MovieDetailsPresenter movieDetailsPresenter;
    private List<ItemFilm> filmPreferiti;
    private List<ItemFilm> filmDavedere;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private FirebaseStorage storage;
    private FirebaseFirestore db;

    public ProfilePresenter(ProfileFragment profileFragment, FirebaseFirestore db, FirebaseStorage storage) {
        this.profileFragment = profileFragment;
        this.db = db;
        this.storage = storage;
        init();
    }

    private void init(){
        filmPreferiti = new ArrayList<>();
        filmDavedere = new ArrayList<>();
        movieDetailsPresenter = new MovieDetailsPresenter(this);

        new Thread(() -> {
            CollectionReference collectionReference = db.collection("favorites").document(profileFragment.curUser).collection(profileFragment.curUser);
            collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        movieDetailsPresenter.requestMovieData(documentSnapshot.getLong("idFilm").intValue(), 1);
                    }
                }
            });
        }).start();

        new Thread(() -> {
            CollectionReference collectionReference = db.collection("da vedere").document(profileFragment.curUser).collection(profileFragment.curUser);
            collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        movieDetailsPresenter.requestMovieData(documentSnapshot.getLong("idFilm").intValue(), 2);
                    }
                }
            });
        }).start();
    }

    public void logout(Button logoutbtn){
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(profileFragment.getActivity());
                builder1.setMessage("Vuoi davvero uscire?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                accetta();
                            }
                        });
                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
    }

    public void modificaCredenzialiBtn(Button modificaBtn){
        modificaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profileFragment.getActivity(), CredenzialiProfiloActivity.class);
                profileFragment.startActivity(intent);
            }
        });
    }

    public void VisualizzaPreferiti(){
        profileFragment.visualizzaPreferiti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(profileFragment.getActivity(), ListeUtenteActivity.class);
                ListeUtentePresenter.filmDaV = filmDavedere;
                ListeUtentePresenter.filmPre = filmPreferiti;
                profileFragment.startActivity(i);
            }
        });
    }

    public void ModificaImmagine(){
        profileFragment.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(profileFragment.getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED){
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        profileFragment.requestPermissions(permissions, PERMISSION_CODE);
                    }else{
                        pickImageFromGallery();
                    }
                }else{
                    pickImageFromGallery();
                }
            }
        });
    }

    public void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        profileFragment.startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    public void accetta() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        Toast.makeText(profileFragment.getActivity(), "Logout effettuato!", Toast.LENGTH_SHORT).show();
        profileFragment.startActivity(new Intent(profileFragment.getActivity(), LoginActivity.class));
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setDataToViews(DetailedMovie movie) {
        if (movie != null) {
            filmPreferiti.add(new ItemFilm(movie.getTitle(), ProfileFragment.getBitmapFromdownload(IMAGE_BASE_URL + movie.getPoster_path()), movie.getId()));
        }
    }


    @Override
    public void setDataCredits(String regista) {

    }

    @Override
    public void setDataLista(DetailedMovie movie) {
        if (movie != null) {
            filmDavedere.add(new ItemFilm(movie.getTitle(), ProfileFragment.getBitmapFromdownload(IMAGE_BASE_URL + movie.getPoster_path()), movie.getId()));
        }
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Toast.makeText(profileFragment.getActivity(), "Errore nel caricamento", Toast.LENGTH_SHORT).show();
    }
}
