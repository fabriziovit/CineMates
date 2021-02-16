package com.example.cinemates.ui.CineMates;

import com.example.cinemates.ui.CineMates.ApiMovie.model.Movie;

public interface MovieDetailsContract {

    interface Model {

        interface OnFinishedListener {
            void onFinished(Movie movie);

            void onFailure(Throwable t);
        }

        void getMovieDetails(OnFinishedListener onFinishedListener, int movieId);
    }

    interface View {

        void showProgress();

        void hideProgress();

        void setDataToViews(Movie movie);

        void onResponseFailure(Throwable throwable);
    }

    interface Presenter {
        void onDestroy();

        void requestMovieData(int movieId);
    }
}
