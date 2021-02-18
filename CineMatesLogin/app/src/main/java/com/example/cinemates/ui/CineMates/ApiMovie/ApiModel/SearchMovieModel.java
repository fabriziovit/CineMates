package com.example.cinemates.ui.CineMates.ApiMovie.ApiModel;

import com.example.cinemates.ui.CineMates.ApiMovie.ApiClient;
import com.example.cinemates.ui.CineMates.ApiMovie.ApiInterface;
import com.example.cinemates.ui.CineMates.ApiMovie.model.Movie;
import com.example.cinemates.ui.CineMates.ApiMovie.model.MovieResearch;
import com.example.cinemates.ui.CineMates.Fragment.ProfileFragment;
import com.example.cinemates.ui.CineMates.ApiMovie.Contract.MovieResearchContract;
import com.example.cinemates.ui.CineMates.model.ItemFilm;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.cinemates.ui.CineMates.ApiMovie.ApiClient.IMAGE_BASE_URL;
import static com.example.cinemates.ui.CineMates.util.Constants.DEFAULT_MOVIE;

public class SearchMovieModel implements MovieResearchContract.Model {
    private final String TAG = "SearchMovieModel";

    @Override
    public void getMovieList(MovieResearchContract.Model.OnFinishedListener onFinihedListener, String query) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MovieResearch> call = apiService.getMovieFromQuery(ApiClient.API_KEY, query);
        call.enqueue(new Callback<MovieResearch>() {
            @Override
            public void onResponse(Call<MovieResearch> call, Response<MovieResearch> response) {
                ArrayList<Movie> movies = response.body().getResults();
                ArrayList<ItemFilm> movieList = new ArrayList<>();
                for(Movie movie: movies){
                    if(movie.getPoster_path() != null)
                    movieList.add(new ItemFilm(movie.getTitle(), ProfileFragment.getBitmapFromdownload(
                            IMAGE_BASE_URL+movie.getPoster_path()), movie.getId()));
                    else
                        movieList.add(new ItemFilm(movie.getTitle(), ProfileFragment.getBitmapFromdownload(
                                DEFAULT_MOVIE), movie.getId()));
                }
                onFinihedListener.onFinished(movieList);
            }

            @Override
            public void onFailure(Call<MovieResearch> call, Throwable t) {
                onFinihedListener.onFailure(t);
            }
        });

    }
}
