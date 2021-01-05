package com.example.cinemates.ui.CineMates.friends;

import android.graphics.Bitmap;

public class ItemUser {

    private String Username;
    private Bitmap bitmap;

    public ItemUser(){
    }

    public ItemUser(String username, Bitmap profilePic){
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
