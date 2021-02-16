package com.example.cinemates.ui.CineMates;

import com.example.cinemates.ui.CineMates.model.ItemFilm;

import java.util.ArrayList;
import java.util.List;

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
        void setDataToRecyclerView(List<ItemFilm> movieArrayList);
        void onResponseFailure(Throwable throwable);
    }

    interface Presenter {
        void onDestroy();
        void getMoreData();
        void requestDataFromServer();
    }
}
