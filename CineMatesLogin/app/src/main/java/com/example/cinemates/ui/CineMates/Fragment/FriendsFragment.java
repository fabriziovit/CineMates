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
import com.example.cinemates.ui.CineMates.friends.model.ItemFriend;
import com.example.cinemates.ui.CineMates.friends.model.ItemUser;
import com.example.cinemates.ui.CineMates.friends.NotificheDialog;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class FriendsFragment extends Fragment{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private List<ItemUser> userList;
    private List<ItemFriend> friendList;
    private ImageView bottoneNotifica;
    private ImageView indicatoreNotifica;
    private boolean notifiche;

    public FriendsFragment() {
        // Required empty public constructor
    }

    public FriendsFragment(List<ItemUser> userList, List<ItemFriend> friendList, boolean notifiche) {
        this.userList = userList;
        this.friendList = friendList;
        this.notifiche = notifiche;
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
        bottoneNotifica = view.findViewById(R.id.notifica_fragment_friends);
        indicatoreNotifica = view.findViewById(R.id.indicatore_notifica_friendsFragment);
        tabLayout = view.findViewById(R.id.tabLayout_fragment_friends);
        viewPager = view.findViewById(R.id.viewPager_fragment_friends);
        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        if(notifiche)
            indicatoreNotifica.setVisibility(View.VISIBLE);
        else
            indicatoreNotifica.setVisibility(View.INVISIBLE);

        adapter.AddFragment(new YourFriendsFragment(friendList, userList, adapter), "Lista Amici");
        adapter.AddFragment(new SearchFriendsFragment(userList), "Cerca Utenti");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        TabLayout();
        ApriNotifiche();
        update();

        return view;
    }

    public void ApriNotifiche() {
        bottoneNotifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indicatoreNotifica.setVisibility(View.INVISIBLE);
                final NotificheDialog notificheDialog = new NotificheDialog(getActivity(), friendList, userList, adapter);
                notificheDialog.setCancelable(false);
                showDialog(notificheDialog);
            }
        });
    }

    public void showDialog(DialogFragment dialogFragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        dialogFragment.show(fragmentManager, "dialog");
    }

    public void update(){
        adapter.update();
    }

    public void TabLayout(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabLayout.getTabAt(0).setIcon(R.drawable.ic_amici_focused);
                tabLayout.getTabAt(1).setIcon(R.drawable.ic_addfriends);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabLayout.getTabAt(0).setIcon(R.drawable.ic_amici_focused);
                tabLayout.getTabAt(1).setIcon(R.drawable.ic_addfriends);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                tabLayout.getTabAt(0).setIcon(R.drawable.ic_amici_focused);
                tabLayout.getTabAt(1).setIcon(R.drawable.ic_addfriends);
            }
        });
    }
}