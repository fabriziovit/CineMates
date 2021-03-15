package com.example.cinemates.ui.CineMates.views.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemates.databinding.ActivityRegistratiBinding;
import com.example.cinemates.ui.CineMates.presenters.activities.RegistratiPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class RegistratiActivity extends AppCompatActivity {
    private ActivityRegistratiBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private RegistratiPresenter registratiPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistratiBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        registratiPresenter = new RegistratiPresenter(this, mAuth, db);

        registratiPresenter.BackButton(binding);
        registratiPresenter.ControlloPassword(binding);
        registratiPresenter.ControlloUsernameOnFocus(binding);
        registratiPresenter.KeyboardRegistrati(binding);
        registratiPresenter.RegistratiButton(binding);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public Activity getActivity(){
        return this;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}