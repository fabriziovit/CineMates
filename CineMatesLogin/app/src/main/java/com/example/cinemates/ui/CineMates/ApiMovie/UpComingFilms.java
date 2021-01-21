package com.example.cinemates.ui.CineMates.ApiMovie;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class UpComingFilms {
    @SerializedName("results")
    ArrayList<Movie> results;

    public UpComingFilms(){}

    public UpComingFilms(ArrayList<Movie> results) {
        this.results = results;
    }

    public ArrayList<Movie> getResults() {
        return results;
    }

    public void setResults(ArrayList<Movie> results) {
        this.results = results;
    }
}
