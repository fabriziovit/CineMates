package com.example.cinemates.ui.CineMates.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.VisualizzaPreferitiActivity;
import com.example.cinemates.ui.CineMates.friends.ItemFriend;
import com.example.cinemates.ui.CineMates.friends.RecycleViewAdapter_Amico;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import Intefaces.UpdateableFragmentListener;

public class YourFriendsFragment extends Fragment implements RecycleViewAdapter_Amico.OnClickListener, UpdateableFragmentListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private List<ItemFriend> friendList;
    private ConstraintLayout constraintLayout;
    private EditText searchBar;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;

    private String mParam1;
    private String mParam2;

    public YourFriendsFragment() {
        // Required empty public constructor
    }

    public YourFriendsFragment(List<ItemFriend> friendList) {
        this.friendList = friendList;
    }

    public static YourFriendsFragment newInstance(String param1, String param2) {
        YourFriendsFragment fragment = new YourFriendsFragment();
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
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_your_friends, container, false);

        constraintLayout = view.findViewById(R.id.container_frament_yourfriends);
        recyclerView = view.findViewById(R.id.recycleView_fragment_YourFriends);
        searchBar = view.findViewById(R.id.searchBar_fragment_YourFriends);
        RecycleViewAdapter_Amico recycleViewAdapterAmico = new RecycleViewAdapter_Amico(getContext(), friendList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recycleViewAdapterAmico);

        Keyboard();
        return view;
    }

    private void Keyboard() {
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(constraintLayout.getWindowToken(), 0);
                searchBar.clearFocus();
            }
        });
    }

    @Override
    public void onClickPreferiti(int position) {
        //friendList.get(position);
        //visualizza preferiti passandogli l'username della position
        //da fixare con i relativi film
        startActivity(new Intent(getActivity(), VisualizzaPreferitiActivity.class));
    }

    @Override
    public void onClickRimuoviAmico(int position){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Vuoi davvero rimuovere "+friendList.get(position).getUsername()+" dagli amici?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        rimuoviAmico(position);
                        friendList.remove(position);
                        update();
                        Toast.makeText(getActivity(), "Amico rimosso!", Toast.LENGTH_SHORT).show();
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

    public void rimuoviAmico(int position){
        String username = friendList.get(position).getUsername();
        CollectionReference collectionReference = db.collection("users");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (username.equals(documentSnapshot.getString("username"))) {
                        String uIdDestinatario = documentSnapshot.getString("uid");
                        String uidUser = firebaseAuth.getCurrentUser().getUid();
                        db.collection("friends").document(uIdDestinatario).collection(uidUser).document(uidUser).delete();
                        db.collection("friends").document(uidUser).collection(uIdDestinatario).document(uIdDestinatario).delete();
                    }
                    break;
                }
            }
        });
    }

    @Override
    public void update() {
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}