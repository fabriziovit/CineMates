package com.example.cinemates.ui.CineMates.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.ApiMovie.model.Movie;
import com.example.cinemates.ui.CineMates.MovieListContract;
import com.example.cinemates.ui.CineMates.MovieListPresenter;
import com.example.cinemates.ui.CineMates.model.ItemFilm;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment implements MovieListContract.View {
    private ArrayList<ItemFilm> filmsPopular;
    private ArrayList<ItemFilm> filmsUpcoming;
    private ArrayList<ItemFilm> filmsNowplaying;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private List<Movie> popularList;
    private List<Movie> upComingList;
    private List<Movie> nowPlayingList;
    private ProgressBar pbLoading;
    private MovieListPresenter movieListPresenter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public HomeFragment() {
        // Required empty public constructor
    }

    public HomeFragment(ArrayList<ItemFilm> filmsPopular, ArrayList<ItemFilm> filmsUpcoming, ArrayList<ItemFilm> filmsNowplaying){
        this.filmsPopular = filmsPopular;
        this.filmsNowplaying = filmsNowplaying;
        this.filmsUpcoming = filmsUpcoming;
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieListPresenter = new MovieListPresenter(this);
        movieListPresenter.requestDataFromServer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tabLayout = view.findViewById(R.id.tabLayout_fragment_home);
        viewPager = view.findViewById(R.id.viewPager_fragment_home);
        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());


        adapter.AddFragment(new PopularFilmsFragment(filmsPopular), "Popolari");
        adapter.AddFragment(new NowPlayingFilmsFragment(filmsNowplaying), "Al cinema");
        adapter.AddFragment(new UpcomingFilmsFragment(filmsUpcoming), "In uscita");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.ic_popolare);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.ic_ticket);
        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(R.drawable.ic_calendario);

        return view;
    }

    @Override
    public void showProgress() {
        //pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        //pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void setDataToRecyclerView(List<Movie> popularArrayList, List<Movie> upComingArrayList, List<Movie> nowPlayingArrayList) {
        popularList.addAll(popularArrayList);
        upComingList.addAll(upComingArrayList);
        nowPlayingList.addAll(nowPlayingArrayList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Log.e("Errore", throwable.getMessage());
        Toast.makeText(getActivity(), "Errore mocc a sort", Toast.LENGTH_LONG).show();
    }

}