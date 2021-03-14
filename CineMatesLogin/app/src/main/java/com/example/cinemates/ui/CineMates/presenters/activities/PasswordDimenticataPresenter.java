package com.example.cinemates.ui.CineMates.presenters.activities;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.cinemates.databinding.ActivityPasswordDimenticataBinding;
import com.example.cinemates.ui.CineMates.views.activities.LoginActivity;
import com.example.cinemates.ui.CineMates.views.activities.PasswordDimenticataActivity;
import com.example.cinemates.ui.CineMates.views.activities.RegistratiActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordDimenticataPresenter {
    private final PasswordDimenticataActivity passwordDimenticataActivity;

    public PasswordDimenticataPresenter(PasswordDimenticataActivity passwordDimenticataActivity) {
        this.passwordDimenticataActivity = passwordDimenticataActivity;
    }

    public void BackButton(ActivityPasswordDimenticataBinding binding){
        binding.backRecuperaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordDimenticataActivity.onBackPressed();
            }
        });
    }

    public void ResetPasswordButton(ActivityPasswordDimenticataBinding binding){
        binding.recuperaRecuperaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword(binding);
            }
        });
    }

    private void resetPassword(ActivityPasswordDimenticataBinding binding){
        if (binding.emailRecuperaTextField.length() != 0) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String emailAddress = binding.emailRecuperaTextField.getText().toString();

            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("Success", "Email sent.");
                                Intent intent = new Intent(passwordDimenticataActivity, LoginActivity.class);
                                passwordDimenticataActivity.getActivity().startActivity(intent);
                                Toast.makeText(passwordDimenticataActivity, "Email inviata! Controlla la tua email", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(passwordDimenticataActivity, "Controlla i dati inseriti", Toast.LENGTH_LONG).show();
                        }
                    });
        } else
            Toast.makeText(passwordDimenticataActivity, "Inserisci una mail", Toast.LENGTH_LONG).show();
    }

    public void keyListnerRecuperoPassword(ActivityPasswordDimenticataBinding binding){
        binding.emailRecuperaTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    resetPassword(binding);
                }
                return false;
            }
        });
    }

    public void KeyboardPassDimenticata(ActivityPasswordDimenticataBinding binding) {
        binding.containerRecupera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) passwordDimenticataActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(binding.containerRecupera.getWindowToken(), 0);
                binding.emailRecuperaTextField.clearFocus();
            }
        });
    }

    public void RegistratiButton(ActivityPasswordDimenticataBinding binding){
        binding.registratiRecuperaTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(passwordDimenticataActivity,RegistratiActivity.class);
                passwordDimenticataActivity.getActivity().startActivity(intent);
            }
        });
    }
}
