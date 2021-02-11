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
import com.example.cinemates.ui.CineMates.friends.model.ItemFriend;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;



public class RecycleViewAdapter_Amici extends RecyclerView.Adapter<RecycleViewAdapter_Amici.MyViewHolder> {

    Context mContext;
    List<ItemFriend> friendsList;
    RecycleViewAdapter_Amici.OnClickListener mOnClickListener;


    public RecycleViewAdapter_Amici(Context mContext, List<ItemFriend> friendsList, RecycleViewAdapter_Amici.OnClickListener mOnClickListener) {
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
        private ImageView visualizzaPreferiti_Image;
        private TextView visualizzaPreferiti_Text;
        private ImageView rimuoviAmicoImageview;
        RecycleViewAdapter_Amici.OnClickListener onClickListener;

        public MyViewHolder(View itemView, RecycleViewAdapter_Amici.OnClickListener onClickListener){
            super(itemView);

            username = itemView.findViewById(R.id.usernameFriend_Item_textView);
            circleImageView = itemView.findViewById(R.id.avatarFriend_item_image);
            visualizzaPreferiti_Text = itemView.findViewById(R.id.visualizzaPreferiti_item_textView);
            visualizzaPreferiti_Image = itemView.findViewById(R.id.visualizzapreferiti_item_imageView);
            rimuoviAmicoImageview = itemView.findViewById(R.id.rimuoviamico_item_imageView);
            this.onClickListener = onClickListener;

            rimuoviAmicoImageview.setOnClickListener(this);
            visualizzaPreferiti_Text.setOnClickListener(this);
            visualizzaPreferiti_Image.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.visualizzapreferiti_item_imageView:
                case R.id.visualizzaPreferiti_item_textView:
                    onClickListener.onClickPreferiti(this.getLayoutPosition());
                    break;
                case R.id.rimuoviamico_item_imageView:
                    onClickListener.onClickRimuoviAmico(this.getLayoutPosition());
                default:
                    break;
            }
        }
    }

    public interface OnClickListener{
        void onClickPreferiti(int position);
        void onClickRimuoviAmico(int position);
    }

}
