package com.example.cinemates.ui.CineMates;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.IdentityProvider;
import com.amazonaws.mobile.client.SignInUIOptions;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amplifyframework.auth.AuthProvider;
import com.amplifyframework.core.Amplify;
import com.example.cinemates.databinding.ActivityAuthenticatorBinding;

public class AuthenticatorActivity extends Activity {
    private ActivityAuthenticatorBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthenticatorBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        AWSMobileClient.getInstance().initialize(this, new Callback<UserStateDetails>() {
            @Override
            public void onResult(UserStateDetails userStateDetails) {
                Log.i("INIT", String.valueOf(userStateDetails.getUserState()));
                AWSMobileClient.getInstance().showSignIn(
                        AuthenticatorActivity.this,
                        SignInUIOptions.builder().nextActivity(RegistatiActivity.class).build(),
                        new Callback<UserStateDetails>() {
                            @Override
                            public void onResult(UserStateDetails result) {
                                Log.d("Risultato", "onResult: " + result.getUserState());
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e("Errore", "onError: ", e);
                            }
                        });
            }


            @Override
            public void onError(Exception e) {
                Log.e("INIT", "Error during initialization", e);
            }
        });


        AWSMobileClient.getInstance().initialize(this, new Callback<UserStateDetails>() {
            @Override
            public void onResult(UserStateDetails userStateDetails) {
                Log.i("INIT", String.valueOf(userStateDetails.getUserState()));
                AWSMobileClient.getInstance().showSignIn(
                        AuthenticatorActivity.this,
                        SignInUIOptions.builder().nextActivity(RegistatiActivity.class).build(),
                        new Callback<UserStateDetails>() {
                            @Override
                            public void onResult(UserStateDetails result) {
                                Log.d("Risultato", "onResult: " + result.getUserState());
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e("Errore", "onError: ", e);
                            }
                        }
                );
            }

            @Override
            public void onError(Exception e) {
                Log.e("INIT", "Error during initialization", e);
            }
        });

        /*@Override
        public void onSuccess(Bundle response) {
            String token = response.getString("id_token");

            AWSMobileClient.getInstance().federatedSignIn("facebook.com", token, new Callback<UserStateDetails>() {
                @Override
                public void onResult(final UserStateDetails userStateDetails) {
                    startActivity(new Intent(AuthenticatorActivity.this, RegistatiActivity.class));
                }

                @Override
                public void onError(Exception e) {
                    Log.e("Errore", "sign-in error", e);
                }
            });
        }*/

    }




}