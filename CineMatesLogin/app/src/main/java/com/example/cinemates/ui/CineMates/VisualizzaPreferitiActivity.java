package com.example.cinemates.ui.CineMates;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemates.databinding.ActivityVisualizzaPreferitiBinding;
import com.example.cinemates.ui.CineMates.Fragment.HomeFragment;
import com.example.cinemates.ui.CineMates.Fragment.ProfileFragment;

import java.util.List;

public class VisualizzaPreferitiActivity extends AppCompatActivity {
    private ActivityVisualizzaPreferitiBinding binding;
    HomeFragment homeFragment;
    ProfileFragment profileFragment;
    private List<ItemFilm> filmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVisualizzaPreferitiBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        /*filmList = new ArrayList<>();
        filmList.add(new ItemFilm(""));

        RecycleViewAdapter_Film recycleViewAdapterFilm = new RecycleViewAdapter_Film(getContext(), filmList, this);
        binding.FilmVisualizzaPreferiti.setLayoutManager(new LinearLayoutManager(this));
        binding.FilmVisualizzaPreferiti.setAdapter(recycleViewAdapterFilm);*/

        Keyboard(binding);
        HomeButton(binding);
        BackButton(binding);
    }

    private void Keyboard(ActivityVisualizzaPreferitiBinding binding) {
        binding.containerVisualizzaPreferiti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.containerVisualizzaPreferiti.getWindowToken(), 0);
                binding.searchBarVisualizzaPreferiti.clearFocus();
            }
        });
    }

    private void HomeButton(ActivityVisualizzaPreferitiBinding binding){
        binding.homeButtonVisualizzaPreferiti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeFragment = new HomeFragment();
            }
        });
    }

    private void BackButton(ActivityVisualizzaPreferitiBinding binding){
        binding.backButtonVisualizzaPreferiti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileFragment = new ProfileFragment();
            }
        });
    }
}