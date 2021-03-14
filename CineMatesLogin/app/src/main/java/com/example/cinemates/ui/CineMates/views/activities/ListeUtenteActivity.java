package com.example.cinemates.ui.CineMates.views.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemates.databinding.ActivityListeUtenteBinding;
import com.example.cinemates.ui.CineMates.presenters.activities.ListeUtentePresenter;

public class ListeUtenteActivity extends AppCompatActivity {
    private ActivityListeUtenteBinding binding;
    private ListeUtentePresenter listeUtentePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListeUtenteBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        listeUtentePresenter = new ListeUtentePresenter(this, binding);

        listeUtentePresenter.BackButton(binding);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}