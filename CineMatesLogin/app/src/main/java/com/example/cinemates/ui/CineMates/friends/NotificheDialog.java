package com.example.cinemates.ui.CineMates.friends;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemates.R;
import com.example.cinemates.databinding.NotificheDialogBinding;
import com.example.cinemates.ui.CineMates.Fragment.ProfileFragment;

import java.util.ArrayList;
import java.util.List;

public class NotificheDialog extends DialogFragment implements RecycleViewAdapter_Richieste.OnClickListener {
    private NotificheDialogBinding binding;

    Activity activity;
    AlertDialog dialog;
    private List<ItemRichieste> richiesteList;

    public NotificheDialog(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.notifiche_dialog, container);
        richiesteList = new ArrayList<>();
        richiesteList.add(new ItemRichieste("Selioh", ProfileFragment.getBitmapFromdownload("https://image.flaticon.com/icons/png/128/1077/1077114.png")));
        richiesteList.add(new ItemRichieste("Lollo", ProfileFragment.getBitmapFromdownload("https://image.flaticon.com/icons/png/128/1077/1077114.png")));
        richiesteList.add(new ItemRichieste("Pazzo", ProfileFragment.getBitmapFromdownload("https://image.flaticon.com/icons/png/128/1077/1077114.png")));
        richiesteList.add(new ItemRichieste("Pizzo", ProfileFragment.getBitmapFromdownload("https://image.flaticon.com/icons/png/128/1077/1077114.png")));
        richiesteList.add(new ItemRichieste("Pizza", ProfileFragment.getBitmapFromdownload("https://image.flaticon.com/icons/png/128/1077/1077114.png")));
        richiesteList.add(new ItemRichieste("Pezzo", ProfileFragment.getBitmapFromdownload("https://image.flaticon.com/icons/png/128/1077/1077114.png")));
        richiesteList.add(new ItemRichieste("Ptzzo", ProfileFragment.getBitmapFromdownload("https://image.flaticon.com/icons/png/128/1077/1077114.png")));
        richiesteList.add(new ItemRichieste("Supercalifragilispiri", ProfileFragment.getBitmapFromdownload("https://image.flaticon.com/icons/png/128/1077/1077114.png")));
        richiesteList.add(new ItemRichieste("Pfizer", ProfileFragment.getBitmapFromdownload("https://image.flaticon.com/icons/png/128/1077/1077114.png")));
        RecycleViewAdapter_Richieste recycleViewAdapterRichieste = new RecycleViewAdapter_Richieste(getActivity(), richiesteList, this);
        RecyclerView recyclerView = view.findViewById(R.id.Richieste_Dialog);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recycleViewAdapterRichieste);

        return view;
    }

    public NotificheDialog(Activity myActivity){
        activity = myActivity;
    }

    public void startNotificheDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.notifiche_dialog, null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();
    }

    public void dismissDialog(){
        dialog.dismiss();
    }

    @Override
    public void onClickAccetta(int position) {
        richiesteList.get(position);

        System.out.println(" "+ "accetta "+position);
    }

    @Override
    public void onClickRifiuta(int position){
        richiesteList.get(position);

        System.out.println(" "+ "rifiuta "+position);
    }
}
