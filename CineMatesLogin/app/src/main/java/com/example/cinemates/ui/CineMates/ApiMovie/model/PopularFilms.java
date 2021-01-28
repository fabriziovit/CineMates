package com.example.cinemates.ui.CineMates.ApiMovie.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PopularFilms {
    @SerializedName("results")
    ArrayList<Movie> results;

    public PopularFilms(){}

    public PopularFilms(ArrayList<Movie> results) {
        this.results = results;
    }

    public ArrayList<Movie> getResults() {
        return results;
    }

    public void setResults(ArrayList<Movie> results) {
        this.results = results;
    }
}
