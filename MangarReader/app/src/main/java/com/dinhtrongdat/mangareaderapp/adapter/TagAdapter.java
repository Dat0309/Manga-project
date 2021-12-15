package com.dinhtrongdat.mangareaderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dinhtrongdat.mangareaderapp.R;
import com.dinhtrongdat.mangareaderapp.model.Tag;

import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolderTag> {
    Context context;
    List<Tag> listTag;

    public  TagAdapter(Context context, List<Tag> listTag){
        this.context = context;
        this.listTag = listTag;
    }
    @NonNull
    @Override
    public ViewHolderTag onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tag, parent, false);
        return new ViewHolderTag(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTag holder, int position) {
        Tag tag = listTag.get(position);
        holder.txtTagName.setText("#"+tag.getTag());

    }

    @Override
    public int getItemCount() {
        return listTag.size();
    }

    public class ViewHolderTag extends  RecyclerView.ViewHolder {
        TextView txtTagName;
        public ViewHolderTag(@NonNull View itemView) {
            super(itemView);
            txtTagName = itemView.findViewById(R.id.txtTagName);
        }
    }
}
