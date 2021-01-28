package com.example.cinemates.ui.CineMates.ApiMovie.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CreditsMovie {

    @SerializedName("crew")
    private ArrayList<Crew> crew;

    public CreditsMovie(ArrayList<Crew> crew) {
        this.crew = crew;
    }

    public ArrayList<Crew> getCrew() {
        return crew;
    }

    public void setCrew(ArrayList<Crew> crew) {
        this.crew = crew;
    }
}
