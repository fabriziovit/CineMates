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
import com.example.cinemates.ui.CineMates.model.ItemFilm;

import java.util.List;

public class RecycleViewAdapter_Film_ListaPreferiti_Amico extends RecyclerView.Adapter<RecycleViewAdapter_Film_ListaPreferiti_Amico.MyViewHolder> {

    Context mContext;
    List<ItemFilm> filmList;
    RecycleViewAdapter_Film_ListaPreferiti_Amico.OnClickListener mOnClickListener;

    public RecycleViewAdapter_Film_ListaPreferiti_Amico(Context mContext, List<ItemFilm> filmList, RecycleViewAdapter_Film_ListaPreferiti_Amico.OnClickListener mOnClickListener){
        this.mContext = mContext;
        this.filmList = filmList;
        this.mOnClickListener = mOnClickListener;
    }

    @NonNull
    @Override
    public RecycleViewAdapter_Film_ListaPreferiti_Amico.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_film_listapreferiti_amico, parent, false);
        RecycleViewAdapter_Film_ListaPreferiti_Amico.MyViewHolder myViewHolder = new RecycleViewAdapter_Film_ListaPreferiti_Amico.MyViewHolder(v, mOnClickListener);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.titolo.setText(filmList.get(position).getTitolo());
        holder.filmPic.setImageBitmap(filmList.get(position).getBitmap());
    }

    @Override
    public int getItemCount() {
        return filmList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView titolo;
        private ImageView filmPic;
        private ImageView rimuoviDaLista;
        private TextView rimuoviText;
        RecycleViewAdapter_Film_ListaPreferiti_Amico.OnClickListener onClickListener;

        public MyViewHolder(View itemView, RecycleViewAdapter_Film_ListaPreferiti_Amico.OnClickListener onClickListener){
            super(itemView);
            titolo = itemView.findViewById(R.id.titoloFilm_item_textView);
            filmPic = itemView.findViewById(R.id.locandinaFilm_item_imageView);

            titolo.setOnClickListener(this);
            filmPic.setOnClickListener(this);
            this.onClickListener = onClickListener;
        }

        @Override
        public void onClick(View view) {
            onClickListener.OnClickScheda(this.getLayoutPosition());
        }
    }

    public interface OnClickListener{
        void OnClickScheda(int position);
    }
}
