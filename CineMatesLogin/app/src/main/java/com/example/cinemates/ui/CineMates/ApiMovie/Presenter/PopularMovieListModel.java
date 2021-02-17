package com.example.cinemates.ui.CineMates.ApiMovie.Presenter;

import android.util.Log;

import com.example.cinemates.ui.CineMates.ApiMovie.model.Movie;
import com.example.cinemates.ui.CineMates.ApiMovie.model.PopularFilms;
import com.example.cinemates.ui.CineMates.Fragment.HomeFragment;
import com.example.cinemates.ui.CineMates.Fragment.ProfileFragment;
import com.example.cinemates.ui.CineMates.MovieListContract;
import com.example.cinemates.ui.CineMates.model.ItemFilm;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopularMovieListModel implements MovieListContract.Model{

    private final String TAG = "PopularMovieListModel";

    @Override
    public void getMovieList(final MovieListContract.Model.OnFinishedListener onFinishedListener) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<PopularFilms> call = apiService.getPopularMovies(ApiClient.API_KEY);
        call.enqueue(new Callback<PopularFilms>() {
            @Override
            public void onResponse(Call<PopularFilms> call, Response<PopularFilms> response) {
                List<Movie> movies = response.body().getResults();
                ArrayList<ItemFilm> popularList = new ArrayList<>();
                for (Movie movie : movies)
                    popularList.add(new ItemFilm(movie.getTitle(), ProfileFragment.getBitmapFromdownload(
                            "https://image.tmdb.org/t/p/w185" + movie.getPoster_path()), movie.getId()));
                HomeFragment.filmsPopular = popularList;
                onFinishedListener.onFinished(popularList);
            }

            @Override
            public void onFailure(Call<PopularFilms> call, Throwable t) {
                Log.e(TAG, t.toString());
                onFinishedListener.onFailure(t);
            }
        });
    }
}
