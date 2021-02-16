package com.example.cinemates.ui.CineMates;

import com.example.cinemates.ui.CineMates.ApiMovie.Presenter.NowPlayingMovieListModel;
import com.example.cinemates.ui.CineMates.ApiMovie.Presenter.PopularMovieListModel;
import com.example.cinemates.ui.CineMates.ApiMovie.Presenter.UpComingMovieListModel;
import com.example.cinemates.ui.CineMates.model.ItemFilm;

import java.util.ArrayList;

public class MovieListPresenter implements MovieListContract.Model.OnFinishedListener, MovieListContract.Presenter{

    private MovieListContract.View movieListView;

    private MovieListContract.Model popularMovieListModel;
    private MovieListContract.Model upComingMovieListModel;
    private MovieListContract.Model nowPlayingMovieListModel;


    public MovieListPresenter(MovieListContract.View movieListView) {
        this.movieListView = movieListView;
        popularMovieListModel = new PopularMovieListModel();
        upComingMovieListModel = new UpComingMovieListModel();
        nowPlayingMovieListModel = new NowPlayingMovieListModel();
    }

    @Override
    public void onDestroy() {
        this.movieListView = null;
    }

    @Override
    public void getMoreData() {

        if (movieListView != null) {
            movieListView.showProgress();
        }
        popularMovieListModel.getMovieList(this);
        upComingMovieListModel.getMovieList(this);
        nowPlayingMovieListModel.getMovieList(this);
    }

    @Override
    public void requestDataFromServer() {

        if (movieListView != null) {
            movieListView.showProgress();
        }
        popularMovieListModel.getMovieList(this);
        upComingMovieListModel.getMovieList(this);
        nowPlayingMovieListModel.getMovieList(this);
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
}
