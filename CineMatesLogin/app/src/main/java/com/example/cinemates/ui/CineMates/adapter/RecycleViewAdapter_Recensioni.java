package com.example.cinemates.ui.CineMates.adapter;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemates.R;
import com.example.cinemates.ui.CineMates.model.ItemRecensione;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecycleViewAdapter_Recensioni extends RecyclerView.Adapter<RecycleViewAdapter_Recensioni.MyViewHolder>{
    Context mContext;
    List<ItemRecensione> recensioniList;
    RecycleViewAdapter_Recensioni.OnClickListener mOnClickListener;

    public RecycleViewAdapter_Recensioni(Context mContext, List<ItemRecensione> recensioniList, RecycleViewAdapter_Recensioni.OnClickListener mOnClickListener) {
        this.mContext = mContext;
        this.recensioniList = recensioniList;
        this.mOnClickListener = mOnClickListener;
    }

    @NonNull
    @Override
    public RecycleViewAdapter_Recensioni.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_recensione, parent, false);
        RecycleViewAdapter_Recensioni.MyViewHolder myViewHolder = new RecycleViewAdapter_Recensioni.MyViewHolder(v, mOnClickListener);


        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter_Recensioni.MyViewHolder holder, int position) {
        holder.username.setText(recensioniList.get(position).getUsername());
        holder.circleImageView.setImageBitmap(recensioniList.get(position).getBitmap());
        String voto = String.valueOf(recensioniList.get(position).getVoto());
        holder.voto.setText(voto);
        holder.recensione.setText(recensioniList.get(position).getRecensione());
    }

    @Override
    public int getItemCount() {
        return recensioniList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView username;
        private CircleImageView circleImageView;
        private TextView recensione;
        private TextView voto;
        RecycleViewAdapter_Recensioni.OnClickListener onClickListener;

        public MyViewHolder(View itemView, RecycleViewAdapter_Recensioni.OnClickListener onClickListener) {
            super(itemView);

            username = itemView.findViewById(R.id.usernameRecensione_item_textView);
            circleImageView = itemView.findViewById(R.id.avatarRecensione_item_imageView);
            voto = itemView.findViewById(R.id.votoRecensione_item_textView);
            recensione = itemView.findViewById(R.id.recensioneFilm_item_textView);
            recensione.setMovementMethod(new ScrollingMovementMethod());
            this.onClickListener = onClickListener;

            username.setOnClickListener(this);
            circleImageView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onClickListener.OnClick(getAdapterPosition());
        }
    }
    public interface OnClickListener {
        void OnClick(int position);
    }
}
