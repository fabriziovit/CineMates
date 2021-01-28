package com.example.cinemates.ui.CineMates.model;

import android.graphics.Bitmap;

public class ItemFilm {

    private int id;
    private String titolo;
    private String anno;
    private String genere;
    private String regista;
    private String voto;
    private Bitmap bitmap;


    public ItemFilm(String titolo, String anno, String genere, String regista, String voto, Bitmap filmPic) {
        this.titolo = titolo;
        this.anno = anno;
        this.genere = genere;
        this.regista = regista;
        this.voto = voto;
        this.bitmap = filmPic;
    }

    public ItemFilm(String titolo,  Bitmap filmPic, int id) {
        this.titolo = titolo;
        this.bitmap = filmPic;
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setNome(String nome) {
        this.titolo = nome;
    }

    public String getAnno() {
        return anno;
    }

    public void setAnno(String anno) {
        this.anno = anno;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public String getRegista() {
        return regista;
    }

    public void setRegista(String regista) {
        this.regista = regista;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }
}
