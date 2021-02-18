package com.example.cinemates.ui.CineMates.ApiMovie.Contract;

import com.example.cinemates.ui.CineMates.ApiMovie.model.DetailedMovie;

public interface MovieDetailsContract {

    interface Model {

        interface OnFinishedListener {
            void onFinishedCredits(String regista);
            void onFinished(DetailedMovie movie);
            void onFinishedLista(DetailedMovie movie);
            void onFailure(Throwable t);
        }

        void getMovieDetails(OnFinishedListener onFinishedListener, int movieId);
        void getMovieDetails(OnFinishedListener onFinishedListener, int movieId, int list);
    }

    interface View {

        void showProgress();

        void hideProgress();

        void setDataToViews(DetailedMovie movie);
        void setDataCredits(String regista);
        void setDataLista(DetailedMovie movie);

        void onResponseFailure(Throwable throwable);
    }

    interface Presenter {
        void onDestroy();

        void requestMovieData(int movieId);
        void requestMovieData(int movieId, int list);
    }
}
