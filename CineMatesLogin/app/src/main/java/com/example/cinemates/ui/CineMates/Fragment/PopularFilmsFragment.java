package com.example.cinemates.ui.CineMates.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.ItemFilm;
import com.example.cinemates.ui.CineMates.RecycleViewAdapter_Film;
import com.example.cinemates.ui.CineMates.SchedaFilmActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PopularFilmsFragment extends Fragment implements RecycleViewAdapter_Film.OnClickListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView_Film;
    private List<ItemFilm> filmList;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;


    public PopularFilmsFragment() {
        // Required empty public constructor
    }

    public PopularFilmsFragment(List<ItemFilm> filmList) {
        this.filmList = filmList;
    }


    public static PopularFilmsFragment newInstance(String param1, String param2) {
        PopularFilmsFragment fragment = new PopularFilmsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_popular_films, container, false);
        recyclerView_Film = view.findViewById(R.id.recycleView_fragment_popularFilms);
        RecycleViewAdapter_Film recycleViewAdapter_film = new RecycleViewAdapter_Film(getContext(), filmList, this);
        recyclerView_Film.setHasFixedSize(true);
        recyclerView_Film.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.HORIZONTAL));
        recyclerView_Film.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2 , GridLayoutManager.VERTICAL, false);

        recyclerView_Film.setLayoutManager(gridLayoutManager);
        recyclerView_Film.setAdapter(recycleViewAdapter_film);

        return view;
    }

    @Override
    public void OnClick(int position) {
        startActivity(new Intent(getActivity(), SchedaFilmActivity.class));
    }
}