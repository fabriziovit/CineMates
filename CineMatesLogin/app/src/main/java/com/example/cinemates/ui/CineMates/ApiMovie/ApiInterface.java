package com.example.cinemates.ui.CineMates.ApiMovie;

import com.example.cinemates.ui.CineMates.ApiMovie.model.CreditsMovie;
import com.example.cinemates.ui.CineMates.ApiMovie.model.DetailedMovie;
import com.example.cinemates.ui.CineMates.ApiMovie.model.MovieResearch;
import com.example.cinemates.ui.CineMates.ApiMovie.model.NowPlayingFilms;
import com.example.cinemates.ui.CineMates.ApiMovie.model.PopularFilms;
import com.example.cinemates.ui.CineMates.ApiMovie.model.UpComingFilms;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("movie/popular")
    Call<PopularFilms> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}")
    Call<DetailedMovie> getMovieDetails(@Path("movie_id") int movieId, @Query("api_key") String apiKey, @Query("append_to_response") String credits);

    @GET("movie/{movie_id}/credits")
    Call<CreditsMovie> getCreditsMovie(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @GET("movie/upcoming")
    Call<UpComingFilms> getUpComingMovies(@Query("api_key") String apiKey);

    @GET("movie/now_playing")
    Call<NowPlayingFilms> getNowPlayingMovies(@Query("api_key") String apiKey);

    @GET("search/movie")
    Call<MovieResearch> getMovieFromQuery(@Query("api_key") String apiKey, @Query("query") String query);
}
