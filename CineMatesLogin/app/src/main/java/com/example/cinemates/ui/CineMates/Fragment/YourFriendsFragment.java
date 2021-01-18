package com.example.cinemates.ui.CineMates.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.friends.ItemFriend;
import com.example.cinemates.ui.CineMates.friends.RecycleViewAdapter_Amico;

import java.util.List;

import Intefaces.UpdateableFragmentListener;

public class YourFriendsFragment extends Fragment implements RecycleViewAdapter_Amico.OnClickListener, UpdateableFragmentListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private List<ItemFriend> friendList;
    private ConstraintLayout constraintLayout;
    private EditText searchBar;

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
    public void OnClick(int position) {
        friendList.get(position);
        //Aggiungere azione
    }

    @Override
    public void update() {
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}