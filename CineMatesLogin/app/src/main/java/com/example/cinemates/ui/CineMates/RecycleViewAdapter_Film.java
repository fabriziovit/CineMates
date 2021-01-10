package com.example.cinemates.ui.CineMates;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    }

    @Override
    public int getItemCount() {
        return filmList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        RecycleViewAdapter_Film.OnClickListener onClickListener;

        public MyViewHolder(View itemView, RecycleViewAdapter_Film.OnClickListener onClickListener){
            super(itemView);

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
