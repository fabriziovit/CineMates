package com.example.cinemates.ui.CineMates.presenters.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemates.ui.CineMates.adapter.RecycleViewAdapter_Amici;
import com.example.cinemates.ui.CineMates.friends.model.ItemFriend;
import com.example.cinemates.ui.CineMates.friends.model.ItemUser;
import com.example.cinemates.ui.CineMates.views.activities.VisualizzaPreferitiActivity;
import com.example.cinemates.ui.CineMates.views.fragments.YourFriendsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import Intefaces.UpdateableFragmentListener;

public class YourFriendsPresenter implements RecycleViewAdapter_Amici.OnClickListener, UpdateableFragmentListener {
    private final YourFriendsFragment yourFriendsFragment;
    private List<ItemFriend> friendList;
    private List<ItemFriend> searchList;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private List<ItemUser> userList;
    private RecyclerView recyclerView_Amici;

    public YourFriendsPresenter(YourFriendsFragment yourFriendsFragment, List<ItemFriend> friendList, List<ItemFriend> searchList, List<ItemUser> userList, FirebaseFirestore db, FirebaseAuth firebaseAuth, RecyclerView recyclerView_Amici) {
        this.yourFriendsFragment = yourFriendsFragment;
        this.friendList = friendList;
        this.searchList = searchList;
        this.userList = userList;
        this.db = db;
        this.firebaseAuth = firebaseAuth;
        this.recyclerView_Amici = recyclerView_Amici;
        init();
    }

    private void init(){
        RecycleViewAdapter_Amici recycleViewAdapterAmici = new RecycleViewAdapter_Amici(yourFriendsFragment.getContext(), searchList, this);
        recyclerView_Amici.setLayoutManager(new LinearLayoutManager(yourFriendsFragment.getActivity()));
        recyclerView_Amici.setAdapter(recycleViewAdapterAmici);
    }

    public void Keyboard() {
        yourFriendsFragment.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) yourFriendsFragment.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(yourFriendsFragment.constraintLayout.getWindowToken(), 0);
                yourFriendsFragment.searchBar.clearFocus();
            }
        });
    }

    @Override
    public void onClickPreferiti(int position) {
        Intent i = new Intent(yourFriendsFragment.getActivity(), VisualizzaPreferitiActivity.class);
        i.putExtra("username", searchList.get(position).getUsername());
        yourFriendsFragment.startActivity(i);
    }

    @Override
    public void onClickRimuoviAmico(int position){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(yourFriendsFragment.getActivity());
        builder1.setMessage("Vuoi davvero rimuovere "+searchList.get(position).getUsername()+" dagli amici?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        for(ItemUser itemUser: userList){
                            if(searchList.get(position).getUid().equals(itemUser.getUid()))
                                itemUser.setRapporto(0);
                        }
                        rimuoviAmico(position);
                        searchList.remove(position);
                        update();
                        yourFriendsFragment.adapter.update();
                        Toast.makeText(yourFriendsFragment.getActivity(), "Amico rimosso!", Toast.LENGTH_SHORT).show();
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
        String uIdDestinatario = searchList.get(position).getUid();
        String uidUserAuth = firebaseAuth.getCurrentUser().getUid();
        db.collection("friends").document(uIdDestinatario).collection(uidUserAuth).document(uidUserAuth).delete();
        db.collection("friends").document(uidUserAuth).collection(uIdDestinatario).document(uIdDestinatario).delete();
    }

    public void cercaAmiciButton(){
        yourFriendsFragment.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cercaAmici();
            }
        });
    }

    private void cercaAmici(){
        searchList = new ArrayList<>();
        InputMethodManager imm = (InputMethodManager) yourFriendsFragment.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(yourFriendsFragment.constraintLayout.getWindowToken(), 0);
        yourFriendsFragment.searchBar.clearFocus();
        String ricerca = yourFriendsFragment.searchBar.getText().toString().toLowerCase();
        if(ricerca.length() != 0) {
            for (int i = 0; i < friendList.size(); i++) {
                String username = friendList.get(i).getUsername().toLowerCase();
                if (username.contains(ricerca)) {
                    ItemFriend newFriend = new ItemFriend(friendList.get(i).getUsername(), friendList.get(i).getBitmap(), friendList.get(i).getUid());
                    searchList.add(newFriend);
                }
            }
            if(searchList.size() != 0) {
                RecycleViewAdapter_Amici recycleViewAdapter_amici = new RecycleViewAdapter_Amici(yourFriendsFragment.getContext(), searchList, this);
                recyclerView_Amici.setLayoutManager(new LinearLayoutManager(yourFriendsFragment.getActivity()));
                recyclerView_Amici.setAdapter(recycleViewAdapter_amici);
                update();
            }else{
                RecycleViewAdapter_Amici recycleViewAdapter_amici = new RecycleViewAdapter_Amici(yourFriendsFragment.getContext(), searchList, this);
                recyclerView_Amici.setLayoutManager(new LinearLayoutManager(yourFriendsFragment.getActivity()));
                recyclerView_Amici.setAdapter(recycleViewAdapter_amici);
                update();
                Toast.makeText(yourFriendsFragment.getContext(), "Nessun amico trovato con quel nome!", Toast.LENGTH_SHORT).show();
            }
        }else {
            searchList = friendList;
            RecycleViewAdapter_Amici recycleViewAdapter_amici = new RecycleViewAdapter_Amici(yourFriendsFragment.getContext(), searchList, this);
            recyclerView_Amici.setLayoutManager(new LinearLayoutManager(yourFriendsFragment.getActivity()));
            recyclerView_Amici.setAdapter(recycleViewAdapter_amici);
            update();
            Toast.makeText(yourFriendsFragment.getContext(), "Nessun parametro di ricerca inserito!", Toast.LENGTH_SHORT).show();
        }
    }

    public void cercaAmiciByKeyboard(){
        yourFriendsFragment.searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    cercaAmici();
                }
                return false;
            }
        });
    }

    @Override
    public void update() {
        recyclerView_Amici.getAdapter().notifyDataSetChanged();
    }
}
