package com.example.cinemates.ui.CineMates.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.adapter.RecycleViewAdapter_Recensioni;
import com.example.cinemates.ui.CineMates.model.DataFilmReviewed;
import com.example.cinemates.ui.CineMates.model.DataReviews;
import com.example.cinemates.ui.CineMates.model.ItemRecensione;
import com.example.cinemates.ui.CineMates.model.ReviewModel;
import com.example.cinemates.ui.CineMates.presenters.fragments.RecensioniFilmPresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shamanland.fab.FloatingActionButton;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

public class RecensioniFilmFragment extends Fragment implements RatingDialogListener {
    public int id;
    public RecyclerView recyclerView;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    public String currUser;
    public FloatingActionButton recensioneButton;
    public TextView textViewRecensioneVuota;
    private RecensioniFilmPresenter recensioniFilmPresenter;

    public RecensioniFilmFragment() {
        // Required empty public constructor
    }

    public RecensioniFilmFragment(int id) {
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
        View view = inflater.inflate(R.layout.fragment_recensioni_film, container, false);
        recensioneButton = view.findViewById(R.id.recensioneButton_RecensioniFilmFragment);
        recyclerView = view.findViewById(R.id.recycleView_recensioni_schedaFilm);
        textViewRecensioneVuota = view.findViewById(R.id.textView_RecensioniVuoti_RecensioniFilm);
        recensioniFilmPresenter = new RecensioniFilmPresenter(this, db);

        recensioniFilmPresenter.recensioneButton();
        return view;
    }

    public void showDialog(){
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Invia")
                .setNegativeButtonText("Cancella")
                .setNumberOfStars(5)
                .setDefaultRating(1)
                .setTitle("Recensisci "+ InfoFilmFragment.movieName)
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
                .create(getActivity())
                .setTargetFragment(this, 1)
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
        textViewRecensioneVuota.setVisibility(View.INVISIBLE);
        if(recensioniFilmPresenter.presente){
            for(ItemRecensione itemRecensione : recensioniFilmPresenter.recensioniList){
                if(itemRecensione.getUid().equals(currUser)){
                    itemRecensione.setRecensione(s);
                    itemRecensione.setVoto(i);
                }
                RecycleViewAdapter_Recensioni recycleViewAdapterRecensioni = new RecycleViewAdapter_Recensioni(getActivity(), recensioniFilmPresenter.recensioniList, recensioniFilmPresenter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(recycleViewAdapterRecensioni);
            }
        }else{
            DocumentReference documentReference = db.collection("users").document(currUser);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        ItemRecensione recensione = new ItemRecensione(documentSnapshot.getString("username"), s, i, ProfileFragment.getBitmapFromdownload(documentSnapshot.getString("imageUrl")), documentSnapshot.getString("uid"));
                        recensione.setProprietario(true);
                        recensioniFilmPresenter.recensioniList.add(0, recensione);
                        recensioniFilmPresenter.presente = true;
                        RecycleViewAdapter_Recensioni recycleViewAdapterRecensioni = new RecycleViewAdapter_Recensioni(getActivity(), recensioniFilmPresenter.recensioniList, recensioniFilmPresenter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setAdapter(recycleViewAdapterRecensioni);
                        recensioniFilmPresenter.recensioni++;
                        DataReviews dataReviews = new DataReviews(recensioniFilmPresenter.recensioni);
                        db.collection("data").document("dataReviews").set(dataReviews);
                        if(!recensioniFilmPresenter.alreadyRecensito) {
                            recensioniFilmPresenter.filmRecensiti++;
                            DataFilmReviewed dataFilmReviewed = new DataFilmReviewed(recensioniFilmPresenter.filmRecensiti);
                            db.collection("data").document("dataFilms").set(dataFilmReviewed);
                        }
                    }
                }
            });
        }
    }

}