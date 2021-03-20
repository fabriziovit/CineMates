package com.example.cinemates.ui.CineMates.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.friends.model.ItemUser;
import com.example.cinemates.ui.CineMates.presenters.fragments.SearchFriendsPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import Intefaces.UpdateableFragmentListener;

public class SearchFriendsFragment extends Fragment implements UpdateableFragmentListener {
    public RecyclerView recyclerView_Utenti;
    private List<ItemUser> userList;
    private List<ItemUser> searchList;
    public ConstraintLayout constraintLayout;
    public EditText searchBar;
    public ImageView searchButton;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    public SearchFriendsPresenter searchFriendsPresenter;

    public SearchFriendsFragment() {
        // Required empty public constructor
    }

    public SearchFriendsFragment(List<ItemUser> userList){
        this.userList = userList;
        this.searchList = userList;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_friends, container, false);
        searchBar = view.findViewById(R.id.searchBar_fragment_SearchFriends);
        searchButton = view.findViewById(R.id.searchButton_fragment_SearchFriends);
        constraintLayout = view.findViewById(R.id.container_fragment_searchfriends);
        recyclerView_Utenti = view.findViewById(R.id.recycleView_fragment_SearchFriends);
        searchBar.setMovementMethod(null);
        searchFriendsPresenter = new SearchFriendsPresenter(this, userList, searchList, db, mAuth);

        searchFriendsPresenter.Keyboard();
        searchFriendsPresenter.cercaUtentiButton();
        searchFriendsPresenter.cercaUtentiByKeyboard();
        return view;
    }

    @Override
    public void update() {
        recyclerView_Utenti.getAdapter().notifyDataSetChanged();
    }
}