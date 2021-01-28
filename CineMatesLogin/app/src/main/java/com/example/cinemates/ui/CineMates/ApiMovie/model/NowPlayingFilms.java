package com.example.cinemates.ui.CineMates.ApiMovie.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NowPlayingFilms {
    @SerializedName("results")
    ArrayList<Movie> results;

    public NowPlayingFilms(){}

    public NowPlayingFilms(ArrayList<Movie> results) {
        this.results = results;
    }

    public ArrayList<Movie> getResults() {
        return results;
    }

    public void setResults(ArrayList<Movie> results) {
        this.results = results;
    }
}
