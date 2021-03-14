package com.example.cinemates.ui.CineMates.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.model.ItemFilm;
import com.example.cinemates.ui.CineMates.presenters.fragments.HomeFragmentPresenter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment{
    public static ArrayList<ItemFilm> filmsPopular;
    public static ArrayList<ItemFilm> filmsUpcoming;
    public static ArrayList<ItemFilm> filmsNowplaying;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private HomeFragmentPresenter homeFragmentPresenter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeFragmentPresenter = new HomeFragmentPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        tabLayout = view.findViewById(R.id.tabLayout_fragment_home);
        viewPager = view.findViewById(R.id.viewPager_fragment_home);
        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());

        adapter.AddFragment(new PopularFilmsFragment(filmsPopular), "Popolari");
        adapter.AddFragment(new NowPlayingFilmsFragment(filmsNowplaying), "Al cinema");
        adapter.AddFragment(new UpcomingFilmsFragment(filmsUpcoming), "In uscita");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.ic_popolare);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.ic_ticket);
        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(R.drawable.ic_calendario);
    }

}