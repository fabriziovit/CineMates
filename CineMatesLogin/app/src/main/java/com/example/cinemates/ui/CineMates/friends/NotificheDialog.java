package com.example.cinemates.ui.CineMates.friends;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.Fragment.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificheDialog extends DialogFragment implements RecycleViewAdapter_Richieste.OnClickListener {

    Activity activity;
    private List<ItemRichieste> richiesteList;
    ImageView chiudiFinestra;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String currUser;

    public NotificheDialog(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.notifiche_dialog, container);
        richiesteList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currUser = auth.getCurrentUser().getUid();

        new Thread(()-> {
            CollectionReference collectionReference = db.collection("users");
            collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        DocumentReference documentReference = db.collection("friend request").document(documentSnapshot.getString("uid")).collection(currUser).document(currUser);
                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        richiesteList.add(new ItemRichieste(documentSnapshot.getString("username"), ProfileFragment.getBitmapFromdownload(documentSnapshot.getString("imageUrl"))));
                                        //Mostrare pallino notifica nella schermata friends fragment
                                    }
                                }
                            }
                        });
                    }
                }
            });
            while(richiesteList.size() == 0){}
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RecycleViewAdapter_Richieste recycleViewAdapterRichieste = new RecycleViewAdapter_Richieste(getActivity(), richiesteList, NotificheDialog.this);
                    RecyclerView recyclerView = view.findViewById(R.id.Richieste_Dialog);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(recycleViewAdapterRichieste);
                }
            });
        }).start();
        chiudiFinestra = view.findViewById(R.id.chiudi_finestra_notificheDialog);
        chiudi();
        return view;
    }

    public NotificheDialog(Activity myActivity){
        activity = myActivity;
    }

    @Override
    public void onClickAccetta(int position) {
        CollectionReference collectionReference = db.collection("users");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                    if(richiesteList.get(position).getUsername().equals(queryDocumentSnapshot.getString("username"))){
                        String uIdMittente = queryDocumentSnapshot.getString("uid");
                        db.collection("friend request").document(uIdMittente).collection(currUser).document(currUser).delete();
                        //aggiornamento UI dialog notifiche
                        FieldValue timestamp = FieldValue.serverTimestamp();
                        Friends friends1 = new Friends(uIdMittente, timestamp);
                        Friends friends2 = new Friends(currUser, timestamp);
                        db.collection("friends").document(currUser).collection(uIdMittente).document(uIdMittente).set(friends1);
                        db.collection("friends").document(uIdMittente).collection(currUser).document(currUser).set(friends2);
                        Toast.makeText(getActivity(), "Richiesta accettata!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onClickRifiuta(int position){
        CollectionReference collectionReference = db.collection("users");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                    if(richiesteList.get(position).getUsername().equals(queryDocumentSnapshot.getString("username"))){
                        String uIdMittente = queryDocumentSnapshot.getString("uid");
                        db.collection("friend request").document(uIdMittente).collection(currUser).document(currUser).delete();
                        //aggiornamento UI dialog notifiche
                        Toast.makeText(getActivity(), "Richiesta cancellata!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void chiudi(){
        chiudiFinestra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
    }
}
