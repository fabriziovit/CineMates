package com.example.cinemates.ui.CineMates.ApiMovie.ApiModel;

import android.util.Log;

import com.example.cinemates.ui.CineMates.ApiMovie.ApiClient;
import com.example.cinemates.ui.CineMates.ApiMovie.ApiInterface;
import com.example.cinemates.ui.CineMates.ApiMovie.model.CreditsMovie;
import com.example.cinemates.ui.CineMates.ApiMovie.model.Crew;
import com.example.cinemates.ui.CineMates.ApiMovie.model.DetailedMovie;
import com.example.cinemates.ui.CineMates.ApiMovie.Contract.MovieDetailsContract;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.cinemates.ui.CineMates.ApiMovie.ApiClient.API_KEY;
import static com.example.cinemates.ui.CineMates.util.Constants.CREDITS;

public class MovieDetailsModel implements MovieDetailsContract.Model {

    private final String TAG = "MovieDetailsModel";
    private CreditsMovie creditsMovie;
    String regista = "";

    @Override
    public void getMovieDetails(OnFinishedListener onFinishedListener, int movieId) {
        ApiInterface apiInterface =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CreditsMovie> creditsMovieCall = apiInterface.getCreditsMovie(movieId, API_KEY);
        creditsMovieCall.enqueue(new Callback<CreditsMovie>() {
            @Override
            public void onResponse(Call<CreditsMovie> call, Response<CreditsMovie> response) {
                creditsMovie = response.body();
                ArrayList<Crew> crewlist = creditsMovie.getCrew();
                for (Crew crew : crewlist) {
                    if (crew.getJob().equals("Director"))
                        if (regista.equals(""))
                            regista = crew.getName();
                        else
                            regista = regista + ", " + crew.getName();
                }
                onFinishedListener.onFinishedCredits(regista);
            }

            @Override
            public void onFailure(Call<CreditsMovie> call, Throwable t) {
                Log.e(TAG, t.toString());
                onFinishedListener.onFailure(t);
            }
        });

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<DetailedMovie> call = apiService.getMovieDetails(movieId, API_KEY, CREDITS);
        call.enqueue(new Callback<DetailedMovie>() {
            @Override
            public void onResponse(Call<DetailedMovie> call, Response<DetailedMovie> response) {
                DetailedMovie detailedMovie = response.body();
                onFinishedListener.onFinished(detailedMovie);
            }

            @Override
            public void onFailure(Call<DetailedMovie> call, Throwable t) {
                Log.e(TAG, t.toString());
                onFinishedListener.onFailure(t);
            }
        });
    }

    @Override
    public void getMovieDetails(OnFinishedListener onFinishedListener, int movieId, int list) {
        if(list == 1) {
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<DetailedMovie> call = apiService.getMovieDetails(movieId, API_KEY, CREDITS);
            call.enqueue(new Callback<DetailedMovie>() {
                @Override
                public void onResponse(Call<DetailedMovie> call, Response<DetailedMovie> response) {
                    DetailedMovie detailedMovie = response.body();
                    onFinishedListener.onFinished(detailedMovie);
                }

                @Override
                public void onFailure(Call<DetailedMovie> call, Throwable t) {
                    Log.e(TAG, t.toString());
                    onFinishedListener.onFailure(t);

                }
            });
        }else{
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<DetailedMovie> call = apiService.getMovieDetails(movieId, API_KEY, CREDITS);
            call.enqueue(new Callback<DetailedMovie>() {
                @Override
                public void onResponse(Call<DetailedMovie> call, Response<DetailedMovie> response) {
                    DetailedMovie detailedMovie = response.body();
                    onFinishedListener.onFinishedLista(detailedMovie);
                }

                @Override
                public void onFailure(Call<DetailedMovie> call, Throwable t) {
                    Log.e(TAG, t.toString());
                    onFinishedListener.onFailure(t);
                }
            });
        }
    }
}
