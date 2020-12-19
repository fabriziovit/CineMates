package com.example.cinemates.ui.CineMates;

import android.app.Activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import com.example.cinemates.databinding.ActivityLoginBinding;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();


        RegistratiButton(binding);
        LoginButton(binding);
        PassDimenticata(binding);
        Keyboard(binding);
        
        binding.googleLoginButton.setSize(SignInButton.SIZE_STANDARD);
        binding.passwordDimLoginTextView.setPaintFlags(binding.passwordDimLoginTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.erroreLoginTextView.setVisibility(View.INVISIBLE);



    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void LoginButton(ActivityLoginBinding binding) {
        binding.accediLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Accedi(binding);
            }
        });
    }

    private void Accedi(ActivityLoginBinding binding){
        mAuth.signInWithEmailAndPassword(binding.emailLoginTextField.getText().toString(), binding.passwordLoginTextField.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Accesso Riuscito", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Accesso Non Riuscito", Toast.LENGTH_LONG).show();
                            }
                        }

                });

    }

    private void PassDimenticata(ActivityLoginBinding binding) {
        binding.passwordDimLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, PasswordDimenticataActivity.class));
            }
        });
    }

    private void Keyboard(ActivityLoginBinding binding) {
        binding.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(binding.container.getWindowToken(), 0);
                binding.emailLoginTextField.clearFocus();
                binding.passwordLoginTextField.clearFocus();
            }
        });
    }

    private void RegistratiButton(ActivityLoginBinding binding) {
        binding.registratiLoginTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistratiActivity.class));
            }
        });
    }
}


    /*AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken)
        {
            if(currentAccessToken == null){
                /*txtEmail.setText("");
                txtName.setText("");
                circleImageView.setImageResource(0);
                Toast.makeText(LoginActivity.this, "Utente disconesso", Toast.LENGTH_LONG).show();
            }else {
                loaduserProfile(currentAccessToken);
            }
        }
    };*/
