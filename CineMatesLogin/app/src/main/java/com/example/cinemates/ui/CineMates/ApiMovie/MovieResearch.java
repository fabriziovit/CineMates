package com.example.cinemates.ui.CineMates.ApiMovie;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class MovieResearch {
    @SerializedName("results")
    ArrayList<Movie> results;

    public MovieResearch(){}

    public MovieResearch(ArrayList<Movie> results) {
        this.results = results;
    }

    public ArrayList<Movie> getResults() {
        return results;
    }

    public void setResults(ArrayList<Movie> results) {
        this.results = results;
    }
}
