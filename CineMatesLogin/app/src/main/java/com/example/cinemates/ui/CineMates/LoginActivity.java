package com.example.cinemates.ui.CineMates;

import android.app.Activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
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
        //updateUI(currentUser);
    }

    /*private void updateUI(FirebaseUser user) {
        hideProgressBar();
        if (user != null) {
            mBinding.status.setText(getString(R.string.emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified()));
            mBinding.detail.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            mBinding.emailPasswordButtons.setVisibility(View.GONE);
            mBinding.emailPasswordFields.setVisibility(View.GONE);
            mBinding.signedInButtons.setVisibility(View.VISIBLE);

            if (user.isEmailVerified()) {
                mBinding.verifyEmailButton.setVisibility(View.GONE);
            } else {
                mBinding.verifyEmailButton.setVisibility(View.VISIBLE);
            }
        } else {
            mBinding.status.setText(R.string.signed_out);
            mBinding.detail.setText(null);

            mBinding.emailPasswordButtons.setVisibility(View.VISIBLE);
            mBinding.emailPasswordFields.setVisibility(View.VISIBLE);
            mBinding.signedInButtons.setVisibility(View.GONE);
        }
    }*/






        /*GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        callbackManager = CallbackManager.Factory.create();

        //Password ed email errati mostra scritta errorePassEmail
        /*if(email && password != db.email && db.password){
            errorePassEmail.setVisibility(View.VISIBLE);
        }*/



        /*googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(LoginActivity.this, "Hello", Toast.LENGTH_LONG).show();
                switch (view.getId()) {
                    case R.id.google_login_button:
                        signIn();
                        break;
                    // ...
                }
            }
        });

        facebookLogin.registerCallback(callbackManager, new FacebookCallback<com.facebook.login.LoginResult>() {
            @Override
            public void onSuccess(com.facebook.login.LoginResult loginResult)
            {
                System.out.println("OK");
            }

            @Override
            public void onCancel()
            {
                System.out.println("Cancellato");
            }

            @Override
            public void onError(FacebookException error)
            {
                System.out.println("Non Funzionante");
            }
        });*/

    private void LoginButton(ActivityLoginBinding binding) {
        binding.accediLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signInWithEmailAndPassword(binding.usernameLoginTextField.getText().toString(), binding.passwordLoginTextField.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("Successo", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("Errore", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                    // ...
                                }

                                // ...
                            }
                        });

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

    private void RegistratiButton(ActivityLoginBinding binding) {
        binding.registratiLoginTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistatiActivity.class));
            }
        });
    }


    private void Keyboard(ActivityLoginBinding binding) {
        binding.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(binding.container.getWindowToken(), 0);
                binding.usernameLoginTextField.clearFocus();
                binding.passwordLoginTextField.clearFocus();
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

   /* private void loaduserProfile(AccessToken newAccessToken)
    {
        final GraphRequest request =  GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response)
            {
                try {
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");
                    String image_url = "https://graph.facebook.com/"+id+"/picture?type=normal";

                    /*
                    txtEmail.setText(email);
                    txtName.setText(first_name+" "+last_name);
                    RequestOption requestOption = new RequestOption();
                    requestOption.dontAnimate();
                    Glide.with(LoginActivity.this).load(image_url).into(circleImageView);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parametres = new Bundle();
        parametres.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parametres);
        request.executeAsync();
    }*/


   /*
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }*/


   /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.

            Intent intent = new Intent(LoginActivity.this, RegistatiActivity.class);
            startActivity(intent);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Errore", "signInResult:failed code=" + e.getStatusCode());
        }
    }
    */

    /*private void updateUi(GoogleSignInAccount model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }*/