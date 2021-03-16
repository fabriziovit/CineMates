package com.example.cinemates.ui.CineMates.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.adapter.RecycleViewAdapter_Film_ListaPreferiti;
import com.example.cinemates.ui.CineMates.model.ItemFilm;
import com.example.cinemates.ui.CineMates.presenters.fragments.PreferitiPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PreferitiFragment extends Fragment {
    private List<ItemFilm> preferitiList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    public String currUser;
    public RecyclerView recyclerView;
    public TextView filmVuoti;
    private PreferitiPresenter preferitiPresenter;


    public PreferitiFragment() {
        // Required empty public constructor
    }

    public PreferitiFragment(List<ItemFilm> preferitiList){
        this.preferitiList = preferitiList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currUser = auth.getCurrentUser().getUid();
        preferitiPresenter = new PreferitiPresenter(this, db, preferitiList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preferiti, container, false);
        recyclerView = view.findViewById(R.id.film_preferitiFragment_recycleView);
        filmVuoti = view.findViewById(R.id.textEdit_listaVuota_preferiti);
        if(preferitiList.size() == 0){
            filmVuoti.setVisibility(View.VISIBLE);
        }else{
            filmVuoti.setVisibility(View.INVISIBLE);
            RecycleViewAdapter_Film_ListaPreferiti recycleViewAdapter_film_listaPreferiti = new RecycleViewAdapter_Film_ListaPreferiti(getContext(), preferitiList, preferitiPresenter);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2 , GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(recycleViewAdapter_film_listaPreferiti);
        }

        return view;
    }
}