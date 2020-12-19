package com.example.cinemates.ui.CineMates;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import com.example.cinemates.R;
import com.example.cinemates.databinding.ActivityRegistatiBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class RegistatiActivity extends AppCompatActivity {
    private ActivityRegistatiBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistatiBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();

        ControlloUsername(binding);
        ControlloPassword(binding);
        RegistratiButton(binding);
        KeyboardRegistrati(binding);
        BackButton(binding);
        binding.ErrorePasswordRegTextView.setVisibility(View.INVISIBLE);
        binding.risDisponibilitaRegistratiTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

        private void ControlloPassword(ActivityRegistatiBinding binding){
            binding.confermapsswTextField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        if (binding.confermapsswTextField.getText().toString().equals(binding.passwordRegistratiTextField.getText().toString()))
                            ;
                        else {
                            binding.ErrorePasswordRegTextView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        binding.ErrorePasswordRegTextView.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }

        private void ControlloUsername(ActivityRegistatiBinding binding) {
            binding.usernameRegistratiTextField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        if (binding.usernameRegistratiTextField.getText().toString() != null/* && QUERY USERNAME NON ESISTENTE*/) {
                            binding.risDisponibilitaRegistratiTextView.setVisibility(View.VISIBLE);
                            binding.risDisponibilitaRegistratiTextView.setText("Disponibile");
                            binding.risDisponibilitaRegistratiTextView.setTextColor(getResources().getColor(R.color.verdeDis));
                    /*}else{
                        binding.risDisponibilitaRegistratiTextView.setVisibility(View.VISIBLE);
                        binding.risDisponibilitaRegistratiTextView.setText("Non Disponibile");
                        binding.risDisponibilitaRegistratiTextView.setTextColor(getResources().getColor(R.color.rossoDis));
                    }*/
                        } else {
                            binding.risDisponibilitaRegistratiTextView.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            });
        }

        private void BackButton(ActivityRegistatiBinding binding){
            binding.backRegistratiButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(RegistatiActivity.this, LoginActivity.class));
                }
            });
        }

        private void RegistratiButton(ActivityRegistatiBinding binding){
            binding.registratiButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (binding.confermapsswTextField.getText().toString().equals(binding.passwordRegistratiTextField.getText().toString())) {
                       Registrati(binding);
                    }
                }
            });
        }



    private void KeyboardRegistrati(ActivityRegistatiBinding binding){
        binding.constraintRegistrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.constraintRegistrati.getWindowToken(), 0);
                binding.usernameRegistratiTextField.clearFocus();
                binding.passwordRegistratiTextField.clearFocus();
                binding.emailRegistratiTextField.clearFocus();
                binding.confermapsswTextField.clearFocus();
            }
        });
    }

    private void Registrati(ActivityRegistatiBinding binding){
        mAuth.createUserWithEmailAndPassword(binding.emailRegistratiTextField.getText().toString(), binding.passwordRegistratiTextField.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegistatiActivity.this, "Registrazione Completata", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegistatiActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(RegistatiActivity.this, "Registrazione Errata", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }




}