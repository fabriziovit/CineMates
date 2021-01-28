package com.example.cinemates.ui.CineMates;

import android.graphics.Bitmap;

public class ItemRecensione {

    private String username;
    private String recensione;
    private String voto;
    private Bitmap bitmap;

    public ItemRecensione(String username, String recensione, String voto, Bitmap bitmap) {
        this.username = username;
        this.recensione = recensione;
        this.voto = voto;
        this.bitmap = bitmap;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRecensione() {
        return recensione;
    }

    public void setRecensione(String recensione) {
        this.recensione = recensione;
    }

    public String getVoto() {
        return voto;
    }

    public void setVoto(String voto) {
        this.voto = voto;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
