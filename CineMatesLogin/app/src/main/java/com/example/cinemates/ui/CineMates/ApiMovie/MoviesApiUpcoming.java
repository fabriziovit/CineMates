package com.example.cinemates.ui.CineMates.ApiMovie;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MoviesApiUpcoming {

    @GET("3/movie/upcoming?api_key=03941baf012eb2cd38196f9df8751df6")
    Call<UpComingFilms> movieList();
}
