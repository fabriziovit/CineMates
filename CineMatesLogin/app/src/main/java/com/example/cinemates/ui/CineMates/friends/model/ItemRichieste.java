package com.example.cinemates.ui.CineMates.friends.model;

import android.graphics.Bitmap;

public class ItemRichieste {

    private String Username;
    private Bitmap bitmap;

    public ItemRichieste(){
    }

    public ItemRichieste(String username, Bitmap profilePic){
        this.bitmap = profilePic;
        this.Username = username;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
