package com.example.cinemates.ui.CineMates;

import com.example.cinemates.ui.CineMates.ApiMovie.model.DetailedMovie;

public interface MovieDetailsContract {

    interface Model {

        interface OnFinishedListener {
            void onFinishedCredits(String regista);
            void onFinished(DetailedMovie movie);
            void onFailure(Throwable t);
        }

        void getMovieDetails(OnFinishedListener onFinishedListener, int movieId);
    }

    interface View {

        void showProgress();

        void hideProgress();

        void setDataToViews(DetailedMovie movie);
        void setDataCredits(String regista);

        void onResponseFailure(Throwable throwable);
    }

    interface Presenter {
        void onDestroy();

        void requestMovieData(int movieId);
    }
}
