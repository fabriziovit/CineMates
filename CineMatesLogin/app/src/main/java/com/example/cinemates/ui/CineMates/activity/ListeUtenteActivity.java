package com.example.cinemates.ui.CineMates.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemates.R;
import com.example.cinemates.databinding.ActivityListeUtenteBinding;
import com.example.cinemates.ui.CineMates.Fragment.DaVedereFragment;
import com.example.cinemates.ui.CineMates.Fragment.PreferitiFragment;
import com.example.cinemates.ui.CineMates.Fragment.ViewPagerAdapter;
import com.example.cinemates.ui.CineMates.model.ItemFilm;

import java.util.List;
import java.util.Objects;

public class ListeUtenteActivity extends AppCompatActivity {
    private ActivityListeUtenteBinding binding;
    private List<ItemFilm> filmPreferiti;
    private List<ItemFilm> filmDavedere;
    public static List<ItemFilm> filmPre;
    public static List<ItemFilm> filmDaV;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListeUtenteBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        adapter = new ViewPagerAdapter(ListeUtenteActivity.this.getSupportFragmentManager());
        binding.tabLayoutListeUtente.setupWithViewPager(binding.viewPagerListeUtente);
        filmPreferiti = filmPre;
        filmDavedere = filmDaV;
        adapter.AddFragment(new PreferitiFragment(filmPreferiti), "Preferiti");
        adapter.AddFragment(new DaVedereFragment(filmDavedere), "Da Vedere");
        binding.viewPagerListeUtente.setAdapter(adapter);
        Objects.requireNonNull(binding.tabLayoutListeUtente.getTabAt(0)).setIcon(R.drawable.ic_favorite);
        Objects.requireNonNull(binding.tabLayoutListeUtente.getTabAt(1)).setIcon(R.drawable.ic_next);

        BackButton(binding);
    }

    private void BackButton(ActivityListeUtenteBinding binding){
        binding.backButtonListeUtente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void update(){
        adapter.update();
    }
}