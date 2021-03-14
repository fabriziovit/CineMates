package com.example.cinemates.ui.CineMates.views.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemates.databinding.ActivityCredenzialiProfiloBinding;
import com.example.cinemates.ui.CineMates.presenters.activities.CredenzialiProfiloPresenter;
import com.google.firebase.firestore.FirebaseFirestore;

public class CredenzialiProfiloActivity extends AppCompatActivity {

    ActivityCredenzialiProfiloBinding binding;
    private FirebaseFirestore db;
    private CredenzialiProfiloPresenter credenzialiProfiloPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCredenzialiProfiloBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        db = FirebaseFirestore.getInstance();

        credenzialiProfiloPresenter = new CredenzialiProfiloPresenter(this, db);

        credenzialiProfiloPresenter.resetButton(binding);
        credenzialiProfiloPresenter.controlloUsernameOnFocus(binding);
        credenzialiProfiloPresenter.Keyboard(binding);
        credenzialiProfiloPresenter.backbutton(binding);
        credenzialiProfiloPresenter.ControlloPassword(binding);
    }

    public Activity getActivity(){
        return this;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}