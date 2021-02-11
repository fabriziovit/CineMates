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

public class RecycleViewAdapter_Film_ListaPreferiti extends RecyclerView.Adapter<RecycleViewAdapter_Film_ListaPreferiti.MyViewHolder> {

    Context mContext;
    List<ItemFilm> filmList;
    RecycleViewAdapter_Film_ListaPreferiti.OnClickListener mOnClickListener;

    public RecycleViewAdapter_Film_ListaPreferiti(Context mContext, List<ItemFilm> filmList, RecycleViewAdapter_Film_ListaPreferiti.OnClickListener mOnClickListener){
        this.mContext = mContext;
        this.filmList = filmList;
        this.mOnClickListener = mOnClickListener;
    }

    @NonNull
    @Override
    public RecycleViewAdapter_Film_ListaPreferiti.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_film_listapreferiti, parent, false);
        RecycleViewAdapter_Film_ListaPreferiti.MyViewHolder myViewHolder = new RecycleViewAdapter_Film_ListaPreferiti.MyViewHolder(v, mOnClickListener);

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
        RecycleViewAdapter_Film_ListaPreferiti.OnClickListener onClickListener;

        public MyViewHolder(View itemView, RecycleViewAdapter_Film_ListaPreferiti.OnClickListener onClickListener){
            super(itemView);
            titolo = itemView.findViewById(R.id.titoloFilm_item_textView);
            filmPic = itemView.findViewById(R.id.locandinaFilm_item_imageView);
            rimuoviDaLista = itemView.findViewById(R.id.rimuoviDaLista_ImageView_listaPreferiti);
            rimuoviText = itemView.findViewById(R.id.rimuoviDaLista_TextView_listaPreferiti);

            titolo.setOnClickListener(this);
            filmPic.setOnClickListener(this);
            rimuoviText.setOnClickListener(this);
            rimuoviDaLista.setOnClickListener(this);
            this.onClickListener = onClickListener;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.locandinaFilm_item_imageView:
                case R.id.titoloFilm_item_textView:
                    onClickListener.OnClickScheda(this.getLayoutPosition());
                    break;
                case R.id.rimuoviDaLista_TextView_listaPreferiti:
                case R.id.rimuoviDaLista_ImageView_listaPreferiti:
                    onClickListener.OnClickRimuovi(this.getLayoutPosition());
                    break;
                default:
                    break;
            }
        }
    }

    public interface OnClickListener{
        void OnClickScheda(int position);
        void OnClickRimuovi(int position);
    }
}
