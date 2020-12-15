package com.example.cinemates.ui.CineMates;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;
import com.example.cinemates.R;
import com.google.android.material.snackbar.Snackbar;

public class ConfermaRegistrazioneActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conferma_registrazione);

        final EditText codiceEditText = findViewById(R.id.codice_Conferma_TextView);
        final Button confermaButton = findViewById(R.id.conferma_conferma_Button);
        final TextView usernameDisplay = findViewById(R.id.usernameText_Conferma);

        confermaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amplify.Auth.confirmSignUp(
                        //username.setText(Controller.username.getText().toString()+" controlla la tua casella email");
                        "Momba",
                        codiceEditText.getText().toString(),
                        result -> Log.i("AuthQuickstart", result.isSignUpComplete() ? "Confirm signUp succeeded": "Confirm sign up not complete"),
                        error -> Log.e("AuthQuickstart", error.toString())
                );
            }
        });
    }

}