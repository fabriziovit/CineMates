package com.example.cinemates.ui.CineMates.views.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemates.databinding.ActivitySplashBinding;
import com.example.cinemates.ui.CineMates.presenters.activities.SplashPresenter;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    private SplashPresenter splashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        splashPresenter = new SplashPresenter(this);

        splashPresenter.Transizione(binding);
    }

    public Activity getActivity(){
        return this;
    }
}