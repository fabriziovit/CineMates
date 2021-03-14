package com.example.cinemates.ui.CineMates.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemates.databinding.ActivityLoginBinding;
import com.example.cinemates.ui.CineMates.presenters.activities.LoginPresenter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    final static String PREFS_NAME = "AUTH";
    private LoginPresenter loginPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loginPresenter = new LoginPresenter(this, auth, db, binding);

        loginPresenter.registratiButton(binding);
        loginPresenter.onLoginButton(binding);
        loginPresenter.keyListenerLogin(binding);
        loginPresenter.keyboard(binding);
        loginPresenter.googleButton(binding);
        loginPresenter.PassDimenticata(binding);

        binding.googleLoginButton.setSize(SignInButton.SIZE_STANDARD);
        binding.passwordDimLoginTextView.setPaintFlags(binding.passwordDimLoginTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.erroreLoginTextView.setVisibility(View.INVISIBLE);

        FirebaseUser user = auth.getInstance().getCurrentUser();

        if (user != null) {
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(i);
        }

        String token = settings.getString("auth_token", null);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == loginPresenter.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("Accesso", "firebaseAuthWithGoogle:" + account.getId());
                loginPresenter.firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w("Errore", "Google sign in failed", e);
            }
        }
        loginPresenter.mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public Activity getStartActivity(){
        return this;
    }

}
