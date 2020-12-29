package com.example.cinemates.ui.CineMates;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemates.R;
import com.example.cinemates.databinding.ActivityRegistratiBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class RegistratiActivity extends AppCompatActivity {
    private ActivityRegistratiBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    UserHelperClass userHelperClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistratiBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userHelperClass = new UserHelperClass();

        ControlloUsername(binding);
        ControlloPassword(binding);

        RegistratiButton(binding);
        KeyboardRegistrati(binding);
        BackButton(binding);
        binding.ErrorePasswordRegTextView.setVisibility(View.INVISIBLE);
        binding.risDisponibilitaRegistratiTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void ControlloPassword(ActivityRegistratiBinding binding) {
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

    public void ControlloUsername(ActivityRegistratiBinding binding) {
        binding.usernameRegistratiTextField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    checkUsername(binding.usernameRegistratiTextField.getText().toString(), binding);
                }
            }
        });
    }


    private void BackButton(ActivityRegistratiBinding binding) {
        binding.backRegistratiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistratiActivity.this, LoginActivity.class));
            }
        });
    }

    private void RegistratiButton(ActivityRegistratiBinding binding) {
        binding.registratiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.usernameRegistratiTextField.isFocused()) {
                    binding.usernameRegistratiTextField.clearFocus();
                }
                if (binding.confermapsswTextField.getText().toString().equals(binding.passwordRegistratiTextField.getText().toString()) && !binding.risDisponibilitaRegistratiTextView.getText().toString().equals("Non Disponibile") && binding.passwordRegistratiTextField.getText().length() >= 6 && binding.usernameRegistratiTextField.getText().length() > 2 && binding.emailRegistratiTextField.getText().toString() != null){
                    Registrati(binding);
                }else
                    Toast.makeText(RegistratiActivity.this, "Controlla i dati inseriti!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void KeyboardRegistrati(ActivityRegistratiBinding binding) {
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

    private void Registrati(ActivityRegistratiBinding binding) {
        mAuth.createUserWithEmailAndPassword(binding.emailRegistratiTextField.getText().toString(), binding.passwordRegistratiTextField.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Get Value from all Field
                            String email = binding.emailRegistratiTextField.getText().toString();
                            String username = binding.usernameRegistratiTextField.getText().toString();
                            DataSet(email, username);
                        } else {
                            Toast.makeText(RegistratiActivity.this, "Registrazione Errata", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void DataSet(String email, String username) {
        FirebaseUser rUser = mAuth.getCurrentUser();
        String uId = rUser.getUid();
        userHelperClass.setEmail(email);
        userHelperClass.setUid(uId);
        userHelperClass.setUsername(username);
        userHelperClass.setImageUrl("default");

        db.collection("users")
                .document(uId).set(userHelperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("FIRESTORE", "Task completato!");
                Toast.makeText(RegistratiActivity.this, "Registrazione Completata", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegistratiActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }


    private void checkUsername(String username, ActivityRegistratiBinding binding) {
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
                    if (binding.usernameRegistratiTextField.getText().toString() != null) {
                        binding.risDisponibilitaRegistratiTextView.setVisibility(View.VISIBLE);
                        binding.risDisponibilitaRegistratiTextView.setText("Disponibile");
                        binding.risDisponibilitaRegistratiTextView.setTextColor(getResources().getColor(R.color.verdeDis));
                    } else {
                        binding.risDisponibilitaRegistratiTextView.setVisibility(View.INVISIBLE);
                    }
                } else {
                    if (binding.usernameRegistratiTextField.getText().toString() != null) {
                        binding.risDisponibilitaRegistratiTextView.setVisibility(View.VISIBLE);
                        binding.risDisponibilitaRegistratiTextView.setText("Non Disponibile");
                        binding.risDisponibilitaRegistratiTextView.setTextColor(getResources().getColor(R.color.rossoDis));
                    } else {
                        binding.risDisponibilitaRegistratiTextView.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }
}