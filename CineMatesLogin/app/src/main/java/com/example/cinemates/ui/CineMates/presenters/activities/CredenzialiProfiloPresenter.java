package com.example.cinemates.ui.CineMates.presenters.activities;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.cinemates.R;
import com.example.cinemates.databinding.ActivityCredenzialiProfiloBinding;
import com.example.cinemates.ui.CineMates.views.activities.CredenzialiProfiloActivity;
import com.example.cinemates.ui.CineMates.views.activities.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class CredenzialiProfiloPresenter {
    private final CredenzialiProfiloActivity credenzialiProfiloActivity;
    private FirebaseFirestore db;

    public CredenzialiProfiloPresenter(CredenzialiProfiloActivity credenzialiProfiloActivity, FirebaseFirestore db){
        this.credenzialiProfiloActivity = credenzialiProfiloActivity;
        this.db = db;
    }

    public void resetButton(ActivityCredenzialiProfiloBinding binding) {
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
                        Toast.makeText(credenzialiProfiloActivity, "Username Modificato", Toast.LENGTH_SHORT).show();
                        username = true;
                    }
                }
                if (password || username) {
                    Intent intent = new Intent(credenzialiProfiloActivity, HomeActivity.class);
                    credenzialiProfiloActivity.getActivity().startActivity(intent);
                }else
                    Toast.makeText(credenzialiProfiloActivity, "Controlla i dati! La password deve essere minimo di 6 caratteri!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void ControlloPassword(ActivityCredenzialiProfiloBinding binding) {
        binding.confermaPassCredenzialiEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!binding.confermaPassCredenzialiEditText.getText().toString().equals(binding.passwordCredenzialiEditText.getText().toString()))
                        binding.errorePasswordCredenzialiTextView.setVisibility(View.VISIBLE);
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
                                Toast.makeText(credenzialiProfiloActivity, "Password Modificata", Toast.LENGTH_SHORT).show();
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
                        binding.risDisponibilitaCredenzialiTextView.setTextColor(credenzialiProfiloActivity.getColor(R.color.verdeDis));
                    } else {
                        binding.risDisponibilitaCredenzialiTextView.setVisibility(View.INVISIBLE);
                    }
                } else {
                    if (binding.usernameCredenzialiTextView.getText().toString() != null) {
                        binding.risDisponibilitaCredenzialiTextView.setVisibility(View.VISIBLE);
                        binding.risDisponibilitaCredenzialiTextView.setText("Non Disponibile");
                        binding.risDisponibilitaCredenzialiTextView.setTextColor(credenzialiProfiloActivity.getColor(R.color.rossoDis));
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

    public void controlloUsernameOnFocus(ActivityCredenzialiProfiloBinding binding) {
        binding.usernameCredenzialiTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    checkUsername(binding, binding.usernameCredenzialiTextView.getText().toString());
                }
            }
        });
    }

    public void Keyboard(ActivityCredenzialiProfiloBinding binding) {
        binding.containerCredenziali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) credenzialiProfiloActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(binding.containerCredenziali.getWindowToken(), 0);
                binding.usernameCredenzialiTextView.clearFocus();
                binding.passwordCredenzialiEditText.clearFocus();
                binding.confermaPassCredenzialiEditText.clearFocus();
            }
        });
    }

    public void backbutton(ActivityCredenzialiProfiloBinding binding){
        binding.backCredenzialiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                credenzialiProfiloActivity.onBackPressed();
            }
        });
    }

}
