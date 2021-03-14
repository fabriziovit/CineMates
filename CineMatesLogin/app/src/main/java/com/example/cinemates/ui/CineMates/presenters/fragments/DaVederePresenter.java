package com.example.cinemates.ui.CineMates.presenters.fragments;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.example.cinemates.ui.CineMates.adapter.RecycleViewAdapter_Film_ListaPreferiti;
import com.example.cinemates.ui.CineMates.views.activities.SchedaFilmActivity;
import com.example.cinemates.ui.CineMates.views.fragments.DaVedereFragment;

import Intefaces.UpdateableFragmentListener;

import static com.example.cinemates.ui.CineMates.util.Constants.KEY_MOVIE_ID;

public class DaVederePresenter implements RecycleViewAdapter_Film_ListaPreferiti.OnClickListener, UpdateableFragmentListener {
    private final DaVedereFragment daVedereFragment;

    public DaVederePresenter(DaVedereFragment daVedereFragment) {
        this.daVedereFragment = daVedereFragment;
    }


    @Override
    public void OnClickScheda(int position) {
        Intent i = new Intent(daVedereFragment.getActivity(), SchedaFilmActivity.class);
        i.putExtra(KEY_MOVIE_ID, daVedereFragment.daVederelist.get(position).getId());
        daVedereFragment.getActivity().startActivity(i);
    }

    @Override
    public void OnClickRimuovi(int position){
        daVedereFragment.db.collection("da vedere").document(daVedereFragment.currUser).collection(daVedereFragment.currUser).document(String.valueOf(daVedereFragment.daVederelist.get(position).getId())).delete();
        daVedereFragment.daVederelist.remove(position);
        update();
        Toast.makeText(daVedereFragment.getActivity(), "Film eliminato dalla lista!", Toast.LENGTH_SHORT).show();
        if(daVedereFragment.daVederelist.size() == 0)
            daVedereFragment.filmVuoti.setVisibility(View.VISIBLE);
        else
            daVedereFragment.filmVuoti.setVisibility(View.INVISIBLE);
    }

    @Override
    public void update() {
        daVedereFragment.recyclerView.getAdapter().notifyDataSetChanged();
    }
}
