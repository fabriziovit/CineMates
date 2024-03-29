package com.example.cinemates.ui.CineMates.friends.model;

import android.graphics.Bitmap;

public class ItemUser{

    private String Username;
    private Bitmap bitmap;
    private int rapporto; //0 non amici, 1 richiesta di amicizia inviata, 2 amici
    private String uid;

    public ItemUser(){
    }

    public ItemUser(String username, Bitmap profilePic, String uid, int rapporto){
        this.bitmap = profilePic;
        this.Username = username;
        this.uid = uid;
        this.rapporto = rapporto;
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

    public int getRapporto() {
        return rapporto;
    }

    public void setRapporto(int rapporto) {
        this.rapporto = rapporto;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
