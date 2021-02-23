package com.example.cinemates.ui.CineMates.ApiMovie;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static Retrofit retrofit = null;
    public static final String API_KEY = "03941baf012eb2cd38196f9df8751df6";
    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w185";


    /**
     * This method returns retrofit client instance
     *
     * @return Retrofit object
     */

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
