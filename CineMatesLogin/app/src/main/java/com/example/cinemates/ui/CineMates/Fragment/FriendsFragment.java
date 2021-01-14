package com.example.cinemates.ui.CineMates.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.friends.NotificheDialog;
import com.example.cinemates.ui.CineMates.friends.ItemUser;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    List<ItemUser> userList;
    private ImageView notifica;

    // TODO: Rename and change types of parameters

    public FriendsFragment() {
        // Required empty public constructor
    }

    public FriendsFragment(List<ItemUser> userList){
        this.userList = userList;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendsFragment newInstance(String param1, String param2) {
        FriendsFragment fragment = new FriendsFragment();
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
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        notifica = view.findViewById(R.id.notifica_fragment_friends);
        tabLayout = view.findViewById(R.id.tabLayout_fragment_friends);
        viewPager = view.findViewById(R.id.viewPager_fragment_friends);
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        //final NotificheDialog notificheDialog = new NotificheDialog(getActivity());



        adapter.AddFragment(new YourFriendsFragment(), "Lista Amici");
        adapter.AddFragment(new SearchFriendsFragment(userList), "Cerca Utenti");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_amici_focused);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_addfriends);

        ApriNotifiche();
        //ChiudiNotifiche();

        return view;
    }

    public void ApriNotifiche(){
        notifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                final NotificheDialog notificheDialog = new NotificheDialog(getActivity());
                showDialog(notificheDialog);
            }
        });
    }

    public void showDialog(DialogFragment dialogFragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        dialogFragment.show(fragmentManager, "dialog");
    }
}