package com.example.cinemates.ui.CineMates;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.example.cinemates.R;
import com.example.cinemates.databinding.ActivityRegistatiBinding;


public class RegistatiActivity extends AppCompatActivity {
    private ActivityRegistatiBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistatiBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ControlloUsername(binding);
        ControlloPassword(binding);
        RegistratiButton(binding);
        KeyboardRegistrati(binding);
        BackButton(binding);

        binding.ErrorePasswordRegTextView.setVisibility(View.INVISIBLE);
        binding.risDisponibilitaRegistratiTextView.setVisibility(View.INVISIBLE);

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
                        Amplify.Auth.signUp(
                                binding.usernameRegistratiTextField.getText().toString(),
                                binding.passwordRegistratiTextField.getText().toString(),
                                AuthSignUpOptions.builder().userAttribute(AuthUserAttributeKey.email(), binding.emailRegistratiTextField.getText().toString()).build(),
                                result -> Log.i("AuthQuickStart", "Result: " + result.toString()),
                                error -> Log.e("AuthQuickStart", "Sign up failed", error)
                        );
                        String usernamevalue = binding.usernameRegistratiTextField.getText().toString();
                        Intent intentConferma = new Intent(RegistatiActivity.this, ConfermaRegistrazioneActivity.class);
                        intentConferma.putExtra(ConfermaRegistrazioneActivity.EXTRA_MAIN_TEXT, usernamevalue);
                        startActivity(intentConferma);
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

}