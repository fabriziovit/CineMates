package com.example.cinemates.ui.CineMates.ApiMovie;

import com.google.gson.annotations.SerializedName;

public class Genere {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String nome;

    public Genere(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
