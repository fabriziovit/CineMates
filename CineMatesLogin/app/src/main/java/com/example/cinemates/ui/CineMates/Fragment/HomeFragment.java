package com.example.cinemates.ui.CineMates.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.ApiMovie.PopularFilms;
import com.example.cinemates.ui.CineMates.ItemFilm;
import com.example.cinemates.ui.CineMates.RecycleViewAdapter_Film;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements RecycleViewAdapter_Film.OnClickListener {
    private PopularFilms popularFilms;
    private ArrayList<ItemFilm> films;
    private RecyclerView recyclerViewFilm;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public HomeFragment() {
        // Required empty public constructor
    }

    public HomeFragment(PopularFilms popularFilms, ArrayList<ItemFilm> films){
        this.popularFilms = popularFilms;
        this.films = films;
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

        recyclerViewFilm = view.findViewById(R.id.recycleView_fragment_Home);
        RecycleViewAdapter_Film recycleViewAdapter_film = new RecycleViewAdapter_Film(getContext(), films, this);
        recyclerViewFilm.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewFilm.setAdapter(recycleViewAdapter_film);
        return view;
    }

    @Override
    public void OnClick(int position) {

    }
}