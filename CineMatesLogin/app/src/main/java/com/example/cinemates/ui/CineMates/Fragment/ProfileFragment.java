package com.example.cinemates.ui.CineMates.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.ApiMovie.DetailedMovieApi;
import com.example.cinemates.ui.CineMates.ApiMovie.model.DetailedMovie;
import com.example.cinemates.ui.CineMates.activity.CredenzialiProfiloActivity;
import com.example.cinemates.ui.CineMates.activity.ListeUtenteActivity;
import com.example.cinemates.ui.CineMates.activity.LoginActivity;
import com.example.cinemates.ui.CineMates.model.ItemFilm;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.SettableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Bitmap bitmap;
    private String usernameText;
    private String emailText;
    private FirebaseFirestore db;
    private Button visualizzaPreferiti;
    private CircleImageView circleImageView;
    private FirebaseStorage storage;
    private String curUser;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private List<ItemFilm> filmPreferiti;
    private List<ItemFilm> filmDavedere;
    private DetailedMovie detailedMovie;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public ProfileFragment(Bitmap profilePic, String usernameText, String emailText){
        this.bitmap = profilePic;
        this.usernameText = usernameText;
        this.emailText = emailText;
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        circleImageView = view.findViewById(R.id.avatar_profile_fragment);
        circleImageView.setImageBitmap(bitmap);
        Button logoutbtn =  view.findViewById(R.id.logout_button_ProfileFragment);
        TextView usernameTextView = view.findViewById(R.id.username_profile_fragment);
        TextView emailTextView = view.findViewById(R.id.email_profile_fragment);
        Button modificaCredenzialiBtn = view.findViewById(R.id.modificaCredenziali_button_ProfileFragment);
        visualizzaPreferiti = view.findViewById(R.id.visualizzaListe_button_ProfileFragment);
        curUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db  = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        filmPreferiti = new ArrayList<>();
        filmDavedere = new ArrayList<>();


        new Thread(() -> {
            CollectionReference collectionReference = db.collection("favorites").document(curUser).collection(curUser);
            collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("https://api.themoviedb.org")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        DetailedMovieApi detailedMovieApi = retrofit.create(DetailedMovieApi.class);
                        Call<DetailedMovie> call = detailedMovieApi.detailedMovie("3/movie/" + documentSnapshot.getLong("idFilm") + "?api_key=03941baf012eb2cd38196f9df8751df6");
                        call.enqueue(new Callback<DetailedMovie>() {
                            @Override
                            public void onResponse(Call<DetailedMovie> call, Response<DetailedMovie> response) {
                                detailedMovie = response.body();
                                filmPreferiti.add(new ItemFilm(detailedMovie.getTitle(), ProfileFragment.getBitmapFromdownload("https://image.tmdb.org/t/p/w185" + detailedMovie.getPoster_path()), detailedMovie.getId()));
                            }

                            @Override
                            public void onFailure(Call<DetailedMovie> call, Throwable t) {
                                Log.e("ERRORE", "caricamento Api non riuscito");
                            }
                        });
                    }
                }
            });
        }).start();

        new Thread(() -> {
            CollectionReference collectionReference = db.collection("da vedere").document(curUser).collection(curUser);
            collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("https://api.themoviedb.org")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        DetailedMovieApi detailedMovieApi = retrofit.create(DetailedMovieApi.class);
                        Call<DetailedMovie> call = detailedMovieApi.detailedMovie("3/movie/" + documentSnapshot.getLong("idFilm") + "?api_key=03941baf012eb2cd38196f9df8751df6");
                        call.enqueue(new Callback<DetailedMovie>() {
                            @Override
                            public void onResponse(Call<DetailedMovie> call, Response<DetailedMovie> response) {
                                detailedMovie = response.body();
                                filmDavedere.add(new ItemFilm(detailedMovie.getTitle(), ProfileFragment.getBitmapFromdownload("https://image.tmdb.org/t/p/w185" + detailedMovie.getPoster_path()), detailedMovie.getId()));
                            }

                            @Override
                            public void onFailure(Call<DetailedMovie> call, Throwable t) {
                                Log.e("ERRORE", "caricamento Api non riuscito");
                            }
                        });
                    }
                }
            });
        }).start();

        emailTextView.setText(emailText);
        usernameTextView.setText(usernameText);

        ModificaImmagine();
        VisualizzaPreferiti();
        modificaCredenzialiBtn(modificaCredenzialiBtn);
        logout(logoutbtn);
        return view;
    }

    public static Bitmap getBitmapFromdownload(String source){
        if(Build.VERSION.SDK_INT>9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            URL url = new URL(source);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            Bitmap icon = BitmapFactory.decodeStream(inputStream);
            return icon;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private void logout(Button logoutbtn){
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setMessage("Vuoi davvero uscire?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                accetta();
                            }
                        });
                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
    }

    private void modificaCredenzialiBtn(Button modificaBtn){
        modificaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CredenzialiProfiloActivity.class));
            }
        });
    }

    public static String getUsernameText(FirebaseFirestore db , String uid) {
        final SettableFuture<DocumentSnapshot> future = SettableFuture.create();
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    future.set(document);
                }
            }
        });
        try{
            DocumentSnapshot ds = future.get();
            if(ds.exists())
                return ds.getString("username");
            else
                return "";
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getEmailText(FirebaseFirestore db , String uid) {
        final SettableFuture<DocumentSnapshot> future = SettableFuture.create();
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    future.set(document);
                }
            }
        });
        try{
            DocumentSnapshot ds = future.get();
            if(ds.exists())
                return ds.getString("email");
            else
                return "";
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getUrlImage(FirebaseFirestore db, String uid){
        final SettableFuture<DocumentSnapshot> future = SettableFuture.create();
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    future.set(document);
                }
            }
        });
        try{
            DocumentSnapshot ds = future.get();
            if(ds.exists())
                return ds.getString("imageUrl");
            else
                return "";
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void VisualizzaPreferiti(){
        visualizzaPreferiti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ListeUtenteActivity.class);
                ListeUtenteActivity.filmDaV = filmDavedere;
                ListeUtenteActivity.filmPre = filmPreferiti;
                startActivity(i);
            }
        });
    }

    private void ModificaImmagine(){
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED){
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    }else{
                        pickImageFromGallery();
                    }
                }else{
                    pickImageFromGallery();
                }
            }
        });
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery();
                }else{
                    Toast.makeText(getContext(), "Permesso Negato", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                circleImageView.setImageBitmap(bitmap);
                StorageReference storageRef = storage.getReference();
                StorageReference riversRef = storageRef.child("images/" + imageUri.getLastPathSegment());
                riversRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        riversRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                String profiloImageUrl = task.getResult().toString();
                                db.collection("users").document(curUser).update("imageUrl", profiloImageUrl);
                            }
                        });
                    }
                });
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void accetta() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        Toast.makeText(getActivity(), "Logout effettuato!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }
}