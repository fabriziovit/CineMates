package com.example.cinemates.ui.CineMates.ApiMovie.Presenter;

import com.example.cinemates.ui.CineMates.ApiMovie.ApiModel.MovieDetailsModel;
import com.example.cinemates.ui.CineMates.ApiMovie.model.DetailedMovie;
import com.example.cinemates.ui.CineMates.ApiMovie.Contract.MovieDetailsContract;

public class MovieDetailsPresenter implements MovieDetailsContract.Presenter, MovieDetailsContract, MovieDetailsContract.Model.OnFinishedListener {

    private MovieDetailsContract.View movieDetailView;
    private MovieDetailsContract.Model movieDetailsModel;

    public MovieDetailsPresenter(MovieDetailsContract.View movieDetailView) {
        this.movieDetailView = movieDetailView;
        this.movieDetailsModel = new MovieDetailsModel();
    }

    @Override
    public void onDestroy() {
        movieDetailView = null;
    }

    @Override
    public void requestMovieData(int movieId) {
        if(movieDetailView != null){
            movieDetailView.showProgress();
        }
        movieDetailsModel.getMovieDetails(this, movieId);
    }

    @Override
    public void requestMovieData(int movieId, int list) {
        if(movieDetailView != null){
            movieDetailView.showProgress();
        }
        movieDetailsModel.getMovieDetails(this, movieId, list);
    }

    @Override
    public void onFinished(DetailedMovie movie) {
        if(movieDetailView != null){
            movieDetailView.hideProgress();
        }
        movieDetailView.setDataToViews(movie);
    }

    @Override
    public void onFinishedLista(DetailedMovie movie) {
        if(movieDetailView != null){
            movieDetailView.hideProgress();
        }
        movieDetailView.setDataLista(movie);
    }


    @Override
    public void onFinishedCredits(String regista) {
        if(movieDetailView != null){
            movieDetailView.hideProgress();
        }
        movieDetailView.setDataCredits(regista);
    }

    @Override
    public void onFailure(Throwable t) {
        if(movieDetailView != null){
            movieDetailView.hideProgress();
        }
        movieDetailView.onResponseFailure(t);
    }
}
