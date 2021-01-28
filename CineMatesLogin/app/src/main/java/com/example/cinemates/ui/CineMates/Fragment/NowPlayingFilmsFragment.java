package com.example.cinemates.ui.CineMates.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.model.ItemFilm;
import com.example.cinemates.ui.CineMates.adapter.RecycleViewAdapter_Film;
import com.example.cinemates.ui.CineMates.SchedaFilmActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class NowPlayingFilmsFragment extends Fragment implements RecycleViewAdapter_Film.OnClickListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView_Film;
    private List<ItemFilm> filmList;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;

    public NowPlayingFilmsFragment() {
        // Required empty public constructor
    }

    public NowPlayingFilmsFragment(List<ItemFilm> filmList) {
        this.filmList = filmList;
    }

    public static NowPlayingFilmsFragment newInstance(String param1, String param2) {
        NowPlayingFilmsFragment fragment = new NowPlayingFilmsFragment();
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
        View view = inflater.inflate(R.layout.fragment_now_playing_films, container, false);
        recyclerView_Film = view.findViewById(R.id.recycleView_fragment_nowplayingFilms);
        RecycleViewAdapter_Film recycleViewAdapter_film = new RecycleViewAdapter_Film(getContext(), filmList, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2 , GridLayoutManager.VERTICAL, false);
        recyclerView_Film.setLayoutManager(gridLayoutManager);
        recyclerView_Film.setAdapter(recycleViewAdapter_film);

        return view;
    }

    @Override
    public void OnClick(int position) {
        Intent i = new Intent(getActivity(), SchedaFilmActivity.class);
        i.putExtra("id", filmList.get(position).getId());
        startActivity(i);
    }
}