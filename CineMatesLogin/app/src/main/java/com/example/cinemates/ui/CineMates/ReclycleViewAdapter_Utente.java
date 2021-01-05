package com.example.cinemates.ui.CineMates;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemates.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReclycleViewAdapter_Utente extends RecyclerView.Adapter<ReclycleViewAdapter_Utente.MyViewHolder> {

    Context mContext;
    List<ItemUser> userList;

    public ReclycleViewAdapter_Utente(Context mContext, List<ItemUser> userList) {
        this.mContext = mContext;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_user, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);


        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.username.setText(userList.get(position).getUsername());
        holder.circleImageView.setImageBitmap(userList.get(position).getBitmap());

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView username;
        private CircleImageView circleImageView;
        public MyViewHolder(View itemView){
            super(itemView);

            username = itemView.findViewById(R.id.usernameUtente_Item_textView);
            circleImageView = itemView.findViewById(R.id.avatarUser_item_image);
        }
    }
}
