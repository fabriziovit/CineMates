package com.example.cinemates.ui.CineMates.presenters.activities;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;

import com.example.cinemates.databinding.ActivityVisualizzaPreferitiBinding;
import com.example.cinemates.ui.CineMates.ApiMovie.Contract.MovieDetailsContract;
import com.example.cinemates.ui.CineMates.ApiMovie.Presenter.MovieDetailsPresenter;
import com.example.cinemates.ui.CineMates.ApiMovie.model.DetailedMovie;
import com.example.cinemates.ui.CineMates.adapter.RecycleViewAdapter_Film_ListaPreferiti_Amico;
import com.example.cinemates.ui.CineMates.model.ItemFilm;
import com.example.cinemates.ui.CineMates.views.activities.HomeActivity;
import com.example.cinemates.ui.CineMates.views.activities.SchedaFilmActivity;
import com.example.cinemates.ui.CineMates.views.activities.VisualizzaPreferitiActivity;
import com.example.cinemates.ui.CineMates.views.fragments.ProfileFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.cinemates.ui.CineMates.ApiMovie.ApiClient.IMAGE_BASE_URL;
import static com.example.cinemates.ui.CineMates.util.Constants.KEY_MOVIE_ID;

public class VisualizzaPreferitiPresenter implements RecycleViewAdapter_Film_ListaPreferiti_Amico.OnClickListener, MovieDetailsContract.View{
    private final VisualizzaPreferitiActivity visualizzaPreferitiActivity;
    private List<ItemFilm> preferitiList;
    private List<ItemFilm> searchList;
    private MovieDetailsPresenter movieDetailsPresenter;
    private FirebaseFirestore db;
    private String username;
    private ActivityVisualizzaPreferitiBinding binding;


    public VisualizzaPreferitiPresenter(VisualizzaPreferitiActivity visualizzaPreferitiActivity, String username, FirebaseFirestore db, ActivityVisualizzaPreferitiBinding binding) {
        this.visualizzaPreferitiActivity = visualizzaPreferitiActivity;
        this.username = username;
        this.db = db;
        this.binding = binding;
        init(binding);
    }


    private void init(ActivityVisualizzaPreferitiBinding binding){
        preferitiList = new ArrayList<>();
        searchList = new ArrayList<>();
        movieDetailsPresenter = new MovieDetailsPresenter(this);

        new Thread(()-> {
            CollectionReference collectionReference = db.collection("users");
            collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (username.equals(documentSnapshot.getString("username"))) {
                            String uid = documentSnapshot.getString("uid");

                            new Thread(() -> {
                                CollectionReference collectionReference = db.collection("favorites").document(uid).collection(uid);
                                collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        if (queryDocumentSnapshots.size() == 0) {
                                            binding.textEditListaVuotaPreferitiAmico.setVisibility(View.VISIBLE);
                                            binding.searchBarVisualizzaPreferiti.setFocusable(false);
                                            binding.searchButtonVisualizzaPreferiti.setClickable(false);
                                        } else{
                                            binding.searchBarVisualizzaPreferiti.setFocusable(true);
                                            binding.searchButtonVisualizzaPreferiti.setClickable(true);
                                            binding.textEditListaVuotaPreferitiAmico.setVisibility(View.INVISIBLE);
                                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                movieDetailsPresenter.requestMovieData(documentSnapshot.getLong("idFilm").intValue());
                                            }
                                        }
                                    }
                                });
                            }).start();
                        }
                    }
                }
            });
        }).start();
    }

    public void Keyboard(ActivityVisualizzaPreferitiBinding binding) {
        binding.containerVisualizzaPreferiti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) visualizzaPreferitiActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.containerVisualizzaPreferiti.getWindowToken(), 0);
                binding.searchBarVisualizzaPreferiti.clearFocus();
            }
        });
    }

    public void HomeButton(ActivityVisualizzaPreferitiBinding binding){
        binding.homeButtonVisualizzaPreferiti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(visualizzaPreferitiActivity, HomeActivity.class);
                visualizzaPreferitiActivity.getActivity().startActivity(intent);
            }
        });
    }

    public void BackButton(ActivityVisualizzaPreferitiBinding binding){
        binding.backButtonVisualizzaPreferiti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visualizzaPreferitiActivity.onBackPressed();
            }
        });
    }

    public void SearchButton(ActivityVisualizzaPreferitiBinding binding){
        binding.searchButtonVisualizzaPreferiti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cercaFilm();
            }
        });
    }


    public void searchByKeyboard(ActivityVisualizzaPreferitiBinding binding){
        binding.searchBarVisualizzaPreferiti.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    cercaFilm();
                }
                return false;
            }
        });
    }

    @Override
    public void OnClickScheda(int position) {
        Intent i = new Intent(visualizzaPreferitiActivity, SchedaFilmActivity.class);
        i.putExtra(KEY_MOVIE_ID, searchList.get(position).getId());
        visualizzaPreferitiActivity.getActivity().startActivity(i);
    }

    private void cercaFilm(){
        searchList = new ArrayList<>();
        InputMethodManager imm = (InputMethodManager) visualizzaPreferitiActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.containerVisualizzaPreferiti.getWindowToken(), 0);
        binding.searchBarVisualizzaPreferiti.clearFocus();
        String ricerca = binding.searchBarVisualizzaPreferiti.getText().toString().toLowerCase();
        if(ricerca.length() != 0) {
            for (int i = 0; i < preferitiList.size(); i++) {
                String titolo = preferitiList.get(i).getTitolo().toLowerCase();
                if (titolo.contains(ricerca)) {
                    ItemFilm newFilm = new ItemFilm(preferitiList.get(i).getTitolo(), preferitiList.get(i).getBitmap(), preferitiList.get(i).getId());
                    searchList.add(newFilm);
                }
            }
            if(searchList.size() != 0) {
                RecycleViewAdapter_Film_ListaPreferiti_Amico recycleViewAdapter_film_listaPreferiti_amico = new RecycleViewAdapter_Film_ListaPreferiti_Amico(visualizzaPreferitiActivity, searchList, this);
                binding.filmVisualizzaPreferitiRecycleView.setLayoutManager(new GridLayoutManager(visualizzaPreferitiActivity, 2, GridLayoutManager.VERTICAL, false));
                binding.filmVisualizzaPreferitiRecycleView.setAdapter(recycleViewAdapter_film_listaPreferiti_amico);
                visualizzaPreferitiActivity.update();
            }else{
                RecycleViewAdapter_Film_ListaPreferiti_Amico recycleViewAdapter_film_listaPreferiti_amico = new RecycleViewAdapter_Film_ListaPreferiti_Amico(visualizzaPreferitiActivity, searchList, this);
                binding.filmVisualizzaPreferitiRecycleView.setLayoutManager(new GridLayoutManager(visualizzaPreferitiActivity, 2, GridLayoutManager.VERTICAL, false));
                binding.filmVisualizzaPreferitiRecycleView.setAdapter(recycleViewAdapter_film_listaPreferiti_amico);
                visualizzaPreferitiActivity.update();
                Toast.makeText(visualizzaPreferitiActivity, "Nessun film trovato con quel titolo!", Toast.LENGTH_SHORT).show();
            }
        }else {
            searchList = preferitiList;
            RecycleViewAdapter_Film_ListaPreferiti_Amico recycleViewAdapter_film_listaPreferiti_amico = new RecycleViewAdapter_Film_ListaPreferiti_Amico(visualizzaPreferitiActivity, searchList, this);
            binding.filmVisualizzaPreferitiRecycleView.setLayoutManager(new GridLayoutManager(visualizzaPreferitiActivity, 2, GridLayoutManager.VERTICAL, false));
            binding.filmVisualizzaPreferitiRecycleView.setAdapter(recycleViewAdapter_film_listaPreferiti_amico);
            visualizzaPreferitiActivity.update();
            Toast.makeText(visualizzaPreferitiActivity, "Nessun parametro di ricerca inserito!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showProgress() {
        binding.progressBarVisualizzaPreferiti.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        binding.progressBarVisualizzaPreferiti.setVisibility(View.GONE);
    }

    @Override
    public void setDataToViews(DetailedMovie movie) {
        if(movie != null) {
            preferitiList.add(new ItemFilm(movie.getTitle(), ProfileFragment.getBitmapFromdownload(IMAGE_BASE_URL + movie.getPoster_path()), movie.getId()));
            searchList.add(new ItemFilm(movie.getTitle(), ProfileFragment.getBitmapFromdownload(IMAGE_BASE_URL + movie.getPoster_path()), movie.getId()));
            RecycleViewAdapter_Film_ListaPreferiti_Amico recycleViewAdapter_film_listaPreferiti_amico = new RecycleViewAdapter_Film_ListaPreferiti_Amico(visualizzaPreferitiActivity, searchList, this);
            binding.filmVisualizzaPreferitiRecycleView.setLayoutManager(new GridLayoutManager(visualizzaPreferitiActivity, 2, GridLayoutManager.VERTICAL, false));
            binding.filmVisualizzaPreferitiRecycleView.setAdapter(recycleViewAdapter_film_listaPreferiti_amico);
        }
    }

    @Override
    public void setDataCredits(String regista) {

    }

    @Override
    public void setDataLista(DetailedMovie movie) {

    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Toast.makeText(visualizzaPreferitiActivity, "Errore nel caricamento", Toast.LENGTH_SHORT).show();
    }
}
