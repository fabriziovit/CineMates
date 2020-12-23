package com.example.cinemates.ui.CineMates;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.cinemates.databinding.FragmentProfileBinding;
import com.example.cinemates.ui.CineMates.Fragment.FriendsFragment;
import com.example.cinemates.ui.CineMates.Fragment.HomeFragment;
import com.example.cinemates.ui.CineMates.Fragment.ProfileFragment;
import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.Fragment.SearchFragment;
import com.example.cinemates.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = HomeActivity.class.getSimpleName();
    private ActivityHomeBinding binding;
    ChipNavigationBar bottomNav;
    FragmentManager fragmentManager;

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        bottomNav =  binding.navHomeMenu;

        if(savedInstanceState == null){
            bottomNav.setItemSelected(R.id.main, true);
            fragmentManager = getSupportFragmentManager();
            HomeFragment homeFragment = new HomeFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_home_container, homeFragment)
                    .commit();
        }

        bottomNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                boolean isProfile = false;
                fragment = null;
                switch (id){
                    case R.id.main:
                        fragment = new HomeFragment();
                        break;
                    case R.id.search:
                        fragment = new SearchFragment();
                        break;
                    case R.id.profilo:
                        isProfile = true;
                        new Thread(()->{
                            Bitmap profilePic = ProfileFragment.getBitmapFromdownload("https://better-default-discord.netlify.app/Icons/Gradient-Gray.png");
                            fragment = new ProfileFragment(profilePic);
                            fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_home_container, fragment)
                                    .commit();
                        }).start();
                        break;
                    case R.id.amici:
                        fragment = new FriendsFragment();
                        break;
                }

                if(fragment!=null && !isProfile){
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_home_container, fragment)
                            .commit();
                }else {
                    Log.e(TAG, "Error in creating fragment");
                }
            }
        });
    }
}