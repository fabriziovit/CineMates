package com.example.cinemates.ui.CineMates.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.friends.FriendRequest;
import com.example.cinemates.ui.CineMates.friends.ItemUser;
import com.example.cinemates.ui.CineMates.friends.RecycleViewAdapter_Utenti;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import Intefaces.UpdateableFragmentListener;

public class SearchFriendsFragment extends Fragment implements RecycleViewAdapter_Utenti.OnClickListener, UpdateableFragmentListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView_Utenti;
    private List<ItemUser> userList;
    private ConstraintLayout constraintLayout;
    private EditText searchBar;
    private ImageView searchButton;
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
        searchButton = view.findViewById(R.id.searchButton_fragment_SearchFriends);
        constraintLayout = view.findViewById(R.id.container_fragment_searchfriends);
        recyclerView_Utenti = view.findViewById(R.id.recycleView_fragment_SearchFriends);
        RecycleViewAdapter_Utenti recycleViewAdapter = new RecycleViewAdapter_Utenti(getContext(), userList, this);
        recyclerView_Utenti.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_Utenti.setAdapter(recycleViewAdapter);

        Keyboard();
        cercaUtenti();
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
        if(userList.get(position).getRapporto() == 1){
            Toast.makeText(getActivity(), "Richiesta già inviata!", Toast.LENGTH_SHORT).show();
        }else if(userList.get(position).getRapporto() == 2){
            Toast.makeText(getActivity(), "Siete già amici!", Toast.LENGTH_SHORT).show();
        }else{
            String uIdDestinatario = userList.get(position).getUid();
            String uIdMittente = mAuth.getCurrentUser().getUid();
            FieldValue timestamp = FieldValue.serverTimestamp();
            FriendRequest friendRequest = new FriendRequest(uIdDestinatario, uIdMittente, timestamp);
            DocumentReference documentReference = db.collection("friend request").document(uIdMittente).collection(uIdDestinatario).document(uIdDestinatario);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    db.collection("friend request").document(uIdMittente).collection(uIdDestinatario).document(uIdDestinatario).set(friendRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getActivity(), "Richiesta inviata correttamente!", Toast.LENGTH_SHORT).show();
                            userList.get(position).setRapporto(1);
                            update();
                        }
                    });
                }
            });
        }
    }

    public void cercaUtenti(){
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(constraintLayout.getWindowToken(), 0);
                searchBar.clearFocus();
                List<ItemUser> searchList = new ArrayList<>();
                String ricerca = searchBar.getText().toString().toLowerCase();
                if(ricerca.length() != 0) {
                    for (int i = 0; i < userList.size(); i++) {
                        String username = userList.get(i).getUsername().toLowerCase();
                        if (username.contains(ricerca)) {
                            ItemUser userSearch = new ItemUser(userList.get(i).getUsername(), userList.get(i).getBitmap());
                            userSearch.setUid(userList.get(i).getUid());
                            userSearch.setRapporto(userList.get(i).getRapporto());
                            searchList.add(userSearch);
                        }
                    }
                    if(searchList.size() != 0) {
                        RecycleViewAdapter_Utenti recycleViewAdapter = new RecycleViewAdapter_Utenti(getContext(), searchList, SearchFriendsFragment.this);
                        recyclerView_Utenti.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView_Utenti.setAdapter(recycleViewAdapter);
                        update();
                    }else{
                        RecycleViewAdapter_Utenti recycleViewAdapter = new RecycleViewAdapter_Utenti(getContext(), searchList, SearchFriendsFragment.this);
                        recyclerView_Utenti.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView_Utenti.setAdapter(recycleViewAdapter);
                        update();
                        Toast.makeText(getContext(), "Nessun amico trovato con quel nome!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    RecycleViewAdapter_Utenti recycleViewAdapter = new RecycleViewAdapter_Utenti(getContext(), userList, SearchFriendsFragment.this);
                    recyclerView_Utenti.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView_Utenti.setAdapter(recycleViewAdapter);
                    update();
                    Toast.makeText(getContext(), "Nessun paramtrro di ricerca inserito!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void update() {
        recyclerView_Utenti.getAdapter().notifyDataSetChanged();
    }
}