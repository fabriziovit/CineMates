package com.example.cinemates.ui.CineMates;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.cinemates.databinding.ActivityPasswordDimenticataBinding;
import com.example.cinemates.databinding.ActivityResetPasswordBinding;

public class ResetPasswordActivity extends AppCompatActivity {
    private ActivityResetPasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        backButton(binding);
        //resetPasswordButton(binding);
        keyboardReset(binding);

    }

    /*private void resetPasswordButton(ActivityResetPasswordBinding binding){
        binding.resetResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.passwordResetEditText.getText().toString().equals(binding.confermaPassResetEditText.getText().toString())) {
                    Amplify.Auth.confirmResetPassword(
                            binding.passwordResetEditText.getText().toString(),
                            binding.codiceResetEditText.getText().toString(),
                            () -> Log.i("AuthQuickstart", "New password confirmed"),
                            error -> Log.e("AuthQuickstart", error.toString())
                    );
                }
                //Tornare al login se il risultato è corretto
            }
        });
    }*/

    private void backButton(ActivityResetPasswordBinding binding){
        binding.backResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
            }
        });
    }

    private void keyboardReset(ActivityResetPasswordBinding binding){
        binding.containerReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(binding.containerReset.getWindowToken(), 0);
                binding.passwordResetEditText.clearFocus();
                binding.confermaPassResetEditText.clearFocus();
            }
        });
    }

}