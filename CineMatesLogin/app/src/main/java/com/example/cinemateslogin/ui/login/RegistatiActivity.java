package com.example.cinemateslogin.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.cinemateslogin.R;

public class RegistatiActivity extends AppCompatActivity {
    TextView risDisponibilita = findViewById(R.id.risDisponibilita_TextView);
    TextView password = findViewById(R.id.password_textView);
    TextView confermaPassword = findViewById(R.id.conferma_password_TextField);
    TextView errorePassword = findViewById(R.id.ErrorePassword_TextView);
    TextView disponibilta = findViewById(R.id.Disponibilta_TextView);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registati);


        errorePassword.setVisibility(View.INVISIBLE);
        risDisponibilita.setVisibility(View.INVISIBLE);
        disponibilta.setVisibility(View.INVISIBLE);
        onActivity();
    }

    protected void onActivity() {
        confermaPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (confermaPassword.getText().toString().equals(password.getText().toString())) {
                    } else {
                        errorePassword.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }
}