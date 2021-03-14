package com.example.cinemates.ui.CineMates.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.friends.model.ItemFriend;
import com.example.cinemates.ui.CineMates.friends.model.ItemUser;
import com.example.cinemates.ui.CineMates.presenters.fragments.FriendsPresenter;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class FriendsFragment extends Fragment{
    public TabLayout tabLayout;
    public ViewPager viewPager;
    public ViewPagerAdapter adapter;
    public List<ItemUser> userList;
    public List<ItemFriend> friendList;
    public ImageView bottoneNotifica;
    public ImageView indicatoreNotifica;
    public boolean notifiche;
    private FriendsPresenter friendsPresenter;

    public FriendsFragment() {
        // Required empty public constructor
    }

    public FriendsFragment(List<ItemUser> userList, List<ItemFriend> friendList, boolean notifiche) {
        this.userList = userList;
        this.friendList = friendList;
        this.notifiche = notifiche;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friendsPresenter = new FriendsPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
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
        tabLayout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        friendsPresenter.TabLayout();
        friendsPresenter.ApriNotifiche();
        friendsPresenter.update();
    }
}