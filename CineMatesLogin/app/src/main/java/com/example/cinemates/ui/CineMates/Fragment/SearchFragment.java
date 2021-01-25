package com.example.cinemates.ui.CineMates.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.ApiMovie.Movie;
import com.example.cinemates.ui.CineMates.ApiMovie.MovieResearch;
import com.example.cinemates.ui.CineMates.ApiMovie.SearchMovieApi;
import com.example.cinemates.ui.CineMates.ItemFilm;
import com.example.cinemates.ui.CineMates.RecycleViewAdapter_Film;
import com.example.cinemates.ui.CineMates.SchedaFilmActivity;

import java.util.ArrayList;

import Intefaces.UpdateableFragmentListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFragment extends Fragment implements RecycleViewAdapter_Film.OnClickListener, UpdateableFragmentListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText searchField;
    private ImageView searchButton;
    private RecyclerView recyclerView;
    private ConstraintLayout constraintLayout;
    private MovieResearch movieResearch;
    private ArrayList<ItemFilm> searchedMovie;
    private boolean pop = false;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchField = view.findViewById(R.id.search_bar_fragmentSearch);
        constraintLayout = view.findViewById(R.id.container_fragment_search);
        searchButton = view.findViewById(R.id.search_button_fragmentSearch);
        recyclerView = view.findViewById(R.id.resultFilm_fragmentSearch);

        searchFilm();
        Keyboard();

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
                if (searchField.getText().length() != 0) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(constraintLayout.getWindowToken(), 0);
                    searchField.clearFocus();
                    searchedMovie = new ArrayList<>();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://api.themoviedb.org")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    new Thread(() -> {
                        SearchMovieApi searchMovieApi = retrofit.create(SearchMovieApi.class);
                        Call<MovieResearch> call = searchMovieApi.movieList("3/search/movie?api_key=03941baf012eb2cd38196f9df8751df6&query=" + searchField.getText().toString());
                        call.enqueue(new Callback<MovieResearch>() {
                            @Override
                            public void onResponse(Call<MovieResearch> call, Response<MovieResearch> response) {
                                movieResearch = response.body();
                                for (Movie movie : movieResearch.getResults()) {
                                    searchedMovie.add(new ItemFilm(movie.getTitle(), ProfileFragment.getBitmapFromdownload(
                                            "https://image.tmdb.org/t/p/w185" + movie.getPoster_path()), movie.getId()));
                                    update();
                                    pop = true;
                                }
                                if (searchedMovie.size() == 0)
                                    Toast.makeText(getContext(), "Nessun risultato", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<MovieResearch> call, Throwable t) {
                                Log.e("Errore", "Errore nel caricamento delle api.");
                            }
                        });
                    }).start();
                    RecycleViewAdapter_Film recycleViewAdapterFilm = new RecycleViewAdapter_Film(getContext(), searchedMovie, SearchFragment.this::OnClick);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                            DividerItemDecoration.HORIZONTAL));
                    recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                            DividerItemDecoration.VERTICAL));
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setAdapter(recycleViewAdapterFilm);
                } else
                    Toast.makeText(getContext(), "Nessun parametro di ricerca!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void OnClick(int position) {
        Intent i = new Intent(getActivity(), SchedaFilmActivity.class);
        i.putExtra("id", searchedMovie.get(position).getId());
        startActivity(i);
    }

    @Override
    public void update() {
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}
