package com.example.cinemates.ui.CineMates.ApiMovie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface DetailedMovieApi {
    @GET
    Call<DetailedMovie> detailedMovie(@Url String url);
}
