package com.example.cinemates.ui.CineMates.ApiMovie.Presenter;

import android.util.Log;

import com.example.cinemates.ui.CineMates.ApiMovie.model.Movie;
import com.example.cinemates.ui.CineMates.ApiMovie.model.NowPlayingFilms;
import com.example.cinemates.ui.CineMates.Fragment.HomeFragment;
import com.example.cinemates.ui.CineMates.Fragment.ProfileFragment;
import com.example.cinemates.ui.CineMates.MovieListContract;
import com.example.cinemates.ui.CineMates.model.ItemFilm;

import java.util.ArrayList;
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
               ArrayList<ItemFilm> nowPlaying = new ArrayList<>();
               for (Movie movie : movies)
                   nowPlaying.add(new ItemFilm(movie.getTitle(), ProfileFragment.getBitmapFromdownload(
                           "https://image.tmdb.org/t/p/w185" + movie.getPoster_path()), movie.getId()));
               HomeFragment.filmsNowplaying = nowPlaying;
               onFinishedListener.onFinished(nowPlaying);
           }

           @Override
           public void onFailure(Call<NowPlayingFilms> call, Throwable t) {
               Log.e(TAG, t.toString());
               onFinishedListener.onFailure(t);
           }
        });
    }
}
