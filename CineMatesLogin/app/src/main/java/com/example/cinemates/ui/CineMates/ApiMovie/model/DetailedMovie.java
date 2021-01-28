package com.example.cinemates.ui.CineMates.ApiMovie.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DetailedMovie {

    @SerializedName("id")
    private int id;
    @SerializedName("poster_path")
    private String poster_path;
    @SerializedName("title")
    private String title;
    @SerializedName("genres")
    private ArrayList<Genere> genere;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String release_date;


    public DetailedMovie(int id, String poster_path, String title, ArrayList<Genere> genere, String overview, String release_date) {
        this.id = id;
        this.poster_path = poster_path;
        this.title = title;
        this.genere = genere;
        this.overview = overview;
        this.release_date = release_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Genere> getGenere() {
        return genere;
    }

    public void setGenere(ArrayList<Genere> genere) {
        this.genere = genere;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
}
