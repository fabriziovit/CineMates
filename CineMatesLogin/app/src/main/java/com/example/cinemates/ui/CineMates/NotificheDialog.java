package com.example.cinemates.ui.CineMates;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cinemates.R;
import com.example.cinemates.databinding.NotificheDialogBinding;
import com.example.cinemates.ui.CineMates.Fragment.ProfileFragment;

import java.util.ArrayList;
import java.util.List;

public class NotificheDialog extends AppCompatActivity implements RecycleViewAdapter_Richieste.OnClickListener{
    private NotificheDialogBinding binding;

    Activity activity;
    AlertDialog dialog;
    private List<ItemRichieste> richiesteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NotificheDialogBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        richiesteList = new ArrayList<>();
        richiesteList.add(new ItemRichieste("Selioh", ProfileFragment.getBitmapFromdownload("https://image.flaticon.com/icons/png/128/1077/1077114.png")));
        richiesteList.add(new ItemRichieste("Lollo", ProfileFragment.getBitmapFromdownload("https://image.flaticon.com/icons/png/128/1077/1077114.png")));
        RecycleViewAdapter_Richieste recycleViewAdapterRichieste = new RecycleViewAdapter_Richieste(this, richiesteList, this);
        binding.RichiesteDialog.setLayoutManager(new LinearLayoutManager(this));
        binding.RichiesteDialog.setAdapter(recycleViewAdapterRichieste);


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
    public void OnClick(int position) {
        richiesteList.get(position);

        System.out.println(" "+ position);
    }
}
