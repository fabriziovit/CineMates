package com.example.cinemates.ui.CineMates.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.activity.SchedaFilmActivity;
import com.example.cinemates.ui.CineMates.adapter.RecycleViewAdapter_Film_ListaPreferiti;
import com.example.cinemates.ui.CineMates.model.ItemFilm;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import Intefaces.UpdateableFragmentListener;

import static com.example.cinemates.ui.CineMates.util.Constants.KEY_MOVIE_ID;

public class PreferitiFragment extends Fragment implements RecycleViewAdapter_Film_ListaPreferiti.OnClickListener, UpdateableFragmentListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<ItemFilm> preferitiList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String currUser;
    private RecyclerView recyclerView;
    private TextView filmVuoti;


    public PreferitiFragment() {
        // Required empty public constructor
    }

    public PreferitiFragment(List<ItemFilm> preferitiList){
        this.preferitiList = preferitiList;
    }

    public static PreferitiFragment newInstance(String param1, String param2) {
        PreferitiFragment fragment = new PreferitiFragment();
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
        View view = inflater.inflate(R.layout.fragment_preferiti, container, false);
        recyclerView = view.findViewById(R.id.film_preferitiFragment_recycleView);
        filmVuoti = view.findViewById(R.id.textEdit_listaVuota_preferiti);
        if(preferitiList.size() == 0){
            filmVuoti.setVisibility(View.VISIBLE);
        }else{
            filmVuoti.setVisibility(View.INVISIBLE);
            RecycleViewAdapter_Film_ListaPreferiti recycleViewAdapter_film_listaPreferiti = new RecycleViewAdapter_Film_ListaPreferiti(getContext(), preferitiList, this);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2 , GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(recycleViewAdapter_film_listaPreferiti);
        }

        return view;
    }

    @Override
    public void OnClickScheda(int position) {
        Intent i = new Intent(getActivity(), SchedaFilmActivity.class);
        i.putExtra(KEY_MOVIE_ID, preferitiList.get(position).getId());
        startActivity(i);
    }

    @Override
    public void OnClickRimuovi(int position){
        db.collection("favorites").document(currUser).collection(currUser).document(String.valueOf(preferitiList.get(position).getId())).delete();
        preferitiList.remove(position);
        update();
        Toast.makeText(getActivity(), "Film eliminato dalla lista!", Toast.LENGTH_SHORT).show();
        if(preferitiList.size() == 0)
            filmVuoti.setVisibility(View.VISIBLE);
        else
            filmVuoti.setVisibility(View.INVISIBLE);
    }

    @Override
    public void update() {
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}