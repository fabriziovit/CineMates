package com.example.cinemates.ui.CineMates;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemates.databinding.ActivitySchedaFilmBinding;

public class SchedaFilmActivity extends AppCompatActivity {
    private ActivitySchedaFilmBinding binding;
    private String titolo;
    private Bitmap poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySchedaFilmBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            titolo = extras.getString("titolo");
            String posterString = extras.getString("poster");
            byte [] encodeByte= Base64.decode(posterString, Base64.DEFAULT);
            poster = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        }
        binding.titoloFIlmSchedaFilm.setText(titolo);
        binding.locandinaFilmSchedaFilm.setImageBitmap(poster);
        
        Keyboard(binding);
        BackButton(binding);
    }

    private void Keyboard(ActivitySchedaFilmBinding binding) {
        binding.containerSchedaFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.containerSchedaFilm.getWindowToken(), 0);
                //binding..clearFocus();
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

}