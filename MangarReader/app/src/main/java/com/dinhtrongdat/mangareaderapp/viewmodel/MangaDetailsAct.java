package com.dinhtrongdat.mangareaderapp.viewmodel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dinhtrongdat.mangareaderapp.R;
import com.dinhtrongdat.mangareaderapp.adapter.ChapterAdapter;
import com.dinhtrongdat.mangareaderapp.adapter.MangaAdapter;
import com.dinhtrongdat.mangareaderapp.adapter.TagAdapter;
import com.dinhtrongdat.mangareaderapp.model.BannerManga;
import com.dinhtrongdat.mangareaderapp.model.Chapter;
import com.dinhtrongdat.mangareaderapp.model.Manga;
import com.dinhtrongdat.mangareaderapp.model.Tag;

import java.util.ArrayList;
import java.util.List;

public class MangaDetailsAct extends AppCompatActivity implements  ChapterAdapter.OnItemChapterClick {

    Manga manga;
    BannerManga bannerManga;

    Button btnReadManga;
    ImageView btnBack, btnShare, btnFavorite ;
    ImageView ivPosterManga, ivBannerManga;
    TextView tvNameManga, tvAuthor,tvDescription;
    RecyclerView rcvChapter, rcvTag;

    ChapterAdapter chapterAdapter;
    List<Chapter> listChapter;

    TagAdapter tagAdapter;
    List<Tag> listTag ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_details);

        initUI();
    }
    private  void initUI(){
        btnReadManga = findViewById(R.id.btn_read_manga);
        btnBack = findViewById(R.id.btn_back);
        btnShare = findViewById(R.id.btn_share);
        btnFavorite = findViewById(R.id.btn_favorite);
        ivPosterManga = findViewById(R.id.img_poster_manga);
        ivBannerManga = findViewById(R.id.img_banner_manga);
        tvNameManga = findViewById(R.id.tv_name_manga);
        tvAuthor = findViewById(R.id.txt_author);
        tvDescription = findViewById(R.id.tv_description);
        rcvChapter = findViewById(R.id.rcv_chapter);
        rcvTag = findViewById(R.id.rcv_tag);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnReadManga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MangaDetailsAct.this, ViewMangaAct.class);
                intent.putExtra("chapter", listChapter.get(0));
                startActivity(intent);
            }
        });
        LoadDetails();
    }
    private void LoadDetails(){
        manga = (Manga) getIntent().getSerializableExtra("manga");
        bannerManga = (BannerManga) getIntent().getSerializableExtra("banner");
        if(manga != null)
        {
            Glide.with(this).load(manga.getImage()).into(ivPosterManga);
            Glide.with(this).load(manga.getBackdrop()).into(ivBannerManga);
            tvNameManga.setText(manga.getName().toString());
            tvAuthor.setText(manga.getAuthor().toString());
            tvDescription.setText(manga.getDescription().toString());
            listChapter = manga.getChapters();

            chapterAdapter = new ChapterAdapter(this,listChapter, this);
            chapterAdapter.notifyDataSetChanged();
            rcvChapter.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            rcvChapter.setAdapter(chapterAdapter);

            String[] tag = manga.getCategory().split("/");
            listTag = new ArrayList<>();
            for(String cate : tag){
                listTag.add(new Tag(cate));
            }
            tagAdapter = new TagAdapter(this, listTag);
            tagAdapter.notifyDataSetChanged();
            rcvTag.setLayoutManager(new GridLayoutManager(this, 2));
            rcvTag.setAdapter(tagAdapter);

            }
        else if(bannerManga != null){
            Glide.with(this).load(bannerManga.getImage()).into(ivPosterManga);
            Glide.with(this).load(bannerManga.getBackdrop()).into(ivBannerManga);
            tvNameManga.setText(bannerManga.getName().toString());
            tvAuthor.setText(bannerManga.getAuthor().toString());
            tvDescription.setText(bannerManga.getDescription().toString());
            listChapter = bannerManga.getChapters();

            chapterAdapter = new ChapterAdapter(this,listChapter, this);
            chapterAdapter.notifyDataSetChanged();
            rcvChapter.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            rcvChapter.setAdapter(chapterAdapter);

            String[] tag = bannerManga.getCategory().split("/");
            listTag = new ArrayList<>();
            for(String cate : tag){
                listTag.add(new Tag(cate));
            }
            tagAdapter = new TagAdapter(this, listTag);
            tagAdapter.notifyDataSetChanged();
            rcvTag.setLayoutManager(new GridLayoutManager(this, 2));
            rcvTag.setAdapter(tagAdapter);
        }
    }


    @Override
    public void onChapterItemClick(int clickedItemIndex) {
        Intent intent = new Intent(MangaDetailsAct.this, ViewMangaAct.class);
        intent.putExtra("chapter", listChapter.get(clickedItemIndex));
        startActivity(intent);

    }
}

