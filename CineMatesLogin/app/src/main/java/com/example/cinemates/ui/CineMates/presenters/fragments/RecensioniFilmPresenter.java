package com.example.cinemates.ui.CineMates.presenters.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cinemates.ui.CineMates.adapter.RecycleViewAdapter_Recensioni;
import com.example.cinemates.ui.CineMates.model.DataFilmReviewed;
import com.example.cinemates.ui.CineMates.model.DataReviews;
import com.example.cinemates.ui.CineMates.model.ItemRecensione;
import com.example.cinemates.ui.CineMates.views.fragments.InfoFilmFragment;
import com.example.cinemates.ui.CineMates.views.fragments.ProfileFragment;
import com.example.cinemates.ui.CineMates.views.fragments.RecensioniFilmFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import Intefaces.UpdateableFragmentListener;

public class RecensioniFilmPresenter implements UpdateableFragmentListener, RecycleViewAdapter_Recensioni.OnClickListener{
    private final RecensioniFilmFragment recensioniFilmFragment;
    public int recensioni;
    public int filmRecensiti;
    private FirebaseFirestore db;

    public boolean presente;
    public boolean alreadyRecensito = false;
    public List<ItemRecensione> recensioniList;

    public RecensioniFilmPresenter(RecensioniFilmFragment recensioniFilmFragment, FirebaseFirestore db) {
        this.recensioniFilmFragment = recensioniFilmFragment;
        this.db = db;
        init();
    }

    private void init(){
        presente = false;
        recensioniList = new ArrayList<>();
        DocumentReference documentReference = db.collection("data").document("dataFilms");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    filmRecensiti = document.getLong("nFilmReviewed").intValue();
                }
            }
        });

        DocumentReference documentReference1 = db.collection("data").document("dataReviews");
        documentReference1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    recensioni = document.getLong("nDataReviews").intValue();
                }
            }
        });

        //Controllo recensioni
        new Thread(()-> {
            CollectionReference collectionReference1 = db.collection("users");
            collectionReference1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        DocumentReference documentReference = db.collection("reviews").document(String.valueOf(recensioniFilmFragment.id)).collection(String.valueOf(recensioniFilmFragment.id))
                                .document(documentSnapshot.getString("uid"));
                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Bitmap profilePic = ProfileFragment.getBitmapFromdownload(documentSnapshot.getString("imageUrl"));
                                        String username = documentSnapshot.getString("username");
                                        int voto = document.getLong("star").intValue();
                                        ItemRecensione recensione = new ItemRecensione(username, document.getString("review"), voto, profilePic, documentSnapshot.getId());
                                        if (recensioniFilmFragment.currUser.equals(documentSnapshot.getString("uid"))) {
                                            recensione.setProprietario(true);
                                            presente = true;
                                            recensioniList.add(0, recensione);
                                        } else
                                            recensioniList.add(recensione);
                                        recensioniFilmFragment.textViewRecensioneVuota.setVisibility(View.INVISIBLE);
                                        alreadyRecensito = true;
                                        RecycleViewAdapter_Recensioni recycleViewAdapterRecensioni = new RecycleViewAdapter_Recensioni(recensioniFilmFragment.getActivity(), recensioniList, RecensioniFilmPresenter.this);
                                        recensioniFilmFragment.recyclerView.setLayoutManager(new LinearLayoutManager(recensioniFilmFragment.getActivity()));
                                        recensioniFilmFragment.recyclerView.setAdapter(recycleViewAdapterRecensioni);
                                    }
                                    if(recensioniList.size() == 0)
                                        recensioniFilmFragment.textViewRecensioneVuota.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                }
            });
        }).start();
    }

    public void recensioneButton(){
        recensioniFilmFragment.recensioneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recensioniFilmFragment.showDialog();
            }
        });
    }

    @Override
    public void update() {
        recensioniFilmFragment.recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void OnClick(int position) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(recensioniFilmFragment.getActivity());
        builder1.setMessage("Vuoi davvero rimuovere la recensione al film "+ InfoFilmFragment.movieName +"?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int a) {
                        db.collection("reviews").document(String.valueOf(recensioniFilmFragment.id)).collection(String.valueOf(recensioniFilmFragment.id)).document(recensioniFilmFragment.currUser).delete();
                        recensioniList.remove(position);
                        presente = false;
                        recensioni = recensioni - 1;
                        DataReviews dataReviews = new DataReviews(recensioni);
                        db.collection("data").document("dataReviews").set(dataReviews);
                        if(recensioniList.size() == 0){
                            filmRecensiti = filmRecensiti - 1;
                            DataFilmReviewed dataFilmReviewed = new DataFilmReviewed(filmRecensiti);
                            db.collection("data").document("dataFilms").set(dataFilmReviewed);
                            alreadyRecensito = false;
                            recensioniFilmFragment.textViewRecensioneVuota.setVisibility(View.VISIBLE);
                        }
                        update();
                    }
                });
        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int a) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
