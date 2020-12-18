package com.example.cinemates.ui.CineMates;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.amplifyframework.core.Amplify;
import com.example.cinemates.databinding.ActivityPasswordDimenticataBinding;

public class PasswordDimenticataActivity extends AppCompatActivity {

    private ActivityPasswordDimenticataBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPasswordDimenticataBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        BackButton(binding);
        KeyboardPassDimenticata(binding);
        ResetPassword(binding);

        binding.erroruserRecuperaTextView.setVisibility(View.INVISIBLE);
    }

    private void BackButton(ActivityPasswordDimenticataBinding binding){
        binding.backRecuperaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PasswordDimenticataActivity.this, LoginActivity.class));
            }
        });
    }

    private void ResetPassword(ActivityPasswordDimenticataBinding binding){
        binding.recuperaRecuperaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amplify.Auth.resetPassword(
                        binding.usernameRecuperaTextField.getText().toString(),
                        result -> Log.i("AuthQuickstart", result.toString()),
                        error -> Log.e("AuthQuickstart", error.toString())
                );
                startActivity(new Intent(PasswordDimenticataActivity.this, ResetPasswordActivity.class));
            }
        });
    }


    private void KeyboardPassDimenticata(ActivityPasswordDimenticataBinding binding) {
        binding.constraintRecupera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(binding.constraintRecupera.getWindowToken(), 0);
                binding.emailRecuperaTextField.clearFocus();
                binding.usernameRecuperaTextField.clearFocus();
            }
        });
    }


}