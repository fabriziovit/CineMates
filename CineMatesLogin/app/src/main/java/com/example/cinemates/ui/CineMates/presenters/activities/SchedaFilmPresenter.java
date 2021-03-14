package com.example.cinemates.ui.CineMates.presenters.activities;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.cinemates.R;
import com.example.cinemates.databinding.ActivitySchedaFilmBinding;
import com.example.cinemates.ui.CineMates.views.activities.SchedaFilmActivity;
import com.example.cinemates.ui.CineMates.views.fragments.InfoFilmFragment;
import com.example.cinemates.ui.CineMates.views.fragments.RecensioniFilmFragment;
import com.example.cinemates.ui.CineMates.views.fragments.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import Intefaces.UpdateableFragmentListener;

public class SchedaFilmPresenter implements UpdateableFragmentListener {
    private final SchedaFilmActivity schedaFilmActivity;
    private ViewPagerAdapter adapter;
    private int id;


    public SchedaFilmPresenter(SchedaFilmActivity schedaFilmActivity, int id, ActivitySchedaFilmBinding binding) {
        this.schedaFilmActivity = schedaFilmActivity;
        this.id = id;
        init(binding);
    }

    private void init(ActivitySchedaFilmBinding binding){
        adapter = new ViewPagerAdapter(schedaFilmActivity.getSupportFragmentManager());
        adapter.AddFragment(new InfoFilmFragment(id), "Scheda Film");
        adapter.AddFragment(new RecensioniFilmFragment(id), "Recensioni");
        binding.viewPagerSchedaFilm.setAdapter(adapter);
        binding.tabLayoutSchedaFilm.setupWithViewPager(binding.viewPagerSchedaFilm);
    }

    public void Keyboard(ActivitySchedaFilmBinding binding) {
        binding.containerSchedaFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) schedaFilmActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.containerSchedaFilm.getWindowToken(), 0);
            }
        });
    }

    public void BackButton(ActivitySchedaFilmBinding binding){
        binding.backButtonSchedaFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                schedaFilmActivity.onBackPressed();
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
