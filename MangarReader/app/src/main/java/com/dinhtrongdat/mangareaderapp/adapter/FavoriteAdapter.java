package com.dinhtrongdat.mangareaderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dinhtrongdat.mangareaderapp.R;
import com.dinhtrongdat.mangareaderapp.model.FavoriteManga;

import java.util.List;

/**
 * Lớp FavoriteAdapter định nghĩa phương thức render ảnh lên danh sách truyện yêu thích
 */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private Context context;
    private List<FavoriteManga> listItem;
    final private ListFavoriteItemClickListener mOnClickListener;

    public FavoriteAdapter(Context context, List<FavoriteManga> listItem, ListFavoriteItemClickListener mOnClickListener) {
        this.context = context;
        this.listItem = listItem;
        this.mOnClickListener = mOnClickListener;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_fav, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.FavoriteViewHolder holder, int position) {
        FavoriteManga fav = listItem.get(position);

        holder.txtName.setText(fav.getName());
        holder.txtAuthor.setText(fav.getAuthor());
        Glide.with(context).load(fav.getBackdrop()).into(holder.imgPoster);
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public interface ListFavoriteItemClickListener{
        void onFavoriteItemClick(int clickedItemIndex);
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtName, txtAuthor;
        ImageView imgPoster;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txtName = itemView.findViewById(R.id.tv_title_fav);
            txtAuthor = itemView.findViewById(R.id.tv_author_fav);
            imgPoster = itemView.findViewById(R.id.iv_poster_fav);
        }

        @Override
        public void onClick(View v) {
            int clickedInx = getAdapterPosition();
            mOnClickListener.onFavoriteItemClick(clickedInx);
        }
    }
}
