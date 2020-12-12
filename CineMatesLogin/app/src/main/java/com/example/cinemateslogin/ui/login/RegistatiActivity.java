package com.example.cinemateslogin.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.example.cinemateslogin.R;

public class RegistatiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registati);

        final TextView risDisponibilita = findViewById(R.id.risDisponibilita_TextView);
        final TextView password = findViewById(R.id.password_TextField);
        final TextView confermaPassword = findViewById(R.id.conferma_password_TextField);
        final TextView errorePassword = findViewById(R.id.ErrorePassword_TextView);
        final TextView username = findViewById(R.id.username_TextField);
        final TextView email = findViewById(R.id.email_TextField);
        final TextView disponibilita = findViewById(R.id.Disponibilta_TextView);

        errorePassword.setVisibility(View.INVISIBLE);
        risDisponibilita.setVisibility(View.INVISIBLE);
        disponibilita.setVisibility(View.INVISIBLE);


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
                        disponibilita.setVisibility(View.VISIBLE);
                        risDisponibilita.setVisibility(View.VISIBLE);
                        risDisponibilita.setText("Disponibile");
                        risDisponibilita.setTextColor(getResources().getColor(R.color.verdeDis));
                    /*}else{
                        disponibilita.setVisibility(View.VISIBLE);
                        risDisponibilita.setVisibility(View.VISIBLE);
                        risDisponibilita.setText("Non Disponibile");
                        risDisponibilita.setTextColor(getResources().getColor(R.color.rossoDis));
                    }*/
                }else{
                    risDisponibilita.setVisibility(View.INVISIBLE);
                    disponibilita.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

}