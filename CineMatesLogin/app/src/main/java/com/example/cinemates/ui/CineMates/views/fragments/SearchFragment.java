package com.example.cinemates.ui.CineMates.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.presenters.fragments.SearchPresenter;

public class SearchFragment extends Fragment {
    public EditText searchField;
    public ImageView searchButton;
    public RecyclerView recyclerView;
    public ConstraintLayout constraintLayout;
    public ProgressBar progressBar;
    private SearchPresenter searchPresenter;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        searchField.setMovementMethod(null);
        searchPresenter = new SearchPresenter(this);

        searchPresenter.searchFilm();
        searchPresenter.Keyboard();
        searchPresenter.searchByKeyboard();
        return view;
    }
}
