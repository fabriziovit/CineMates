package com.example.cinemates.ui.CineMates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.cinemates.R;
import com.example.cinemates.databinding.ActivityConfermaRegistrazioneBinding;
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


}