package com.example.cinemates.ui.CineMates.ApiMovie.Presenter;

import com.example.cinemates.ui.CineMates.ApiMovie.ApiModel.SearchMovieModel;
import com.example.cinemates.ui.CineMates.ApiMovie.Contract.MovieResearchContract;
import com.example.cinemates.ui.CineMates.model.ItemFilm;

import java.util.ArrayList;

public class MovieResearchPresenter implements MovieResearchContract.Presenter, MovieResearchContract, MovieResearchContract.Model.OnFinishedListener {
    private MovieResearchContract.View movieListView;

    private MovieResearchContract.Model researchMovieListModel;

    public MovieResearchPresenter(MovieResearchContract.View movieListView) {
        this.movieListView = movieListView;
        researchMovieListModel = new SearchMovieModel();
    }

    @Override
    public void onFinished(ArrayList<ItemFilm> movies) {
        movieListView.setDataToRecyclerView(movies);
        if (movieListView != null) {
            movieListView.hideProgress();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        movieListView.onResponseFailure(t);
        if (movieListView != null) {
            movieListView.hideProgress();
        }
    }

    @Override
    public void onDestroy() {
        this.movieListView = null;
    }

    @Override
    public void getMoreData(String query) {
        if (movieListView != null) {
            movieListView.showProgress();
        }
        researchMovieListModel.getMovieList(this, query);
    }

    @Override
    public void requestDataFromServer(String query) {
        researchMovieListModel.getMovieList(this, query);
    }
}
