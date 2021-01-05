package com.example.cinemates.ui.CineMates.friends;

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

public class ReclycleViewAdapter_Amico extends RecyclerView.Adapter<ReclycleViewAdapter_Amico.MyViewHolder> {

    Context mContext;
    List<ItemFriend> friendsList;

    public ReclycleViewAdapter_Amico(Context mContext, List<ItemFriend> friendsList) {
        this.mContext = mContext;
        this.friendsList = friendsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_friend, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);


        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.username.setText(friendsList.get(position).getUsername());
        holder.circleImageView.setImageBitmap(friendsList.get(position).getBitmap());

    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView username;
        private CircleImageView circleImageView;
        public MyViewHolder(View itemView){
            super(itemView);

            username = itemView.findViewById(R.id.usernameFriend_Item_textView);
            circleImageView = itemView.findViewById(R.id.avatarFriend_item_image);
        }
    }
}
