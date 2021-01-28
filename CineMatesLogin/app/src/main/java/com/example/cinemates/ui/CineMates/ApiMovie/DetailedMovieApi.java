package com.example.cinemates.ui.CineMates.ApiMovie;

import com.example.cinemates.ui.CineMates.ApiMovie.model.DetailedMovie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface DetailedMovieApi {
    @GET
    Call<DetailedMovie> detailedMovie(@Url String url);
}
