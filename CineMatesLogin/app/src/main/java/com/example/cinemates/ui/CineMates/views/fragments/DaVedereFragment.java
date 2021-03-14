package com.example.cinemates.ui.CineMates.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.adapter.RecycleViewAdapter_Film_ListaPreferiti;
import com.example.cinemates.ui.CineMates.model.ItemFilm;
import com.example.cinemates.ui.CineMates.presenters.fragments.DaVederePresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class DaVedereFragment extends Fragment {
    public List<ItemFilm> daVederelist;
    public FirebaseFirestore db;
    public FirebaseAuth auth;
    public String currUser;
    public RecyclerView recyclerView;
    public TextView filmVuoti;
    private DaVederePresenter daVederePresenter;

    public DaVedereFragment() {
        // Required empty public constructor
    }

    public DaVedereFragment(List<ItemFilm> daVederelist){
        this.daVederelist = daVederelist;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currUser = auth.getCurrentUser().getUid();
        daVederePresenter = new DaVederePresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_da_vedere, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.film_davedereFragment_recycleView);
        filmVuoti = view.findViewById(R.id.textEdit_listaVuota_daVedere);
        if (daVederelist.size() == 0) {
            filmVuoti.setVisibility(View.VISIBLE);
        } else {
            filmVuoti.setVisibility(View.INVISIBLE);
            RecycleViewAdapter_Film_ListaPreferiti recycleViewAdapter_film_listaPreferiti = new RecycleViewAdapter_Film_ListaPreferiti(getContext(), daVederelist, daVederePresenter);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(recycleViewAdapter_film_listaPreferiti);
        }
    }
}