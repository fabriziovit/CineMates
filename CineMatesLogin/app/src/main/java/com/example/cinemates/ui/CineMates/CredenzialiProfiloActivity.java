package com.example.cinemates.ui.CineMates;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemates.R;
import com.example.cinemates.databinding.ActivityCredenzialiProfiloBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class CredenzialiProfiloActivity extends AppCompatActivity {

    private ActivityCredenzialiProfiloBinding binding;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCredenzialiProfiloBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        db = FirebaseFirestore.getInstance();

        resetButton(binding);
        controlloUsernameOnFocus(binding);
        Keyboard(binding);
        backbutton(binding);
        ControlloPassword(binding);
    }

    private void resetButton(ActivityCredenzialiProfiloBinding binding) {

        binding.resetCredenzialiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean password = false;
                boolean username = false;
                if (binding.confermaPassCredenzialiEditText.isFocused() || binding.passwordCredenzialiEditText.isFocused()) {
                    binding.confermaPassCredenzialiEditText.clearFocus();
                }
                if (binding.confermaPassCredenzialiEditText.getText().toString().equals(binding.passwordCredenzialiEditText.getText().toString()) && binding.passwordCredenzialiEditText.getText().length() >= 6) {
                    aggiornaPassword(binding);
                    password = true;
                }
                if (binding.usernameCredenzialiTextView.getText().length() != 0) {
                    if (binding.usernameCredenzialiTextView.isFocused()) {
                        binding.usernameCredenzialiTextView.clearFocus();
                    }
                    if (!binding.risDisponibilitaCredenzialiTextView.getText().toString().equals("Non Disponibile") && binding.usernameCredenzialiTextView.getText().length() > 2) {
                        aggiornaUsername(binding);
                        Toast.makeText(CredenzialiProfiloActivity.this, "Username Modificato", Toast.LENGTH_SHORT).show();
                        username = true;
                    }
                }
                if (password || username)
                    startActivity(new Intent(CredenzialiProfiloActivity.this, HomeActivity.class));
                else
                    Toast.makeText(CredenzialiProfiloActivity.this, "Controlla i dati! La password deve essere minimo di 6 caratteri!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void ControlloPassword(ActivityCredenzialiProfiloBinding binding) {
        binding.confermaPassCredenzialiEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (binding.confermaPassCredenzialiEditText.getText().toString().equals(binding.passwordCredenzialiEditText.getText().toString()))
                        ;
                    else {
                        binding.errorePasswordCredenzialiTextView.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.errorePasswordCredenzialiTextView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void aggiornaPassword(ActivityCredenzialiProfiloBinding binding) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (binding.passwordCredenzialiEditText.getText().toString().equals(binding.confermaPassCredenzialiEditText.getText().toString())) {
            user.updatePassword(binding.passwordCredenzialiEditText.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                                Toast.makeText(CredenzialiProfiloActivity.this, "Password Modificata", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    private void checkUsername(ActivityCredenzialiProfiloBinding binding, String username) {
        CollectionReference collectionReference = db.collection("users");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int n = 0;
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (username.equals(documentSnapshot.getString("username"))) {
                        n = 1;
                        break;
                    }
                }
                if (n == 0) {
                    if (binding.usernameCredenzialiTextView.getText().toString() != null) {
                        binding.risDisponibilitaCredenzialiTextView.setVisibility(View.VISIBLE);
                        binding.risDisponibilitaCredenzialiTextView.setText("Disponibile");
                        binding.risDisponibilitaCredenzialiTextView.setTextColor(getResources().getColor(R.color.verdeDis));
                    } else {
                        binding.risDisponibilitaCredenzialiTextView.setVisibility(View.INVISIBLE);
                    }
                } else {
                    if (binding.usernameCredenzialiTextView.getText().toString() != null) {
                        binding.risDisponibilitaCredenzialiTextView.setVisibility(View.VISIBLE);
                        binding.risDisponibilitaCredenzialiTextView.setText("Non Disponibile");
                        binding.risDisponibilitaCredenzialiTextView.setTextColor(getResources().getColor(R.color.rossoDis));
                    } else {
                        binding.risDisponibilitaCredenzialiTextView.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

    private void aggiornaUsername(ActivityCredenzialiProfiloBinding binding) {
        db.collection("users").document(FirebaseAuth.getInstance().getUid()).update("username", binding.usernameCredenzialiTextView.getText().toString());
    }

    private void controlloUsernameOnFocus(ActivityCredenzialiProfiloBinding binding) {
        binding.usernameCredenzialiTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    checkUsername(binding, binding.usernameCredenzialiTextView.getText().toString());
                }
            }
        });
    }

    private void Keyboard(ActivityCredenzialiProfiloBinding binding) {
        binding.containerCredenziali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(binding.containerCredenziali.getWindowToken(), 0);
                binding.usernameCredenzialiTextView.clearFocus();
                binding.passwordCredenzialiEditText.clearFocus();
                binding.confermaPassCredenzialiEditText.clearFocus();
            }
        });
    }

    private void backbutton(ActivityCredenzialiProfiloBinding binding){
        binding.backCredenzialiButton.setOnClickListener(new View.OnClickListener() {
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

}