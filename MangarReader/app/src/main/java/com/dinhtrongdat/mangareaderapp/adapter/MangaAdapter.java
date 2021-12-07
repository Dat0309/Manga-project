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
import com.dinhtrongdat.mangareaderapp.model.Manga;

import java.util.List;

public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.ViewHolderManga> {

    Context context;
    List<Manga> Mangas;
    final private OnItemMangaClick mOnClick;

    public MangaAdapter(Context context, List<Manga> mangas, OnItemMangaClick mOnClick) {
        this.context = context;
        this.Mangas = mangas;
        this.mOnClick = mOnClick;
    }

    @NonNull
    @Override
    public ViewHolderManga onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.manga_item, parent, false);
        return new ViewHolderManga(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaAdapter.ViewHolderManga holder, int position) {
        Manga mange = Mangas.get(position);
        holder.txtName.setText(mange.getName());
        Glide.with(context).load(Mangas.get(position).getImage()).into(holder.imgManga);
    }

    @Override
    public int getItemCount() {
        return Mangas.size();
    }

    public static interface OnItemMangaClick{
        void onMangaItemClick(int clickedItemIndex);
    }

    public class ViewHolderManga extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgManga;
        TextView txtName;

        public ViewHolderManga(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imgManga = itemView.findViewById(R.id.img_manga);
            txtName = itemView.findViewById(R.id.txt_manga_name);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClick.onMangaItemClick(clickedPosition);
        }
    }
}
