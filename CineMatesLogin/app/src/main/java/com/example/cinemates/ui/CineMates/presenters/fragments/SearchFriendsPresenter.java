package com.example.cinemates.ui.CineMates.presenters.fragments;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cinemates.ui.CineMates.adapter.RecycleViewAdapter_Utenti;
import com.example.cinemates.ui.CineMates.friends.model.FriendRequest;
import com.example.cinemates.ui.CineMates.friends.model.ItemUser;
import com.example.cinemates.ui.CineMates.views.fragments.SearchFriendsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import Intefaces.UpdateableFragmentListener;

public class SearchFriendsPresenter implements RecycleViewAdapter_Utenti.OnClickListener, UpdateableFragmentListener {
    private final SearchFriendsFragment searchFriendsFragment;
    private List<ItemUser> userList;
    private List<ItemUser> searchList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public SearchFriendsPresenter(SearchFriendsFragment searchFriendsFragment, List<ItemUser> userList, List<ItemUser> searchList, FirebaseFirestore db, FirebaseAuth mAuth ) {
        this.searchFriendsFragment = searchFriendsFragment;
        this.searchList = searchList;
        this.userList = userList;
        this.db = db;
        this.mAuth = mAuth;
        init();
    }

    private void init(){
        RecycleViewAdapter_Utenti recycleViewAdapter = new RecycleViewAdapter_Utenti(searchFriendsFragment.getContext(), searchList, this);
        searchFriendsFragment.recyclerView_Utenti.setLayoutManager(new LinearLayoutManager(searchFriendsFragment.getActivity()));
        searchFriendsFragment.recyclerView_Utenti.setAdapter(recycleViewAdapter);
    }


    public void Keyboard() {
        searchFriendsFragment.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) searchFriendsFragment.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchFriendsFragment.constraintLayout.getWindowToken(), 0);
                searchFriendsFragment.searchBar.clearFocus();
            }
        });
    }

    @Override
    public void OnClick(int position) {
        if(searchList.get(position).getRapporto() == 1){
            Toast.makeText(searchFriendsFragment.getActivity(), "Richiesta già inviata!", Toast.LENGTH_SHORT).show();
        }else if(searchList.get(position).getRapporto() == 2) {
            Toast.makeText(searchFriendsFragment.getActivity(), "Siete già amici!", Toast.LENGTH_SHORT).show();
        }else if(searchList.get(position).getRapporto() == 3){
            Toast.makeText(searchFriendsFragment.getActivity(), "Richiesta già presente, vai ad accettarla!", Toast.LENGTH_SHORT).show();
        }else{
            String uIdDestinatario = searchList.get(position).getUid();
            String uIdMittente = mAuth.getCurrentUser().getUid();
            FieldValue timestamp = FieldValue.serverTimestamp();
            FriendRequest friendRequest = new FriendRequest(uIdDestinatario, uIdMittente, timestamp);
            DocumentReference documentReference = db.collection("friend request").document(uIdMittente).collection(uIdDestinatario).document(uIdDestinatario);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    db.collection("friend request").document(uIdMittente).collection(uIdDestinatario).document(uIdDestinatario).set(friendRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(searchFriendsFragment.getActivity(), "Richiesta inviata correttamente!", Toast.LENGTH_SHORT).show();
                            searchList.get(position).setRapporto(1);
                            String username = searchList.get(position).getUsername();
                            for(ItemUser itemUser : userList){
                                if(username.equals(itemUser.getUsername()))
                                    itemUser.setRapporto(1);
                            }
                            update();
                        }
                    });
                }
            });
        }
    }

    public void cercaUtentiButton(){
        searchFriendsFragment.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cercaUtenti();
            }
        });
    }

    public void cercaUtentiByKeyboard(){
        searchFriendsFragment.searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    cercaUtenti();
                }
                return false;
            }
        });
    }

    private void cercaUtenti(){
        InputMethodManager imm = (InputMethodManager) searchFriendsFragment.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchFriendsFragment.constraintLayout.getWindowToken(), 0);
        searchFriendsFragment.searchBar.clearFocus();
        searchList = new ArrayList<>();
        String ricerca = searchFriendsFragment.searchBar.getText().toString().toLowerCase();
        if(ricerca.length() != 0) {
            for (int i = 0; i < userList.size(); i++) {
                String username = userList.get(i).getUsername().toLowerCase();
                if (username.contains(ricerca)) {
                    ItemUser userSearch = new ItemUser(userList.get(i).getUsername(), userList.get(i).getBitmap(), userList.get(i).getUid(), userList.get(i).getRapporto());
                    searchList.add(userSearch);
                }
            }
            if(searchList.size() != 0) {
                RecycleViewAdapter_Utenti recycleViewAdapter = new RecycleViewAdapter_Utenti(searchFriendsFragment.getContext(), searchList, this);
                searchFriendsFragment.recyclerView_Utenti.setLayoutManager(new LinearLayoutManager(searchFriendsFragment.getActivity()));
                searchFriendsFragment.recyclerView_Utenti.setAdapter(recycleViewAdapter);
                update();
            }else{
                RecycleViewAdapter_Utenti recycleViewAdapter = new RecycleViewAdapter_Utenti(searchFriendsFragment.getContext(), searchList, this);
                searchFriendsFragment.recyclerView_Utenti.setLayoutManager(new LinearLayoutManager(searchFriendsFragment.getActivity()));
                searchFriendsFragment.recyclerView_Utenti.setAdapter(recycleViewAdapter);
                update();
                Toast.makeText(searchFriendsFragment.getContext(), "Nessun amico trovato con quel nome!", Toast.LENGTH_SHORT).show();
            }
        }else {
            searchList = userList;
            RecycleViewAdapter_Utenti recycleViewAdapter = new RecycleViewAdapter_Utenti(searchFriendsFragment.getContext(), searchList, this);
            searchFriendsFragment.recyclerView_Utenti.setLayoutManager(new LinearLayoutManager(searchFriendsFragment.getActivity()));
            searchFriendsFragment.recyclerView_Utenti.setAdapter(recycleViewAdapter);
            update();
            Toast.makeText(searchFriendsFragment.getContext(), "Nessun parametro di ricerca inserito!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void update() {
        searchFriendsFragment.recyclerView_Utenti.getAdapter().notifyDataSetChanged();
    }
}
