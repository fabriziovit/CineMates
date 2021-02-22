package com.example.cinemates.ui.CineMates.ApiMovie.Contract;

import com.example.cinemates.ui.CineMates.model.ItemFilm;

import java.util.ArrayList;

public interface MovieListContract {

    interface Model {
        interface OnFinishedListener {
            void onFinished(ArrayList<ItemFilm> movies);
            void onFailure(Throwable t);
        }
        void getMovieList(OnFinishedListener onFinihedListener);
    }

    interface View {
        void showProgress();
        void hideProgress();
        void onResponseFailure(Throwable throwable);
    }

    interface Presenter {
        void onDestroy();
        void getMoreData();
        void requestDataFromServer();
    }
}
