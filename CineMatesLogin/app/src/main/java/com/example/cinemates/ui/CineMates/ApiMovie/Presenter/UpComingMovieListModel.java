package com.example.cinemates.ui.CineMates.ApiMovie.Presenter;

import android.util.Log;

import com.example.cinemates.ui.CineMates.ApiMovie.model.Movie;
import com.example.cinemates.ui.CineMates.ApiMovie.model.UpComingFilms;
import com.example.cinemates.ui.CineMates.Fragment.HomeFragment;
import com.example.cinemates.ui.CineMates.Fragment.ProfileFragment;
import com.example.cinemates.ui.CineMates.MovieListContract;
import com.example.cinemates.ui.CineMates.model.ItemFilm;

import java.util.ArrayList;
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
                ArrayList<ItemFilm> upComingList = new ArrayList<>();
                Log.d(TAG, "Number of movies received: " + movies.size());
                for (Movie movie : movies)
                    upComingList.add(new ItemFilm(movie.getTitle(), ProfileFragment.getBitmapFromdownload(
                            "https://image.tmdb.org/t/p/w185" + movie.getPoster_path()), movie.getId()));
                HomeFragment.filmsUpcoming = upComingList;
                onFinishedListener.onFinished(upComingList);
            }

            @Override
            public void onFailure(Call<UpComingFilms> call, Throwable t) {
                Log.e(TAG, t.toString());
                onFinishedListener.onFailure(t);
            }
        });
    }
}
