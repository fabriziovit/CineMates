package com.example.cinemates.ui.CineMates.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.ApiMovie.Contract.MovieResearchContract;
import com.example.cinemates.ui.CineMates.ApiMovie.Presenter.MovieResearchPresenter;
import com.example.cinemates.ui.CineMates.activity.SchedaFilmActivity;
import com.example.cinemates.ui.CineMates.adapter.RecycleViewAdapter_Film_SearchFilm;
import com.example.cinemates.ui.CineMates.model.ItemFilm;

import java.util.ArrayList;
import java.util.List;

import Intefaces.UpdateableFragmentListener;

import static com.example.cinemates.ui.CineMates.util.Constants.KEY_MOVIE_ID;

public class SearchFragment extends Fragment implements MovieResearchContract.View, RecycleViewAdapter_Film_SearchFilm.OnClickListener, UpdateableFragmentListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText searchField;
    private ImageView searchButton;
    private RecyclerView recyclerView;
    private ConstraintLayout constraintLayout;
    private ProgressBar progressBar;
    private ArrayList<ItemFilm> searchedMovie;
    private MovieResearchPresenter movieResearchPresenter;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieResearchPresenter = new MovieResearchPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchField = view.findViewById(R.id.search_bar_fragmentSearch);
        constraintLayout = view.findViewById(R.id.container_fragment_search);
        searchButton = view.findViewById(R.id.search_button_fragmentSearch);
        recyclerView = view.findViewById(R.id.resultFilm_fragmentSearch);
        progressBar = view.findViewById(R.id.progressBar_SearchFilm);

        searchFilm();
        Keyboard();
        searchByKeyboard();
        return view;
    }

    private void Keyboard() {
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(constraintLayout.getWindowToken(), 0);
                searchField.clearFocus();
            }
        });
    }

    private void searchFilm() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress();
                if (searchField.getText().length() != 0) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(constraintLayout.getWindowToken(), 0);
                    searchField.clearFocus();
                    searchedMovie = new ArrayList<>();
                    movieResearchPresenter.requestDataFromServer(searchField.getText().toString());
                    RecycleViewAdapter_Film_SearchFilm recycleViewAdapter_film_searchFilm = new RecycleViewAdapter_Film_SearchFilm(getContext(), searchedMovie, SearchFragment.this);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setAdapter(recycleViewAdapter_film_searchFilm);
                } else {
                    hideProgress();
                    Toast.makeText(getContext(), "Nessun parametro di ricerca!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void searchByKeyboard(){
        searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    showProgress();
                    if (searchField.getText().length() != 0) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(constraintLayout.getWindowToken(), 0);
                        searchField.clearFocus();
                        searchedMovie = new ArrayList<>();
                        movieResearchPresenter.requestDataFromServer(searchField.getText().toString());
                        RecycleViewAdapter_Film_SearchFilm recycleViewAdapter_film_searchFilm = new RecycleViewAdapter_Film_SearchFilm(getContext(), searchedMovie, SearchFragment.this);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(gridLayoutManager);
                        recyclerView.setAdapter(recycleViewAdapter_film_searchFilm);
                    } else {
                        hideProgress();
                        Toast.makeText(getContext(), "Nessun parametro di ricerca!", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void OnClick(int position) {
        Intent i = new Intent(getActivity(), SchedaFilmActivity.class);
        i.putExtra(KEY_MOVIE_ID, searchedMovie.get(position).getId());
        startActivity(i);
    }

    @Override
    public void update() {
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setDataToRecyclerView(List<ItemFilm> movieArrayList) {
        searchedMovie.addAll(movieArrayList);
        update();
        if(movieArrayList.size() == 0)
            Toast.makeText(getContext(), "Nessun film con questo titolo!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Toast.makeText(getActivity(), "Errore nel caricamento", Toast.LENGTH_LONG).show();
    }
}
