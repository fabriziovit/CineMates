package com.example.cinemates.ui.CineMates.Fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.SettableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Bitmap bitmap;
    private String usernameText;
    private String emailText;
    private static FirebaseFirestore db;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public ProfileFragment(Bitmap profilePic, String usernameText, String emailText){
        this.bitmap = profilePic;
        this.usernameText = usernameText;
        this.emailText = emailText;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        CircleImageView circleImageView = view.findViewById(R.id.avatar_profile_fragment);
        circleImageView.setImageBitmap(bitmap);
        Button logoutbtn =  view.findViewById(R.id.logout_profile_fragment);
        TextView usernameTextView = view.findViewById(R.id.username_profile_fragment);
        TextView emailTextView = view.findViewById(R.id.email_profile_fragment);
        emailTextView.setText(emailText);
        usernameTextView.setText(usernameText);
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
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getActivity(), "Logout effettuato!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getActivity(), LoginActivity.class));
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


    //Settare il nome utente e l'email dell'utente con chiamata dal database

}