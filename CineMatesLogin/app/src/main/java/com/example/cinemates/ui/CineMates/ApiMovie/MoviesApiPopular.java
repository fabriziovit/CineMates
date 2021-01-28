package com.example.cinemates.ui.CineMates.ApiMovie;

import com.example.cinemates.ui.CineMates.ApiMovie.model.PopularFilms;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MoviesApiPopular {
    @GET("3/movie/popular?api_key=03941baf012eb2cd38196f9df8751df6")
    Call<PopularFilms> movieList();
}
