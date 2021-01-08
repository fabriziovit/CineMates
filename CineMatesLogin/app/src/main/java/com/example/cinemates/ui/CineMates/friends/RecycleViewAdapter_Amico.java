package com.example.cinemates.ui.CineMates.friends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemates.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecycleViewAdapter_Amico extends RecyclerView.Adapter<RecycleViewAdapter_Amico.MyViewHolder> {

    Context mContext;
    List<ItemFriend> friendsList;
    RecycleViewAdapter_Amico.OnClickListener mOnClickListener;


    public RecycleViewAdapter_Amico(Context mContext, List<ItemFriend> friendsList, RecycleViewAdapter_Amico.OnClickListener mOnClickListener) {
        this.mContext = mContext;
        this.friendsList = friendsList;
        this.mOnClickListener = mOnClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_friend, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v, mOnClickListener);


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

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView username;
        private CircleImageView circleImageView;
        private ImageView imageView;
        private TextView textView;
        RecycleViewAdapter_Amico.OnClickListener onClickListener;

        public MyViewHolder(View itemView, RecycleViewAdapter_Amico.OnClickListener onClickListener){
            super(itemView);

            username = itemView.findViewById(R.id.usernameFriend_Item_textView);
            circleImageView = itemView.findViewById(R.id.avatarFriend_item_image);
            textView = itemView.findViewById(R.id.visualizzaPreferiti_item_textView);
            imageView = itemView.findViewById(R.id.visualizzapreferiti_item_imageView);
            this.onClickListener = onClickListener;

            textView.setOnClickListener(this);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickListener.OnClick(getAdapterPosition());
        }
    }


    public interface OnClickListener{
        void OnClick(int position);
    }
}
