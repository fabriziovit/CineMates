package com.example.cinemates.ui.CineMates.views.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemates.databinding.ActivityVisualizzaPreferitiBinding;
import com.example.cinemates.ui.CineMates.model.ItemFilm;
import com.example.cinemates.ui.CineMates.presenters.activities.VisualizzaPreferitiPresenter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import Intefaces.UpdateableFragmentListener;

public class VisualizzaPreferitiActivity extends AppCompatActivity implements UpdateableFragmentListener {
    private ActivityVisualizzaPreferitiBinding binding;
    private String username;
    private FirebaseFirestore db;
    private List<ItemFilm> searchList;
    private VisualizzaPreferitiPresenter visualizzaPreferitiPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVisualizzaPreferitiBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        db = FirebaseFirestore.getInstance();
        binding.searchBarVisualizzaPreferiti.setMovementMethod(null);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
            binding.visualizzaPreferitiTextviewVisualizzaPreferiti.setText("Lista preferiti di "+username);
        }

        visualizzaPreferitiPresenter = new VisualizzaPreferitiPresenter(this, username, db, binding);

        visualizzaPreferitiPresenter.Keyboard(binding);
        visualizzaPreferitiPresenter.HomeButton(binding);
        visualizzaPreferitiPresenter.BackButton(binding);
        visualizzaPreferitiPresenter.SearchButton(binding);
        visualizzaPreferitiPresenter.searchByKeyboard(binding);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public  Activity getActivity(){
        return this;
    }

    @Override
    public void update() {
        binding.filmVisualizzaPreferitiRecycleView.getAdapter().notifyDataSetChanged();
    }
}