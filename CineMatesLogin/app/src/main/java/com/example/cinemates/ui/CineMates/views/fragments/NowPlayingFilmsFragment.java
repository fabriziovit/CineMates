package com.example.cinemates.ui.CineMates.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.views.activities.SchedaFilmActivity;
import com.example.cinemates.ui.CineMates.adapter.RecycleViewAdapter_Film;
import com.example.cinemates.ui.CineMates.model.ItemFilm;

import java.util.List;

import static com.example.cinemates.ui.CineMates.util.Constants.KEY_MOVIE_ID;

public class NowPlayingFilmsFragment extends Fragment implements RecycleViewAdapter_Film.OnClickListener{
    private RecyclerView recyclerView_Film;
    private List<ItemFilm> filmList;

    public NowPlayingFilmsFragment() {
        // Required empty public constructor
    }

    public NowPlayingFilmsFragment(List<ItemFilm> filmList) {
        this.filmList = filmList;
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
        i.putExtra(KEY_MOVIE_ID, filmList.get(position).getId());
        startActivity(i);
    }
}