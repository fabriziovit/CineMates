package com.example.cinemates.ui.CineMates.views.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemates.databinding.ActivityPasswordDimenticataBinding;
import com.example.cinemates.ui.CineMates.presenters.activities.PasswordDimenticataPresenter;

public class PasswordDimenticataActivity extends AppCompatActivity {

    private ActivityPasswordDimenticataBinding binding;
    private PasswordDimenticataPresenter passwordDimenticataPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPasswordDimenticataBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        passwordDimenticataPresenter = new PasswordDimenticataPresenter(this);

        passwordDimenticataPresenter.BackButton(binding);
        passwordDimenticataPresenter.RegistratiButton(binding);
        passwordDimenticataPresenter.KeyboardPassDimenticata(binding);
        passwordDimenticataPresenter.ResetPasswordButton(binding);
        passwordDimenticataPresenter.keyListnerRecuperoPassword(binding);
    }

    public Activity getActivity(){
        return this;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}