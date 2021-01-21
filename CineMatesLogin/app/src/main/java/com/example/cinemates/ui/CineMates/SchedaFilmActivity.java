package com.example.cinemates.ui.CineMates;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemates.databinding.ActivitySchedaFilmBinding;

public class SchedaFilmActivity extends AppCompatActivity {
    ActivitySchedaFilmBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySchedaFilmBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


    }
}