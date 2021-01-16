package com.example.cinemates.ui.CineMates.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.friends.ItemFriend;
import com.example.cinemates.ui.CineMates.friends.ItemUser;
import com.example.cinemates.ui.CineMates.friends.NotificheDialog;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class FriendsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private List<ItemUser> userList;
    private ImageView notifica;
    private List<ItemFriend> friendList;


    public FriendsFragment() {
        // Required empty public constructor
    }

    public FriendsFragment(List<ItemUser> userList, List<ItemFriend> friendList){
        this.userList = userList;
        this.friendList = friendList;
    }

    public static FriendsFragment newInstance(String param1, String param2) {
        FriendsFragment fragment = new FriendsFragment();
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
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        notifica = view.findViewById(R.id.notifica_fragment_friends);
        tabLayout = view.findViewById(R.id.tabLayout_fragment_friends);
        viewPager = view.findViewById(R.id.viewPager_fragment_friends);
        adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.AddFragment(new YourFriendsFragment(friendList), "Lista Amici");
        adapter.AddFragment(new SearchFriendsFragment(userList), "Cerca Utenti");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_amici_focused);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_addfriends);
        ApriNotifiche();

        return view;
    }

    public void ApriNotifiche(){
        notifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                final NotificheDialog notificheDialog = new NotificheDialog(getActivity());
                showDialog(notificheDialog);
            }
        });
    }

    public void showDialog(DialogFragment dialogFragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        dialogFragment.show(fragmentManager, "dialog");
    }
}