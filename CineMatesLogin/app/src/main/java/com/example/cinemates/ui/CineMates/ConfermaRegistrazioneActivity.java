package com.example.cinemates.ui.CineMates;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;
import com.example.cinemates.R;
import com.example.cinemates.databinding.ActivityConfermaRegistrazioneBinding;
import com.example.cinemates.databinding.ActivityLoginBinding;
import com.example.cinemates.databinding.ActivityPasswordDimenticataBinding;
import com.example.cinemates.databinding.ActivityRegistatiBinding;

public class ConfermaRegistrazioneActivity extends AppCompatActivity {

    private ActivityConfermaRegistrazioneBinding binding;
    public static final String EXTRA_MAIN_TEXT = "EXTRA_MAIN_TEXT";
    String usernameExtra = " ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfermaRegistrazioneBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ConfermaButton(binding);
        BackButton(binding);
        KeyboardConferma(binding);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            usernameExtra = bundle.getString(EXTRA_MAIN_TEXT);
        binding.usernameTextConferma.setText(usernameExtra +" controlla la tua casella email");


    }


        private void ConfermaButton(ActivityConfermaRegistrazioneBinding binding) {
            binding.confermaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Amplify.Auth.confirmSignUp(
                            usernameExtra,
                            binding.codiceConfermaTextView.getText().toString(),
                            result -> Log.i("AuthQuickstart", result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete"),
                            error -> Log.e("AuthQuickstart", error.toString())
                    );
                }
            });
        }

    private void BackButton(ActivityConfermaRegistrazioneBinding binding){
        binding.backConfermaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfermaRegistrazioneActivity.this, RegistatiActivity.class));
            }
        });
    }

    private void KeyboardConferma(ActivityConfermaRegistrazioneBinding binding) {
        binding.containerConferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(binding.containerConferma.getWindowToken(), 0);
            }
        });
    }

        //   Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
}