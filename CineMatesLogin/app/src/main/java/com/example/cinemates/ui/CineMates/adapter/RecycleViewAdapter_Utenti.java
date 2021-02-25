package com.example.cinemates.ui.CineMates.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.friends.model.ItemUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecycleViewAdapter_Utenti extends RecyclerView.Adapter<RecycleViewAdapter_Utenti.MyViewHolder> {

    Context mContext;
    List<ItemUser> userList;
    RecycleViewAdapter_Utenti.OnClickListener mOnClickListener;

    public RecycleViewAdapter_Utenti(Context mContext, List<ItemUser> userList, OnClickListener mOnClickListener) {
        this.mContext = mContext;
        this.userList = userList;
        this.mOnClickListener = mOnClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_user, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v, mOnClickListener);


        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.username.setText(userList.get(position).getUsername());
        holder.circleImageView.setImageBitmap(userList.get(position).getBitmap());
        if(userList.get(position).getRapporto() == 0) {
            holder.textView.setText("Invia richiesta di amicizia");
            holder.imageView.setImageResource(R.drawable.ic_aggiungi);
        }else if(userList.get(position).getRapporto() == 1) {
            holder.textView.setText("Richiesta inviata");
            holder.imageView.setImageResource(R.drawable.ic_attendere);
        }else if(userList.get(position).getRapporto() == 3) {
            holder.textView.setText("Richiesta già presente");
            holder.imageView.setImageResource(R.drawable.ic_richiesta_ricevuta);
        }else {
            holder.textView.setText("Amico");
            holder.imageView.setImageResource(R.drawable.ic_checked);
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView username;
        private CircleImageView circleImageView;
        private ImageView imageView;
        private TextView textView;
        OnClickListener onClickListener;


        public MyViewHolder(View itemView, OnClickListener onClickListener){
            super(itemView);

            username = itemView.findViewById(R.id.usernameUtente_Item_textView);
            circleImageView = itemView.findViewById(R.id.avatarUser_item_image);
            textView = itemView.findViewById(R.id.rapporto_item_textView);
            imageView = itemView.findViewById(R.id.rapporto_item_imageView);

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

