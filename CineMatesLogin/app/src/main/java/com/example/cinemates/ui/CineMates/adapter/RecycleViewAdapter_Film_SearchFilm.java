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

public class RecycleViewAdapter_Film_SearchFilm extends RecyclerView.Adapter<RecycleViewAdapter_Film_SearchFilm.MyViewHolder> {

    Context mContext;
    List<ItemFilm> filmList;
    RecycleViewAdapter_Film_SearchFilm.OnClickListener mOnClickListener;

    public RecycleViewAdapter_Film_SearchFilm(Context mContext, List<ItemFilm> filmList, RecycleViewAdapter_Film_SearchFilm.OnClickListener mOnClickListener){
        this.mContext = mContext;
        this.filmList = filmList;
        this.mOnClickListener = mOnClickListener;
    }

    @NonNull
    @Override
    public RecycleViewAdapter_Film_SearchFilm.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_film_searchfilm, parent, false);
        RecycleViewAdapter_Film_SearchFilm.MyViewHolder myViewHolder = new RecycleViewAdapter_Film_SearchFilm.MyViewHolder(v, mOnClickListener);

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
        RecycleViewAdapter_Film_SearchFilm.OnClickListener onClickListener;

        public MyViewHolder(View itemView, RecycleViewAdapter_Film_SearchFilm.OnClickListener onClickListener){
            super(itemView);
            titolo = itemView.findViewById(R.id.titoloFilm_item_textView);
            filmPic = itemView.findViewById(R.id.locandinaFilm_item_imageView);
            this.onClickListener = onClickListener;
            titolo.setOnClickListener(this);
            filmPic.setOnClickListener(this);
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
