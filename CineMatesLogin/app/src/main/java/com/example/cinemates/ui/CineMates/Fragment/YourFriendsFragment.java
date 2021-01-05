package com.example.cinemates.ui.CineMates.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.friends.ItemFriend;
import com.example.cinemates.ui.CineMates.friends.ReclycleViewAdapter_Amico;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link YourFriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YourFriendsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private List<ItemFriend> friendList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public YourFriendsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment YourFriendsFragment.
     */
    // TODO: Rename and change types and number of parameters
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

        friendList = new ArrayList<>();
        friendList.add(new ItemFriend("SauTube", ProfileFragment.getBitmapFromdownload("https://image.flaticon.com/icons/png/128/1077/1077114.png")));
        friendList.add(new ItemFriend("Giardina", ProfileFragment.getBitmapFromdownload("https://image.flaticon.com/icons/png/128/1077/1077114.png")));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_your_friends, container, false);

        recyclerView = view.findViewById(R.id.recycleView_fragment_YourFriends);
        ReclycleViewAdapter_Amico reclycleViewAdapterAmico = new ReclycleViewAdapter_Amico(getContext(), friendList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(reclycleViewAdapterAmico);

        return view;
    }


}