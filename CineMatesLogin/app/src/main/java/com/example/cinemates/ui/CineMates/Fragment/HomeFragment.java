package com.example.cinemates.ui.CineMates.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.ItemFilm;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment{
    private ArrayList<ItemFilm> filmsPopular;
    private ArrayList<ItemFilm> filmsUpcoming;
    private ArrayList<ItemFilm> filmsNowplaying;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public HomeFragment() {
        // Required empty public constructor
    }

    public HomeFragment(ArrayList<ItemFilm> filmsPopular, ArrayList<ItemFilm> filmsUpcoming, ArrayList<ItemFilm> filmsNowplaying){
        this.filmsPopular = filmsPopular;
        this.filmsNowplaying = filmsNowplaying;
        this.filmsUpcoming = filmsUpcoming;
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

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

        return view;
    }

}