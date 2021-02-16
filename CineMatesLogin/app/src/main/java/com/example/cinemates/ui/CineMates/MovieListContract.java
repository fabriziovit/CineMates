package com.example.cinemates.ui.CineMates;

import com.example.cinemates.ui.CineMates.ApiMovie.model.Movie;

import java.util.List;

public interface MovieListContract {

    interface Model {
        interface OnFinishedListener {
            void onFinished(List<Movie> movies);
            void onFinished(List<Movie> popularArrayList, List<Movie> upComingArrayList, List<Movie> nowPlayingArrayList);
            void onFailure(Throwable t);
        }

        void getMovieList(OnFinishedListener onFinihedListener);
    }

    interface View {
        void showProgress();
        void hideProgress();
        void setDataToRecyclerView(List<Movie> popularArrayList, List<Movie> upComingArrayList, List<Movie> nowPlayingArrayList);
        void onResponseFailure(Throwable throwable);
    }

    interface Presenter {
        void onDestroy();
        void getMoreData();
        void requestDataFromServer();
    }
}
