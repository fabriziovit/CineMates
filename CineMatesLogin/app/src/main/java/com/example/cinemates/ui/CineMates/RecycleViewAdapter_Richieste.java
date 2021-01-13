package com.example.cinemates.ui.CineMates;

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

public class RecycleViewAdapter_Richieste extends RecyclerView.Adapter<RecycleViewAdapter_Richieste.MyViewHolder> {

    Context mContext;
    List<ItemRichieste> richiesteList;
    RecycleViewAdapter_Richieste.OnClickListener mOnClickListener;

    public RecycleViewAdapter_Richieste(Context mContext, List<ItemRichieste> richiesteList, RecycleViewAdapter_Richieste.OnClickListener mOnClickListener) {
        this.mContext = mContext;
        this.richiesteList = richiesteList;
        this.mOnClickListener = mOnClickListener;
    }


    @NonNull
    @Override
    public RecycleViewAdapter_Richieste.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_richieste, parent, false);
        RecycleViewAdapter_Richieste.MyViewHolder myViewHolder = new RecycleViewAdapter_Richieste.MyViewHolder(v, mOnClickListener);


        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter_Richieste.MyViewHolder holder, int position) {
        holder.username.setText(richiesteList.get(position).getUsername());
        holder.circleImageView.setImageBitmap(richiesteList.get(position).getBitmap());

    }

    @Override
    public int getItemCount() {
        return richiesteList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView username;
        private CircleImageView circleImageView;
        private ImageView accetta;
        private ImageView rifiuta;
        RecycleViewAdapter_Richieste.OnClickListener onClickListener;

        public MyViewHolder(View itemView, RecycleViewAdapter_Richieste.OnClickListener onClickListener){
            super(itemView);

            username = itemView.findViewById(R.id.usernameRichiesta_Item_textView);
            circleImageView = itemView.findViewById(R.id.avatarRichiesta_item_image);
            accetta = itemView.findViewById(R.id.accetta_item_imageView);
            rifiuta = itemView.findViewById(R.id.rifiuta_item_ImageView);

            rifiuta.setOnClickListener(this);
            accetta.setOnClickListener(this);
            this.onClickListener = onClickListener;


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
