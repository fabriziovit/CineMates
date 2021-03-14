package com.example.cinemates.ui.CineMates.presenters.fragments;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.example.cinemates.ui.CineMates.adapter.RecycleViewAdapter_Film_ListaPreferiti;
import com.example.cinemates.ui.CineMates.views.activities.SchedaFilmActivity;
import com.example.cinemates.ui.CineMates.views.fragments.PreferitiFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import Intefaces.UpdateableFragmentListener;

import static com.example.cinemates.ui.CineMates.util.Constants.KEY_MOVIE_ID;

public class PreferitiPresenter implements RecycleViewAdapter_Film_ListaPreferiti.OnClickListener, UpdateableFragmentListener {
    private final PreferitiFragment preferitiFragment;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    public PreferitiPresenter(PreferitiFragment preferitiFragment, FirebaseFirestore db, FirebaseAuth auth) {
        this.preferitiFragment = preferitiFragment;
        this.db = db;
        this.auth = auth;
    }

    @Override
    public void OnClickScheda(int position) {
        Intent i = new Intent(preferitiFragment.getActivity(), SchedaFilmActivity.class);
        i.putExtra(KEY_MOVIE_ID, preferitiFragment.preferitiList.get(position).getId());
        preferitiFragment.startActivity(i);
    }

    @Override
    public void OnClickRimuovi(int position){
        db.collection("favorites").document(preferitiFragment.currUser).collection(preferitiFragment.currUser).document(String.valueOf(preferitiFragment.preferitiList.get(position).getId())).delete();
        preferitiFragment.preferitiList.remove(position);
        update();
        Toast.makeText(preferitiFragment.getActivity(), "Film eliminato dalla lista!", Toast.LENGTH_SHORT).show();
        if(preferitiFragment.preferitiList.size() == 0)
            preferitiFragment.filmVuoti.setVisibility(View.VISIBLE);
        else
            preferitiFragment.filmVuoti.setVisibility(View.INVISIBLE);
    }

    @Override
    public void update() {
        preferitiFragment.recyclerView.getAdapter().notifyDataSetChanged();
    }

}
