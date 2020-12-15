package com.example.cinemates.ui.CineMates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.example.cinemates.R;

public class RegistatiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registati);

        final TextView risDisponibilita = findViewById(R.id.risDisponibilita_registrati_TextView);
        final EditText password = findViewById(R.id.password_registrati_TextField);
        final EditText confermaPassword = findViewById(R.id.confermapssw_TextField);
        final TextView errorePassword = findViewById(R.id.ErrorePassword_reg_TextView);
        final EditText username = findViewById(R.id.username_registrati_TextField);
        final EditText email = findViewById(R.id.email_registrati_TextField);
        final TextView disponibilita = findViewById(R.id.Disponibilta_registrati_TextView);
        final Button registratiButton = findViewById(R.id.registrati_button);


        errorePassword.setVisibility(View.INVISIBLE);
        risDisponibilita.setVisibility(View.INVISIBLE);




        confermaPassword.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(confermaPassword.getText().toString().equals(password.getText().toString()));
                    else {
                        errorePassword.setVisibility(View.VISIBLE);
                    }
                }else{
                    errorePassword.setVisibility(View.INVISIBLE);
                }
            }
        });

        username.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    //if(QUERY USERNAME NON ESISTENTE){
                        risDisponibilita.setVisibility(View.VISIBLE);
                        risDisponibilita.setText("Disponibile");
                        risDisponibilita.setTextColor(getResources().getColor(R.color.verdeDis));
                    /*}else{
                        risDisponibilita.setVisibility(View.VISIBLE);
                        risDisponibilita.setText("Non Disponibile");
                        risDisponibilita.setTextColor(getResources().getColor(R.color.rossoDis));
                    }*/
                }else{
                    risDisponibilita.setVisibility(View.INVISIBLE);
                }
            }
        });


        registratiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("HEleoDs");
                if(confermaPassword.getText().toString().equals(password.getText().toString())){
                    Amplify.Auth.signUp(
                            username.getText().toString(),
                            password.getText().toString(),
                            AuthSignUpOptions.builder().userAttribute(AuthUserAttributeKey.email(), email.getText().toString()).build(),
                            result -> Log.i("AuthQuickStart", "Result: " + result.toString()),
                            error -> Log.e("AuthQuickStart", "Sign up failed", error)
                    );
                    startActivity(new Intent(RegistatiActivity.this, ConfermaRegistrazioneActivity.class));
                }
            }
        });

    }

}