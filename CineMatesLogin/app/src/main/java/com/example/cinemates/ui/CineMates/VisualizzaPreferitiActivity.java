package com.example.cinemates.ui.CineMates;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cinemates.databinding.ActivityVisualizzaPreferitiBinding;
import com.example.cinemates.ui.CineMates.Fragment.ProfileFragment;

import java.util.ArrayList;
import java.util.List;

public class VisualizzaPreferitiActivity extends AppCompatActivity implements RecycleViewAdapter_Film.OnClickListener{
    private ActivityVisualizzaPreferitiBinding binding;
    private List<ItemFilm> filmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVisualizzaPreferitiBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        filmList = new ArrayList<>();
        filmList.add(new ItemFilm("Finbonacci", "1990", "Action", "Renato Tondi", "9,0", ProfileFragment.getBitmapFromdownload("https://image.flaticon.com/icons/png/128/1077/1077114.png")));
        filmList.add(new ItemFilm("Ghioza", "2020", "Commedia", "Francesco Noccio", "6,5", ProfileFragment.getBitmapFromdownload("https://image.flaticon.com/icons/png/128/1077/1077114.png")));

        RecycleViewAdapter_Film recycleViewAdapterFilm = new RecycleViewAdapter_Film(this, filmList, this);
        binding.FilmVisualizzaPreferiti.setLayoutManager(new LinearLayoutManager(this));
        binding.FilmVisualizzaPreferiti.setAdapter(recycleViewAdapterFilm);

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
                startActivity(new Intent(VisualizzaPreferitiActivity.this, HomeActivity.class));
            }
        });
    }

    private void BackButton(ActivityVisualizzaPreferitiBinding binding){
        binding.backButtonVisualizzaPreferiti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void OnClick(int position) {
        filmList.get(position);

        System.out.println(" "+ position);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}