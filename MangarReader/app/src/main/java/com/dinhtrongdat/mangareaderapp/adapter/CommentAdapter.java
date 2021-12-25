package com.dinhtrongdat.mangareaderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dinhtrongdat.mangareaderapp.R;
import com.dinhtrongdat.mangareaderapp.model.Rating;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolderManga> implements Filterable {

    Context context;
    List<Rating> Ratings;

    public CommentAdapter(Context context, List<Rating> rating) {
        this.context = context;
        this.Ratings = rating;

    }
    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolderManga onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_cmt, parent, false);
        return new ViewHolderManga(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolderManga holder, int position) {
        String uriImage = "https://firebasestorage.googleapis.com/v0/b/manga-ctk43.appspot.com/o/avatar_user%2Fdefaultuser.png?alt=media&token=6e7f08a6-aeed-4ff3-8918-8a9ee7d95cd9";
        Rating rating = Ratings.get(position);

        holder.tvCmt.setText(rating.getComment());
        holder.rbRating.setRating(Float.parseFloat(rating.getRateValue()));
        Glide.with(context).load(uriImage).into(holder.imgUser);
    }

    @Override
    public int getItemCount() {
        return Ratings.size();
    }

    public class ViewHolderManga extends RecyclerView.ViewHolder {
        ImageView imgUser;
        TextView txtNameUser;
        TextView tvCmt;
        RatingBar rbRating;
        public ViewHolderManga(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.img_user_cmt);
            txtNameUser = itemView.findViewById(R.id.txt_name_user);
            tvCmt = itemView.findViewById(R.id.tv_cmt);
            rbRating = itemView.findViewById(R.id.ratingStar_cmt);
        }
    }
}
