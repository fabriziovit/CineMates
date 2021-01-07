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
import com.example.cinemates.ui.CineMates.friends.ReclycleViewAdapter_Utente;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFriendsFragment extends Fragment implements ReclycleViewAdapter_Utente.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private List<ItemUser> userList;
    private ConstraintLayout constraintLayout;
    private EditText searchBar;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFriendsFragment() {
        // Required empty public constructor
    }

    public SearchFriendsFragment(List<ItemUser> userList){
        this.userList = userList;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFriendsFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        ReclycleViewAdapter_Utente reclycleViewAdapter = new ReclycleViewAdapter_Utente(getContext(), userList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(reclycleViewAdapter);


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
        userList.get(position);

        new Thread(()-> {
            CollectionReference collectionReference = db.collection("users");
            collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    int n = 0;
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (userList.get(position).getUsername().equals(documentSnapshot.getString("username"))) {
                            String uIdDestinatario = documentSnapshot.getString("uid");
                            String uIdMittente = mAuth.getCurrentUser().getUid();
                            Long tsLong = System.currentTimeMillis()/1000;
                            String ts = tsLong.toString();
                            FriendRequest friendRequest = new FriendRequest(uIdDestinatario, uIdMittente, ts);
                            db.collection("friend request").document(uIdDestinatario).set(friendRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getActivity(), "RICHIESTA INVIATA CORRETTAMENTE!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            break;
                        }
                    }
                }
            });
        }).start();
    }
}