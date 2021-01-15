package com.example.cinemates.ui.CineMates.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.ApiMovie.Movie;
import com.example.cinemates.ui.CineMates.ApiMovie.PopularFilms;

public class HomeFragment extends Fragment {
    private PopularFilms popularFilms;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public HomeFragment() {
        // Required empty public constructor
    }

    public HomeFragment(PopularFilms popularFilms){ this.popularFilms = popularFilms; }

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

        new Thread(()-> {
            for (Movie movie : popularFilms.getResults())
                System.out.println(movie.getTitle());
        }).start();
        return view;
    }
}