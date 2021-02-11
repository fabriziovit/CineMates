package com.example.cinemates.ui.CineMates.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.SchedaFilmActivity;
import com.example.cinemates.ui.CineMates.adapter.RecycleViewAdapter_Film_ListaPreferiti;
import com.example.cinemates.ui.CineMates.model.ItemFilm;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import Intefaces.UpdateableFragmentListener;

public class DaVedereFragment extends Fragment implements RecycleViewAdapter_Film_ListaPreferiti.OnClickListener, UpdateableFragmentListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<ItemFilm> daVederelist;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String currUser;
    private RecyclerView recyclerView;

    public DaVedereFragment() {
        // Required empty public constructor
    }

    public DaVedereFragment(List<ItemFilm> daVederelist){
        this.daVederelist = daVederelist;
    }

    public static DaVedereFragment newInstance(String param1, String param2) {
        DaVedereFragment fragment = new DaVedereFragment();
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
        auth = FirebaseAuth.getInstance();
        currUser = auth.getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_da_vedere, container, false);
        recyclerView = view.findViewById(R.id.film_davedereFragment_recycleView);
        RecycleViewAdapter_Film_ListaPreferiti recycleViewAdapter_film_listaPreferiti = new RecycleViewAdapter_Film_ListaPreferiti(getContext(), daVederelist, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2 , GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(recycleViewAdapter_film_listaPreferiti);

        return view;
    }

    @Override
    public void OnClickScheda(int position) {
        Intent i = new Intent(getActivity(), SchedaFilmActivity.class);
        i.putExtra("id", daVederelist.get(position).getId());
        startActivity(i);
    }

    @Override
    public void OnClickRimuovi(int position){
        db.collection("da vedere").document(currUser).collection(currUser).document(String.valueOf(daVederelist.get(position).getId())).delete();
        daVederelist.remove(position);
        update();
        Toast.makeText(getActivity(), "Film eliminato dalla lista!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void update() {
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}