package com.example.cinemates.ui.CineMates.presenters.activities;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.cinemates.R;
import com.example.cinemates.databinding.ActivityRegistratiBinding;
import com.example.cinemates.ui.CineMates.friends.model.Friends;
import com.example.cinemates.ui.CineMates.model.UserHelperClass;
import com.example.cinemates.ui.CineMates.views.activities.LoginActivity;
import com.example.cinemates.ui.CineMates.views.activities.RegistratiActivity;
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

import static com.example.cinemates.ui.CineMates.util.Constants.DEFAULT_PROFILE_PIC;

public class RegistratiPresenter {
    private final RegistratiActivity registratiActivity;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private UserHelperClass userHelperClass;

    public RegistratiPresenter(RegistratiActivity registratiActivity, FirebaseAuth mAuth, FirebaseFirestore db) {
        this.registratiActivity = registratiActivity;
        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
    }

    public void ControlloPassword(ActivityRegistratiBinding binding) {
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

    public void ControlloUsernameOnFocus(ActivityRegistratiBinding binding) {
        binding.usernameRegistratiTextField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    checkUsername(binding.usernameRegistratiTextField.getText().toString(), binding);
                }
            }
        });
    }

    public void BackButton(ActivityRegistratiBinding binding) {
        binding.backRegistratiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registratiActivity.onBackPressed();
            }
        });
    }

    public void RegistratiButton(ActivityRegistratiBinding binding) {
        binding.registratiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.usernameRegistratiTextField.isFocused()) {
                    binding.usernameRegistratiTextField.clearFocus();
                }
                if (binding.confermapsswTextField.getText().toString().equals(binding.passwordRegistratiTextField.getText().toString()) && !binding.risDisponibilitaRegistratiTextView.getText().toString().equals("Non Disponibile") && binding.passwordRegistratiTextField.getText().length() >= 6 && binding.usernameRegistratiTextField.getText().length() > 2 && binding.emailRegistratiTextField.getText().toString() != null){
                    Registrati(binding);
                }else
                    Toast.makeText(registratiActivity, "Controlla i dati inseriti!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void KeyboardRegistrati(ActivityRegistratiBinding binding) {
        binding.constraintRegistrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) registratiActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
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
                .addOnCompleteListener(registratiActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String email = binding.emailRegistratiTextField.getText().toString();
                            String username = binding.usernameRegistratiTextField.getText().toString();
                            DataSet(email, username);
                        } else {
                            Toast.makeText(registratiActivity, "Registrazione Errata", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void DataSet(String email, String username) {
        userHelperClass = new UserHelperClass();
        FirebaseUser rUser = mAuth.getCurrentUser();
        String uId = rUser.getUid();
        userHelperClass.setEmail(email);
        userHelperClass.setUid(uId);
        userHelperClass.setUsername(username);
        userHelperClass.setImageUrl(DEFAULT_PROFILE_PIC);

        Friends friends = new Friends();

        db.collection("users")
                .document(uId).set(userHelperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("FIRESTORE", "Task completato!");
                db.collection("friends").document(uId).set(friends).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("FIRESTORE", "Task completato Friends!");
                    }
                });
                Toast.makeText(registratiActivity, "Registrazione Completata", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(registratiActivity, LoginActivity.class);
                registratiActivity.getActivity().startActivity(intent);
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
                    if (binding.usernameRegistratiTextField.getText() != null) {
                        binding.risDisponibilitaRegistratiTextView.setVisibility(View.VISIBLE);
                        binding.risDisponibilitaRegistratiTextView.setText("Disponibile");
                        binding.risDisponibilitaRegistratiTextView.setTextColor(registratiActivity.getColor(R.color.verdeDis));
                    } else {
                        binding.risDisponibilitaRegistratiTextView.setVisibility(View.INVISIBLE);
                    }
                } else {
                    if (binding.usernameRegistratiTextField.getText() != null) {
                        binding.risDisponibilitaRegistratiTextView.setVisibility(View.VISIBLE);
                        binding.risDisponibilitaRegistratiTextView.setText("Non Disponibile");
                        binding.risDisponibilitaRegistratiTextView.setTextColor(registratiActivity.getColor(R.color.rossoDis));
                    } else {
                        binding.risDisponibilitaRegistratiTextView.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

}
