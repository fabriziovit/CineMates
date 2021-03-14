package com.example.cinemates.ui.CineMates.presenters.fragments;

import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.friends.NotificheDialog;
import com.example.cinemates.ui.CineMates.views.fragments.FriendsFragment;
import com.google.android.material.tabs.TabLayout;

public class FriendsPresenter {
    private final FriendsFragment friendsFragment;

    public FriendsPresenter(FriendsFragment friendsFragment) {
        this.friendsFragment = friendsFragment;
    }

    public void ApriNotifiche() {
        friendsFragment.bottoneNotifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendsFragment.indicatoreNotifica.setVisibility(View.INVISIBLE);
                final NotificheDialog notificheDialog = new NotificheDialog(friendsFragment.getActivity(), friendsFragment.friendList, friendsFragment.userList, friendsFragment.adapter);
                notificheDialog.setCancelable(false);
                showDialog(notificheDialog);
            }
        });
    }

    public void showDialog(DialogFragment dialogFragment) {
        FragmentManager fragmentManager = friendsFragment.getParentFragmentManager();
        dialogFragment.show(fragmentManager, "dialog");
    }

    public void update(){
        friendsFragment.adapter.update();
    }

    public void TabLayout(){
        friendsFragment.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                friendsFragment.tabLayout.getTabAt(0).setIcon(R.drawable.ic_amici_focused);
                friendsFragment.tabLayout.getTabAt(1).setIcon(R.drawable.ic_addfriends);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                friendsFragment.tabLayout.getTabAt(0).setIcon(R.drawable.ic_amici_focused);
                friendsFragment.tabLayout.getTabAt(1).setIcon(R.drawable.ic_addfriends);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                friendsFragment.tabLayout.getTabAt(0).setIcon(R.drawable.ic_amici_focused);
                friendsFragment.tabLayout.getTabAt(1).setIcon(R.drawable.ic_addfriends);
            }
        });
    }
}
