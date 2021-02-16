package com.example.cinemates.ui.CineMates.ApiMovie.Presenter;

import android.util.Log;

import com.example.cinemates.ui.CineMates.ApiMovie.model.Movie;
import com.example.cinemates.ui.CineMates.ApiMovie.model.UpComingFilms;
import com.example.cinemates.ui.CineMates.MovieListContract;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpComingMovieListModel implements MovieListContract.Model {

    private final String TAG = "UpComingMovieListModel";

    @Override
    public void getMovieList(final MovieListContract.Model.OnFinishedListener onFinishedListener) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UpComingFilms> call = apiService.getUpComingMovies(ApiClient.API_KEY);
        call.enqueue(new Callback<UpComingFilms>() {
            @Override
            public void onResponse(Call<UpComingFilms> call, Response<UpComingFilms> response) {
                List<Movie> movies = response.body().getResults();
                Log.d(TAG, "Number of movies received: " + movies.size());
                onFinishedListener.onFinished(movies);
            }

            @Override
            public void onFailure(Call<UpComingFilms> call, Throwable t) {
                Log.e(TAG, t.toString());
                onFinishedListener.onFailure(t);
            }
        });
    }
}
