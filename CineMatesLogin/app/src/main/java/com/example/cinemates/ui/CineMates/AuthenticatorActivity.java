package com.example.cinemates.ui.CineMates;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.SignInUIOptions;
import com.amazonaws.mobile.client.UserStateDetails;

public class AuthenticatorActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


        /*AWSMobileClient.getInstance().initialize(this, new Callback<UserStateDetails>() {
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
        });*/
    }

}