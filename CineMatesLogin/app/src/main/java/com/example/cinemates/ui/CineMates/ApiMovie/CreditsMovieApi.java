package com.example.cinemates.ui.CineMates.ApiMovie;

import com.example.cinemates.ui.CineMates.ApiMovie.model.CreditsMovie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface CreditsMovieApi {
    @GET
    Call<CreditsMovie> creditsMovie(@Url String url);
}
