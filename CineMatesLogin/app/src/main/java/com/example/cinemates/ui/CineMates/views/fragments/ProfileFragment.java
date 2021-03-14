package com.example.cinemates.ui.CineMates.views.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
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
import com.example.cinemates.ui.CineMates.presenters.fragments.ProfilePresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.SettableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    public Bitmap bitmap;
    public String usernameText;
    public String emailText;
    private FirebaseFirestore db;
    public Button visualizzaPreferiti;
    public CircleImageView circleImageView;
    private FirebaseStorage storage;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    public String curUser;
    private ProfilePresenter profilePresenter;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public ProfileFragment(Bitmap profilePic, String usernameText, String emailText){
        this.bitmap = profilePic;
        this.usernameText = usernameText;
        this.emailText = emailText;
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

        profilePresenter = new ProfilePresenter(this, db, storage);

        emailTextView.setText(emailText);
        usernameTextView.setText(usernameText);

        profilePresenter.ModificaImmagine();
        profilePresenter.VisualizzaPreferiti();
        profilePresenter.modificaCredenzialiBtn(modificaCredenzialiBtn);
        profilePresenter.logout(logoutbtn);
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    profilePresenter.pickImageFromGallery();
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
}