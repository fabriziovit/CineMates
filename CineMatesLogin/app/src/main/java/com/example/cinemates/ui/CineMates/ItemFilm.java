package com.example.cinemates.ui.CineMates;

import android.graphics.Bitmap;

public class ItemFilm {

    private String nome;
    private String anno;
    private String genere;
    private String regista;
    private String voto;
    private Bitmap bitmap;


    public ItemFilm(String nome, String anno, String genere, String regista, String voto, Bitmap filmPic) {
        this.nome = nome;
        this.anno = anno;
        this.genere = genere;
        this.regista = regista;
        this.voto = voto;
        this.bitmap = filmPic;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
}
