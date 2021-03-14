package com.example.cinemates.ui.CineMates.presenters.activities;

import android.view.View;

import com.example.cinemates.R;
import com.example.cinemates.databinding.ActivityListeUtenteBinding;
import com.example.cinemates.ui.CineMates.model.ItemFilm;
import com.example.cinemates.ui.CineMates.views.activities.ListeUtenteActivity;
import com.example.cinemates.ui.CineMates.views.fragments.DaVedereFragment;
import com.example.cinemates.ui.CineMates.views.fragments.PreferitiFragment;
import com.example.cinemates.ui.CineMates.views.fragments.ViewPagerAdapter;

import java.util.List;
import java.util.Objects;

public class ListeUtentePresenter {
    private final ListeUtenteActivity listeUtenteActivity;
    private ViewPagerAdapter adapter;
    public static List<ItemFilm> filmPre;
    public static List<ItemFilm> filmDaV;
    private List<ItemFilm> filmPreferiti;
    private List<ItemFilm> filmDavedere;

    public ListeUtentePresenter(ListeUtenteActivity listeUtenteActivity, ActivityListeUtenteBinding binding) {
        this.listeUtenteActivity = listeUtenteActivity;
        init(binding);
    }

    private void init(ActivityListeUtenteBinding binding){
        adapter = new ViewPagerAdapter(listeUtenteActivity.getSupportFragmentManager());
        binding.tabLayoutListeUtente.setupWithViewPager(binding.viewPagerListeUtente);
        filmPreferiti = filmPre;
        filmDavedere = filmDaV;
        adapter.AddFragment(new PreferitiFragment(filmPreferiti), "Preferiti");
        adapter.AddFragment(new DaVedereFragment(filmDavedere), "Da Vedere");
        binding.viewPagerListeUtente.setAdapter(adapter);
        Objects.requireNonNull(binding.tabLayoutListeUtente.getTabAt(0)).setIcon(R.drawable.ic_favorite);
        Objects.requireNonNull(binding.tabLayoutListeUtente.getTabAt(1)).setIcon(R.drawable.ic_next);
    }

    public void BackButton(ActivityListeUtenteBinding binding){
        binding.backButtonListeUtente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listeUtenteActivity.onBackPressed();
            }
        });
    }

    public void update(){
        adapter.update();
    }
}
