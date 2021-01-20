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

public class RecycleViewAdapter_Film extends RecyclerView.Adapter<RecycleViewAdapter_Film.MyViewHolder> {

    Context mContext;
    List<ItemFilm> filmList;
    RecycleViewAdapter_Film.OnClickListener mOnClickListener;

    public RecycleViewAdapter_Film(Context mContext, List<ItemFilm> filmList, RecycleViewAdapter_Film.OnClickListener mOnClickListener){
        this.mContext = mContext;
        this.filmList = filmList;
        this.mOnClickListener = mOnClickListener;
    }

    @NonNull
    @Override
    public RecycleViewAdapter_Film.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_film, parent, false);
        RecycleViewAdapter_Film.MyViewHolder myViewHolder = new RecycleViewAdapter_Film.MyViewHolder(v, mOnClickListener);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.titolo.setText(filmList.get(position).getNome());
        //holder.anno.setText(filmList.get(position).getAnno());
        //holder.regista.setText(filmList.get(position).getRegista());
        //holder.genere.setText(filmList.get(position).getGenere());
        //holder.voto.setText(filmList.get(position).getVoto());
        holder.filmPic.setImageBitmap(filmList.get(position).getBitmap());
    }

    @Override
    public int getItemCount() {
        return filmList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView titolo;
        private TextView anno;
        private TextView regista;
        private TextView genere;
        private TextView voto;
        private ImageView filmPic;
        private TextView rimuoviFilm_Text;
        private ImageView rimuoviFilm_Image;
        RecycleViewAdapter_Film.OnClickListener onClickListener;

        public MyViewHolder(View itemView, RecycleViewAdapter_Film.OnClickListener onClickListener){
            super(itemView);

            titolo = itemView.findViewById(R.id.titoloFilm_item_textView);
            anno = itemView.findViewById(R.id.annoFilm_item_textView);
            regista = itemView.findViewById(R.id.registaFilm_item_textView);
            genere = itemView.findViewById(R.id.genereFilm_item_textView);
            voto = itemView.findViewById(R.id.votoFilm_item_textView);
            filmPic = itemView.findViewById(R.id.locandinaFilm_item_imageView);
            rimuoviFilm_Text = itemView.findViewById(R.id.rimuoviFilm_item_textView);
            rimuoviFilm_Image = itemView.findViewById(R.id.rimuoviFilm_item_imageView);
            this.onClickListener = onClickListener;

            titolo.setOnClickListener(this);
            anno.setOnClickListener(this);
            regista.setOnClickListener(this);
            genere.setOnClickListener(this);
            voto.setOnClickListener(this);
            filmPic.setOnClickListener(this);
            rimuoviFilm_Text.setOnClickListener(this);
            rimuoviFilm_Image.setOnClickListener(this);

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
