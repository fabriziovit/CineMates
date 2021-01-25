package com.example.cinemates.ui.CineMates.ApiMovie;

import com.google.gson.annotations.SerializedName;

public class Crew {

    @SerializedName("name")
    private String name;
    @SerializedName("job")
    private String job;

    public Crew(String name, String job) {
        this.name = name;
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
