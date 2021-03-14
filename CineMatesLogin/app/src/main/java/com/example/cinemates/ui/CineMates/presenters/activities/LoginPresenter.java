package com.example.cinemates.ui.CineMates.presenters.activities;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.cinemates.R;
import com.example.cinemates.databinding.ActivityLoginBinding;
import com.example.cinemates.ui.CineMates.friends.model.Friends;
import com.example.cinemates.ui.CineMates.model.UserHelperClass;
import com.example.cinemates.ui.CineMates.views.activities.HomeActivity;
import com.example.cinemates.ui.CineMates.views.activities.LoginActivity;
import com.example.cinemates.ui.CineMates.views.activities.PasswordDimenticataActivity;
import com.example.cinemates.ui.CineMates.views.activities.RegistratiActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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

public class LoginPresenter {
    private final LoginActivity loginActivity;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    public static final int RC_SIGN_IN = 1;
    public GoogleSignInClient mGoogleSignInClient;
    public CallbackManager mCallbackManager;


    public LoginPresenter(LoginActivity loginActivity, FirebaseAuth auth, FirebaseFirestore db, ActivityLoginBinding binding){
        this.loginActivity = loginActivity;
        this.auth = auth;
        this.db = db;

        init(binding);
    }

    private void init(ActivityLoginBinding binding){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(loginActivity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(loginActivity, gso);

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
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Errore", "facebook:onError", error);
                Toast.makeText(loginActivity, "Accesso Non Riuscito", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onLoginButton(ActivityLoginBinding binding){
        binding.accediLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.emailLoginTextField.getText().length() != 0 && binding.passwordLoginTextField.getText().length() != 0)
                    Accedi(binding);
                else
                    Toast.makeText(loginActivity, "Inserisci email e password per accedere", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void registratiButton(ActivityLoginBinding binding){
        binding.registratiLoginTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginActivity, RegistratiActivity.class);
                loginActivity.getStartActivity().startActivity(intent);
            }
        });
    }

    public void Accedi(ActivityLoginBinding binding){
        auth.signInWithEmailAndPassword(binding.emailLoginTextField.getText().toString(), binding.passwordLoginTextField.getText().toString())
                .addOnCompleteListener(loginActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(loginActivity, "Accesso Riuscito", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(loginActivity, HomeActivity.class);
                            loginActivity.getStartActivity().startActivity(intent);
                            loginActivity.finish();
                        } else {
                            Toast.makeText(loginActivity, "Accesso Non Riuscito, Controlla i dati!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void keyListenerLogin(ActivityLoginBinding binding){
        binding.passwordLoginTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    binding.container.clearFocus();
                    if (binding.emailLoginTextField.getText().length() != 0 && binding.passwordLoginTextField.getText().length() != 0) {
                        InputMethodManager inputMethodManager = (InputMethodManager) loginActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(binding.container.getWindowToken(), 0);
                        Accedi(binding);
                    } else
                        Toast.makeText(loginActivity, "Inserisci email e password per accedere", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    public void keyboard(ActivityLoginBinding binding) {
        binding.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) loginActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(binding.container.getWindowToken(), 0);
                binding.emailLoginTextField.clearFocus();
                binding.passwordLoginTextField.clearFocus();
            }
        });
    }

    public void googleButton(ActivityLoginBinding binding){
        binding.googleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        loginActivity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(loginActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Accesso", "signInWithCredential:success");
                            //controllo se già esiste e se non esiste aggiungo al db
                            String uid = auth.getCurrentUser().getUid();
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

                                    if (n == 1) {
                                        Toast.makeText(loginActivity, "Accesso Riuscito", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(loginActivity, HomeActivity.class);
                                        loginActivity.getStartActivity().startActivity(intent);
                                    } else {
                                        FirebaseUser user = auth.getCurrentUser();
                                        if (user != null) {
                                            String uid = auth.getCurrentUser().getUid();
                                            String username = "";
                                            String email = "";
                                            String photoUrl = null;
                                            final Integer random = new Random().nextInt(2000);
                                            String numero = random.toString();
                                            for (UserInfo profile : user.getProviderData()) {
                                                photoUrl = profile.getPhotoUrl().toString();
                                                username = profile.getDisplayName().replace(" ", ".") + numero;
                                                email = profile.getEmail();
                                            }
                                            UserHelperClass userHelperClass = new UserHelperClass();
                                            userHelperClass.setEmail(email);
                                            userHelperClass.setUid(uid);
                                            userHelperClass.setImageUrl(photoUrl);
                                            userHelperClass.setUsername(username);

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
                                                    Toast.makeText(loginActivity, "Registrazione Completata", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(loginActivity, HomeActivity.class);
                                                    loginActivity.getStartActivity().startActivity(intent);
                                                }
                                            });

                                        }
                                    }
                                }
                            });
                        } else {
                            Log.w("Errore", "signInWithCredential:failure", task.getException());
                            Toast.makeText(loginActivity, "Accesso Non Riuscito", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void PassDimenticata(ActivityLoginBinding binding) {
        binding.passwordDimLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginActivity, PasswordDimenticataActivity.class);
                loginActivity.getStartActivity().startActivity(intent);
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("Token", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(loginActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Accesso", "signInWithCredential:success");
                            String uid = auth.getCurrentUser().getUid();
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
                                        Toast.makeText(loginActivity, "Accesso Riuscito", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(loginActivity, HomeActivity.class);
                                        loginActivity.getStartActivity().startActivity(intent);
                                    }else{
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        if (user != null) {
                                            String uid = auth.getCurrentUser().getUid();
                                            String username = "";
                                            String email = "";
                                            String photoUrl = null;
                                            final Integer random = new Random().nextInt(2000);
                                            String numero = random.toString();
                                            for (UserInfo profile : user.getProviderData()) {
                                                photoUrl = profile.getPhotoUrl().toString();
                                                username = profile.getDisplayName().replace(" ", ".")+numero;
                                                email = profile.getEmail();
                                            }
                                            UserHelperClass userHelperClass = new UserHelperClass();
                                            userHelperClass.setEmail(email);
                                            userHelperClass.setUid(uid);
                                            userHelperClass.setImageUrl(photoUrl);
                                            userHelperClass.setUsername(username);

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
                                                    Toast.makeText(loginActivity, "Registrazione Completata", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(loginActivity, HomeActivity.class);
                                                    loginActivity.getStartActivity().startActivity(intent);
                                                }
                                            });
                                        }
                                    }
                                }
                            });
                        } else {
                            Log.w("Errore", "signInWithCredential:failure", task.getException());
                            Toast.makeText(loginActivity, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
