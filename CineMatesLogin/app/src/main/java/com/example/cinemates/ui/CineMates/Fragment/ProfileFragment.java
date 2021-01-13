package com.example.cinemates.ui.CineMates.Fragment;


import android.Manifest;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.CredenzialiProfiloActivity;
import com.example.cinemates.ui.CineMates.LoginActivity;
import com.example.cinemates.ui.CineMates.VisualizzaPreferitiActivity;
import com.facebook.login.LoginManager;
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
    private FirebaseFirestore db;
    private Button visualizzaPreferiti;
    private ImageView modifica;
    private CircleImageView circleImageView;
    private FirebaseStorage storage;
    String curUser;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

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
        visualizzaPreferiti = view.findViewById(R.id.visualizzaPreferiti_button_ProfileFragment);
        modifica = view.findViewById(R.id.modifica_icon_fragmentProfile);
        curUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db  = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

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
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                Toast.makeText(getActivity(), "Logout effettuato!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), LoginActivity.class));
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
                startActivity(new Intent(getActivity(), VisualizzaPreferitiActivity.class));
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
                                System.out.println(profiloImageUrl);
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


}