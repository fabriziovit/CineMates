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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.VisualizzaPreferitiActivity;
import com.example.cinemates.ui.CineMates.friends.ItemFriend;
import com.example.cinemates.ui.CineMates.friends.RecycleViewAdapter_Amici;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import Intefaces.UpdateableFragmentListener;

public class YourFriendsFragment extends Fragment implements RecycleViewAdapter_Amici.OnClickListener, UpdateableFragmentListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView_Amici;
    private List<ItemFriend> friendList;
    private ConstraintLayout constraintLayout;
    private EditText searchBar;
    private ImageView searchButton;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;

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

        constraintLayout = view.findViewById(R.id.container_fragment_yourfriends);
        recyclerView_Amici = view.findViewById(R.id.recycleView_fragment_YourFriends);
        searchBar = view.findViewById(R.id.searchBar_fragment_YourFriends);
        searchButton = view.findViewById(R.id.searchButton_fragment_YourFriends);
        RecycleViewAdapter_Amici recycleViewAdapterAmici = new RecycleViewAdapter_Amici(getContext(), friendList, this);
        recyclerView_Amici.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_Amici.setAdapter(recycleViewAdapterAmici);

        Keyboard();
        cercaAmici();
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
        String uIdDestinatario = friendList.get(position).getUid();
        String uidUserAuth = firebaseAuth.getCurrentUser().getUid();
        db.collection("friends").document(uIdDestinatario).collection(uidUserAuth).document(uidUserAuth).delete();
        db.collection("friends").document(uidUserAuth).collection(uIdDestinatario).document(uIdDestinatario).delete();
    }

    public void cercaAmici(){
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(constraintLayout.getWindowToken(), 0);
                searchBar.clearFocus();
                List<ItemFriend> searchList = new ArrayList<>();
                String ricerca = searchBar.getText().toString().toLowerCase();
                if(ricerca.length() != 0) {
                    for (int i = 0; i < friendList.size(); i++) {
                        String username = friendList.get(i).getUsername().toLowerCase();
                        if (username.contains(ricerca)) {
                            ItemFriend newFriend = new ItemFriend(friendList.get(i).getUsername(), friendList.get(i).getBitmap());
                            newFriend.setUid(friendList.get(i).getUid());
                            searchList.add(newFriend);
                        }
                    }
                    if(searchList.size() != 0) {
                        RecycleViewAdapter_Amici recycleViewAdapter_amici = new RecycleViewAdapter_Amici(getContext(), searchList, YourFriendsFragment.this);
                        recyclerView_Amici.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView_Amici.setAdapter(recycleViewAdapter_amici);
                        update();
                    }else{
                        RecycleViewAdapter_Amici recycleViewAdapter_amici = new RecycleViewAdapter_Amici(getContext(), searchList, YourFriendsFragment.this);
                        recyclerView_Amici.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView_Amici.setAdapter(recycleViewAdapter_amici);
                        update();
                        Toast.makeText(getContext(), "Nessun amico trovato con quel nome!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    RecycleViewAdapter_Amici recycleViewAdapter_amici = new RecycleViewAdapter_Amici(getContext(), friendList, YourFriendsFragment.this);
                    recyclerView_Amici.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView_Amici.setAdapter(recycleViewAdapter_amici);
                    update();
                    Toast.makeText(getContext(), "Nessun paramtrro di ricerca inserito!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void update() {
        recyclerView_Amici.getAdapter().notifyDataSetChanged();
    }
}