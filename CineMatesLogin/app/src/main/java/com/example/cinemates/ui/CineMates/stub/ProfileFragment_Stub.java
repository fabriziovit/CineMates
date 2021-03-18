package com.example.cinemates.ui.CineMates.stub;

import android.os.Build;
import android.os.StrictMode;

import com.example.cinemates.ui.CineMates.views.fragments.ProfileFragment;

public class ProfileFragment_Stub extends ProfileFragment {

    public boolean getBitmapFromdownloadStub(String source){
        if(Build.VERSION.SDK_INT>9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        if(source == null)
            return false;
        else if(source.equals("https://image.tmdb.org/t/p/w185/8UlWHLMpgZm9bx6QYh0NFoq67TZ.jpeg"))
            return true;
        return false;
    }
}
