package com.example.cinemates.ui.CineMates.ApiMovie.Contract;

import com.example.cinemates.ui.CineMates.model.ItemFilm;

import java.util.ArrayList;
import java.util.List;

public interface MovieResearchContract {
    interface Model {
        interface OnFinishedListener {
            void onFinished(ArrayList<ItemFilm> movies);
            void onFailure(Throwable t);
        }
        void getMovieList(MovieResearchContract.Model.OnFinishedListener onFinihedListener, String query);
    }

    interface View {
        void showProgress();
        void hideProgress();
        void setDataToRecyclerView(List<ItemFilm> movieArrayList);
        void onResponseFailure(Throwable throwable);
    }

    interface Presenter {
        void onDestroy();
        void getMoreData(String query);
        void requestDataFromServer(String query);
    }

}
