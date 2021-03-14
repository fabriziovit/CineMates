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
import com.example.cinemates.ui.CineMates.friends.model.ItemFriend;
import com.example.cinemates.ui.CineMates.friends.model.ItemUser;
import com.example.cinemates.ui.CineMates.presenters.fragments.YourFriendsPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class YourFriendsFragment extends Fragment {
    public RecyclerView recyclerView_Amici;
    private List<ItemFriend> friendList;
    private List<ItemFriend> searchList;
    public ConstraintLayout constraintLayout;
    public EditText searchBar;
    public ImageView searchButton;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    public ViewPagerAdapter adapter;
    private List<ItemUser> userList;
    private YourFriendsPresenter yourFriendsPresenter;

    public YourFriendsFragment() {
        // Required empty public constructor
    }

    public YourFriendsFragment(List<ItemFriend> friendList, List<ItemUser> userList, ViewPagerAdapter adapter) {
        this.friendList = friendList;
        this.searchList = friendList;
        this.userList = userList;
        this.adapter = adapter;
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
        searchBar.setMovementMethod(null);
        yourFriendsPresenter = new YourFriendsPresenter(this, friendList, searchList, userList, db, firebaseAuth);

        yourFriendsPresenter.Keyboard();
        yourFriendsPresenter.cercaAmiciButton();
        yourFriendsPresenter.cercaAmiciByKeyboard();
        return view;
    }
}