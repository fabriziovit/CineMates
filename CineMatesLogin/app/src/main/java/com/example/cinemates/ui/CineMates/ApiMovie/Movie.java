package com.example.cinemates.ui.CineMates.ApiMovie;

import com.google.gson.annotations.SerializedName;

public class Movie {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    //@SerializedName("genre_ids")
    //ArrayList<Integer> genre_ids;
    @SerializedName("poster_path")
    private String poster_path;

    public Movie(){}

    public Movie(int id, String title, String poster_path) {
        this.id = id;
        this.title = title;
        this.poster_path = poster_path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }
}
