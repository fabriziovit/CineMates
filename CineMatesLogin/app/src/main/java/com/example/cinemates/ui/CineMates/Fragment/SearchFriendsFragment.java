package com.example.cinemates.ui.CineMates.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.friends.FriendRequest;
import com.example.cinemates.ui.CineMates.friends.ItemUser;
import com.example.cinemates.ui.CineMates.friends.RecycleViewAdapter_Utente;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class SearchFriendsFragment extends Fragment implements RecycleViewAdapter_Utente.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private List<ItemUser> userList;
    private ConstraintLayout constraintLayout;
    private EditText searchBar;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    public SearchFriendsFragment() {
        // Required empty public constructor
    }

    public SearchFriendsFragment(List<ItemUser> userList){
        this.userList = userList;
    }

    public static SearchFriendsFragment newInstance(String param1, String param2) {
        SearchFriendsFragment fragment = new SearchFriendsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_friends, container, false);

        searchBar = view.findViewById(R.id.searchBar_fragment_SearchFriends);
        constraintLayout = view.findViewById(R.id.container_fragment_searchfriends);
        recyclerView = view.findViewById(R.id.recycleView_fragment_SearchFriends);
        RecycleViewAdapter_Utente recycleViewAdapter = new RecycleViewAdapter_Utente(getContext(), userList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recycleViewAdapter);

        Keyboard();
        return view;
    }

    private void Keyboard() {
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(constraintLayout.getWindowToken(), 0);
                searchBar.clearFocus();
            }
        });
    }

    @Override
    public void OnClick(int position) {
        CollectionReference collectionReference = db.collection("users");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (userList.get(position).getUsername().equals(documentSnapshot.getString("username"))) {
                        String uIdDestinatario = documentSnapshot.getString("uid");
                        String uIdMittente = mAuth.getCurrentUser().getUid();
                        FieldValue timestamp = FieldValue.serverTimestamp();
                        FriendRequest friendRequest = new FriendRequest(uIdDestinatario, uIdMittente, timestamp);
                        DocumentReference documentReference = db.collection("friend request").document(uIdDestinatario);
                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists() && document.getString("uIdMittente").equals(uIdMittente)) {
                                        Toast.makeText(getActivity(), "Richiesta già inviata!", Toast.LENGTH_SHORT).show();
                                    }else {
                                        db.collection("friend request").document(uIdDestinatario).set(friendRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getActivity(), "Richista inviata correttamente!", Toast.LENGTH_SHORT).show();
                                                userList.get(position).setRapporto(1);
                                                // modficare in real time
                                            }
                                        });
                                    }
                                }
                            }
                        });
                        break;
                    }
                }
            }
        });
    }
}