package com.example.cinemates.ui.CineMates;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemates.R;
import com.example.cinemates.databinding.ActivityLoginBinding;
import com.example.cinemates.ui.CineMates.friends.Friends;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Random;


public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;
    FirebaseFirestore db;
    final static String PREFS_NAME = "AUTH";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        db = FirebaseFirestore.getInstance();

        RegistratiButton(binding);
        LoginButton(binding);
        PassDimenticata(binding);
        Keyboard(binding);
        googleButton(binding);

        binding.googleLoginButton.setSize(SignInButton.SIZE_STANDARD);
        binding.passwordDimLoginTextView.setPaintFlags(binding.passwordDimLoginTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.erroreLoginTextView.setVisibility(View.INVISIBLE);

        String token = settings.getString("auth_token", null);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton fbloginButton = binding.fbLoginButton;
        fbloginButton.setReadPermissions("email", "public_profile");
        fbloginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Accesso", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("Cancellato", "facebook:onCancel");
                // [START_EXCLUDE]
                Toast.makeText(LoginActivity.this, "Accesso Cancellato", Toast.LENGTH_LONG).show();
                // [END_EXCLUDE]
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Errore", "facebook:onError", error);
                // [START_EXCLUDE]
                Toast.makeText(LoginActivity.this, "Accesso Non Riuscito", Toast.LENGTH_LONG).show();
                // [END_EXCLUDE]
            }
        });
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
                if (binding.emailLoginTextField.getText().length() != 0 && binding.passwordLoginTextField.getText().length() != 0)
                    Accedi(binding);
                else
                    Toast.makeText(LoginActivity.this, "Inserisci email e password per accedere", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void googleButton(ActivityLoginBinding binding){
        binding.googleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("Accesso", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Errore", "Google sign in failed", e);
                // ...
            }
        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Accesso", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //controllo se già esiste e se non esiste aggiungo al db
                            String uid = user.getUid();
                            CollectionReference collectionReference = db.collection("users");
                            collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    int n = 0;
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        if (uid.equals(documentSnapshot.getString("uid"))) {
                                            n = 1;
                                            break;
                                        }
                                    }
                                    if(n == 1){
                                        Toast.makeText(LoginActivity.this, "Accesso Riuscito", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    }else{
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        if (user != null) {
                                            String uid = user.getUid();
                                            String username = "";
                                            String email = "";
                                            String photoUrl = null;
                                            final Integer random = new Random().nextInt(140);
                                            String numero = random.toString();
                                            for (UserInfo profile : user.getProviderData()) {
                                                // Id of the provider (ex: google.com)
                                                String providerId = profile.getProviderId();
                                                photoUrl = profile.getPhotoUrl().toString();
                                                // Name, email address, and profile photo Url
                                                username = profile.getDisplayName().replace(" ", ".")+numero;
                                                email = profile.getEmail();
                                            }
                                            UserHelperClass userHelperClass = new UserHelperClass();
                                            userHelperClass.setEmail(email);
                                            userHelperClass.setUid(uid);
                                            userHelperClass.setImageUrl(photoUrl);
                                            userHelperClass.setUsername(username+numero);

                                            Friends friends = new Friends();

                                            db.collection("users")
                                                    .document(uid).set(userHelperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Log.d("FIRESTORE", "Task completato!");
                                                    db.collection("friends").document(uid).set(friends).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Log.d("FIRESTORE", "Task completato!");
                                                        }
                                                    });
                                                    Toast.makeText(LoginActivity.this, "Registrazione Completata", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                    startActivity(intent);
                                                }
                                            });

                                        }
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Errore", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Accesso Non Riuscito", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("Token", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Accesso", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();
                            CollectionReference collectionReference = db.collection("users");
                            collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    int n = 0;
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        if (uid.equals(documentSnapshot.getString("uid"))) {
                                            n = 1;
                                            break;
                                        }
                                    }

                                    if(n == 1){
                                        Toast.makeText(LoginActivity.this, "Accesso Riuscito", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    }else{
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        if (user != null) {
                                            String uid = user.getUid();
                                            String username = "";
                                            String email = "";
                                            String photoUrl = null;
                                            final Integer random = new Random().nextInt(140);
                                            String numero = random.toString();
                                            for (UserInfo profile : user.getProviderData()) {
                                                // Id of the provider (ex: google.com)
                                                String providerId = profile.getProviderId();
                                                photoUrl = profile.getPhotoUrl().toString();
                                                // Name, email address, and profile photo Url
                                                username = profile.getDisplayName().replace(" ", ".")+numero;
                                                email = profile.getEmail();
                                            }
                                            UserHelperClass userHelperClass = new UserHelperClass();
                                            userHelperClass.setEmail(email);
                                            userHelperClass.setUid(uid);
                                            userHelperClass.setImageUrl(photoUrl);
                                            userHelperClass.setUsername(username+numero);

                                            Friends friends = new Friends();

                                            db.collection("users")
                                                    .document(uid).set(userHelperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Log.d("FIRESTORE", "Task completato!");
                                                    db.collection("friends").document(uid).set(friends).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Log.d("FIRESTORE", "Task completato Friends!");
                                                        }
                                                    });
                                                    Toast.makeText(LoginActivity.this, "Registrazione Completata", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                    startActivity(intent);
                                                }
                                            });
                                        }
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Errore", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void Accedi(ActivityLoginBinding binding){
        mAuth.signInWithEmailAndPassword(binding.emailLoginTextField.getText().toString(), binding.passwordLoginTextField.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Accesso Riuscito", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Accesso Non Riuscito, Controlla i dati!", Toast.LENGTH_SHORT).show();
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
