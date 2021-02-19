package com.example.cinemates.ui.CineMates.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemates.R;
import com.example.cinemates.databinding.ActivitySchedaFilmBinding;
import com.example.cinemates.ui.CineMates.Fragment.InfoFilmFragment;
import com.example.cinemates.ui.CineMates.Fragment.RecensioniFilmFragment;
import com.example.cinemates.ui.CineMates.Fragment.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import Intefaces.UpdateableFragmentListener;

import static com.example.cinemates.ui.CineMates.util.Constants.KEY_MOVIE_ID;

public class SchedaFilmActivity extends AppCompatActivity implements UpdateableFragmentListener {
    private ActivitySchedaFilmBinding binding;
    private int id;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySchedaFilmBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //Prendo l'id del film dalla scheda da cui provengo
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt(KEY_MOVIE_ID);
        }

        adapter = new ViewPagerAdapter(this.getSupportFragmentManager());
        adapter.AddFragment(new InfoFilmFragment(id), "Scheda Film");
        adapter.AddFragment(new RecensioniFilmFragment(id), "Recensioni");
        binding.viewPagerSchedaFilm.setAdapter(adapter);
        binding.tabLayoutSchedaFilm.setupWithViewPager(binding.viewPagerSchedaFilm);

        Keyboard(binding);
        BackButton(binding);
        TabLayout(binding);
        update();
    }

    private void Keyboard(ActivitySchedaFilmBinding binding) {
        binding.containerSchedaFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.containerSchedaFilm.getWindowToken(), 0);
            }
        });
    }

    private void BackButton(ActivitySchedaFilmBinding binding){
        binding.backButtonSchedaFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void update() {
        adapter.notifyDataSetChanged();
    }

    public void TabLayout(ActivitySchedaFilmBinding binding){
        binding.tabLayoutSchedaFilm.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.tabLayoutSchedaFilm.getTabAt(0).setIcon(R.drawable.ic_info);
                binding.tabLayoutSchedaFilm.getTabAt(1).setIcon(R.drawable.ic_stella);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                binding.tabLayoutSchedaFilm.getTabAt(0).setIcon(R.drawable.ic_info);
                binding.tabLayoutSchedaFilm.getTabAt(1).setIcon(R.drawable.ic_stella);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                binding.tabLayoutSchedaFilm.getTabAt(0).setIcon(R.drawable.ic_info);
                binding.tabLayoutSchedaFilm.getTabAt(1).setIcon(R.drawable.ic_stella);
            }
        });
    }
}