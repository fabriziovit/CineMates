package com.example.cinemates.ui.CineMates.model;

public class PreferitiModel {

    private String uid;
    private int idFilm;

    public PreferitiModel(String uid, int idFilm) {
        this.uid = uid;
        this.idFilm = idFilm;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(int idFilm) {
        this.idFilm = idFilm;
    }
}
