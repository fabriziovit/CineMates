package com.example.cinemates.ui.CineMates.Fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.adapter.RecycleViewAdapter_Recensioni;
import com.example.cinemates.ui.CineMates.model.ItemRecensione;
import com.example.cinemates.ui.CineMates.model.ReviewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class RecensioniFilmFragment extends Fragment implements RatingDialogListener, UpdateableFragmentListener, RecycleViewAdapter_Recensioni.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<ItemRecensione> recensioniList;
    private boolean presente;
    private Button recensioneButton;
    private int id;
    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String currUser;

    public RecensioniFilmFragment() {
        // Required empty public constructor
    }

    public RecensioniFilmFragment(int id) {
        this.id = id;
    }

    public static RecensioniFilmFragment newInstance(String param1, String param2) {
        RecensioniFilmFragment fragment = new RecensioniFilmFragment();
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
        View view = inflater.inflate(R.layout.fragment_recensioni_film, container, false);
        db = FirebaseFirestore.getInstance();
        presente = false;
        recensioneButton = view.findViewById(R.id.recensioneButton_RecensioniFilmFragment);
        recyclerView = view.findViewById(R.id.recycleView_recensioni_schedaFilm);
        recensioniList = new ArrayList<>();

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
                                        RecycleViewAdapter_Recensioni recycleViewAdapterRecensioni = new RecycleViewAdapter_Recensioni(getActivity(), recensioniList, RecensioniFilmFragment.this);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                        recyclerView.setAdapter(recycleViewAdapterRecensioni);
                                    }
                                }
                            }
                        });
                    }
                }
            });
        }).start();

        recensioneButton();
        return view;
    }

    public void recensioneButton(){
        recensioneButton.setOnClickListener(new View.OnClickListener() {
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
        if(presente){
            for(ItemRecensione itemRecensione : recensioniList){
                if(itemRecensione.getUid().equals(currUser)){
                    itemRecensione.setRecensione(s);
                    itemRecensione.setVoto(i);
                }
                RecycleViewAdapter_Recensioni recycleViewAdapterRecensioni = new RecycleViewAdapter_Recensioni(getActivity(), recensioniList, this);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(recycleViewAdapterRecensioni);
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
                        RecycleViewAdapter_Recensioni recycleViewAdapterRecensioni = new RecycleViewAdapter_Recensioni(getActivity(), recensioniList, RecensioniFilmFragment.this);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setAdapter(recycleViewAdapterRecensioni);
                    }
                }
            });
        }
    }

    @Override
    public void update() {
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void OnClick(int position) {
    }
}