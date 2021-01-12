package com.example.cinemates.ui.CineMates;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemates.databinding.ActivityPasswordDimenticataBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordDimenticataActivity extends AppCompatActivity {

    private ActivityPasswordDimenticataBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPasswordDimenticataBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        BackButton(binding);
        RegistratiButton(binding);
        KeyboardPassDimenticata(binding);
        ResetPassword(binding);

    }

    private void BackButton(ActivityPasswordDimenticataBinding binding){
        binding.backRecuperaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void ResetPassword(ActivityPasswordDimenticataBinding binding){
        binding.recuperaRecuperaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = binding.emailRecuperaTextField.getText().toString();

                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Success", "Email sent.");
                                    startActivity(new Intent(PasswordDimenticataActivity.this, LoginActivity.class));
                                    Toast.makeText(PasswordDimenticataActivity.this, "Email inviata! Controlla la tua email", Toast.LENGTH_SHORT).show();
                                }else
                                    Toast.makeText(PasswordDimenticataActivity.this, "Controlla i dati inseriti", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    private void KeyboardPassDimenticata(ActivityPasswordDimenticataBinding binding) {
        binding.containerRecupera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(binding.containerRecupera.getWindowToken(), 0);
                binding.emailRecuperaTextField.clearFocus();
            }
        });
    }

    private void RegistratiButton(ActivityPasswordDimenticataBinding binding){
        binding.registratiRecuperaTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PasswordDimenticataActivity.this, RegistratiActivity.class));
            }
        });
    }


}