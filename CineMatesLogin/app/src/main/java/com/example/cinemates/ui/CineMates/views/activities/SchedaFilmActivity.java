package com.example.cinemates.ui.CineMates.views.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemates.databinding.ActivitySchedaFilmBinding;
import com.example.cinemates.ui.CineMates.presenters.activities.SchedaFilmPresenter;

import static com.example.cinemates.ui.CineMates.util.Constants.KEY_MOVIE_ID;

public class SchedaFilmActivity extends AppCompatActivity {
    private ActivitySchedaFilmBinding binding;
    private int id;
    private SchedaFilmPresenter schedaFilmPresenter;

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

        schedaFilmPresenter = new SchedaFilmPresenter(this, id, binding);

        schedaFilmPresenter.Keyboard(binding);
        schedaFilmPresenter.BackButton(binding);
        schedaFilmPresenter.TabLayout(binding);
        schedaFilmPresenter.update();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}