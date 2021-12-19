package com.dinhtrongdat.mangareaderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.dinhtrongdat.mangareaderapp.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

public class MyViewPagerAdapter extends PagerAdapter {

    Context context;
    List<String> images;

    public MyViewPagerAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
    }


    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_pager_item, container, false);

        PhotoView image = view.findViewById(R.id.pager_image);
        Glide.with(context).load(images.get(position)).into(image);

        container.addView(view);
        return view;
    }
}
