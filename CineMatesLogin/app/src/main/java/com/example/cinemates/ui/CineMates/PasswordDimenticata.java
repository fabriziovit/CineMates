package com.example.cinemates.ui.CineMates;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cinemates.R;

public class PasswordDimenticata extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_dimenticata);

        final TextView Errore_emailuser = findViewById(R.id.erroruser_textView);

        Errore_emailuser.setVisibility(View.INVISIBLE);
    }
}