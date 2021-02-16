package com.example.cinemates.ui.CineMates.ApiMovie.Presenter;

import android.util.Log;

import com.example.cinemates.ui.CineMates.ApiMovie.model.Movie;
import com.example.cinemates.ui.CineMates.ApiMovie.model.NowPlayingFilms;
import com.example.cinemates.ui.CineMates.MovieListContract;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NowPlayingMovieListModel implements MovieListContract.Model {

    private final String TAG = "NowPlayingMovieModel";

    @Override
    public void getMovieList(final MovieListContract.Model.OnFinishedListener onFinishedListener) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<NowPlayingFilms> call = apiService.getNowPlayingMovies(ApiClient.API_KEY);
        call.enqueue(new Callback<NowPlayingFilms>() {
           @Override
           public void onResponse(Call<NowPlayingFilms> call, Response<NowPlayingFilms> response) {
               List<Movie> movies = response.body().getResults();
               Log.d(TAG, "Number of movies received: " + movies.size());
               onFinishedListener.onFinished(movies);
           }

           @Override
           public void onFailure(Call<NowPlayingFilms> call, Throwable t) {
               Log.e(TAG, t.toString());
               onFinishedListener.onFailure(t);
           }
        });
    }
}
