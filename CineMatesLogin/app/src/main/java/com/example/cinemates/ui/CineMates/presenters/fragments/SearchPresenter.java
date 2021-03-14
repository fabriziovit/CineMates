package com.example.cinemates.ui.CineMates.presenters.fragments;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;

import com.example.cinemates.ui.CineMates.ApiMovie.Contract.MovieResearchContract;
import com.example.cinemates.ui.CineMates.ApiMovie.Presenter.MovieResearchPresenter;
import com.example.cinemates.ui.CineMates.adapter.RecycleViewAdapter_Film_SearchFilm;
import com.example.cinemates.ui.CineMates.model.ItemFilm;
import com.example.cinemates.ui.CineMates.views.activities.SchedaFilmActivity;
import com.example.cinemates.ui.CineMates.views.fragments.SearchFragment;

import java.util.ArrayList;
import java.util.List;

import Intefaces.UpdateableFragmentListener;

import static com.example.cinemates.ui.CineMates.util.Constants.KEY_MOVIE_ID;

public class SearchPresenter implements MovieResearchContract.View, RecycleViewAdapter_Film_SearchFilm.OnClickListener, UpdateableFragmentListener {
    private final SearchFragment searchFragment;
    private MovieResearchPresenter movieResearchPresenter;
    private ArrayList<ItemFilm> searchedMovie;

    public SearchPresenter(SearchFragment searchFragment) {
        this.searchFragment = searchFragment;
        init();
    }

    private void init(){
        movieResearchPresenter = new MovieResearchPresenter(this);
        searchedMovie = new ArrayList<>();
    }

    public void Keyboard() {
        searchFragment.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) searchFragment.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchFragment.constraintLayout.getWindowToken(), 0);
                searchFragment.searchField.clearFocus();
            }
        });
    }

    public void searchFilm() {
        searchFragment.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress();
                if (searchFragment.searchField.getText().length() != 0) {
                    InputMethodManager imm = (InputMethodManager) searchFragment.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchFragment.constraintLayout.getWindowToken(), 0);
                    searchFragment.searchField.clearFocus();
                    searchedMovie = new ArrayList<>();
                    movieResearchPresenter.requestDataFromServer(searchFragment.searchField.getText().toString());
                    RecycleViewAdapter_Film_SearchFilm recycleViewAdapter_film_searchFilm = new RecycleViewAdapter_Film_SearchFilm(searchFragment.getContext(), searchedMovie, SearchPresenter.this);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(searchFragment.getContext(), 2, GridLayoutManager.VERTICAL, false);
                    searchFragment.recyclerView.setLayoutManager(gridLayoutManager);
                    searchFragment.recyclerView.setAdapter(recycleViewAdapter_film_searchFilm);
                } else {
                    hideProgress();
                    Toast.makeText(searchFragment.getContext(), "Nessun parametro di ricerca!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void searchByKeyboard(){
        searchFragment.searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    showProgress();
                    if (searchFragment.searchField.getText().length() != 0) {
                        InputMethodManager imm = (InputMethodManager) searchFragment.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(searchFragment.constraintLayout.getWindowToken(), 0);
                        searchFragment.searchField.clearFocus();
                        searchedMovie = new ArrayList<>();
                        movieResearchPresenter.requestDataFromServer(searchFragment.searchField.getText().toString());
                        RecycleViewAdapter_Film_SearchFilm recycleViewAdapter_film_searchFilm = new RecycleViewAdapter_Film_SearchFilm(searchFragment.getContext(), searchedMovie, SearchPresenter.this);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(searchFragment.getContext(), 2, GridLayoutManager.VERTICAL, false);
                        searchFragment.recyclerView.setLayoutManager(gridLayoutManager);
                        searchFragment.recyclerView.setAdapter(recycleViewAdapter_film_searchFilm);
                    } else {
                        hideProgress();
                        Toast.makeText(searchFragment.getContext(), "Nessun parametro di ricerca!", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void OnClick(int position) {
        Intent i = new Intent(searchFragment.getActivity(), SchedaFilmActivity.class);
        i.putExtra(KEY_MOVIE_ID, searchedMovie.get(position).getId());
        searchFragment.startActivity(i);
    }

    @Override
    public void update() {
        searchFragment.recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showProgress() {
        searchFragment.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        searchFragment.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setDataToRecyclerView(List<ItemFilm> movieArrayList) {
        searchedMovie.addAll(movieArrayList);
        update();
        if(movieArrayList.size() == 0)
            Toast.makeText(searchFragment.getContext(), "Nessun film con questo titolo!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Toast.makeText(searchFragment.getActivity(), "Errore nel caricamento", Toast.LENGTH_LONG).show();
    }
}
