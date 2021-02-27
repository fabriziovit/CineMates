package com.example.cinemates.ui.CineMates.model;

import android.graphics.Bitmap;

public class ItemRecensione {

    private String username;
    private String recensione;
    private int voto;
    private Bitmap bitmap;
    private String uid;
    private boolean proprietario;

    public ItemRecensione(String username, String recensione, int voto, Bitmap bitmap, String uid) {
        this.username = username;
        this.recensione = recensione;
        this.voto = voto;
        this.bitmap = bitmap;
        this.uid = uid;
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

    public int getVoto() {
        return voto;
    }

    public void setVoto(int voto) {
        this.voto = voto;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isProprietario() {
        return proprietario;
    }

    public void setProprietario(boolean proprietario) {
        this.proprietario = proprietario;
    }
}
