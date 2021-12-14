package com.dinhtrongdat.mangareaderapp.viewmodel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dinhtrongdat.mangareaderapp.R;
import com.dinhtrongdat.mangareaderapp.adapter.ChapterAdapter;
import com.dinhtrongdat.mangareaderapp.adapter.MangaAdapter;
import com.dinhtrongdat.mangareaderapp.model.BannerManga;
import com.dinhtrongdat.mangareaderapp.model.Chapter;
import com.dinhtrongdat.mangareaderapp.model.Manga;

import java.util.List;

public class MangaDetailsAct extends AppCompatActivity implements  ChapterAdapter.OnItemChapterClick {

    Manga manga;

    Button btnReadManga;
    ImageView btnBack, btnShare, btnFavorite ;
    ImageView ivPosterManga, ivBannerManga;
    TextView tvNameManga, tvAuthor,tvDescription;
    View vType;
    RecyclerView rcvChapter;

    ChapterAdapter chapterAdapter;
    List<Chapter> listChapter;





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
        vType = findViewById(R.id.tv_type);
        tvDescription = findViewById(R.id.tv_description);
        rcvChapter = findViewById(R.id.rcv_chapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        LoadDetails();
    }
    private void LoadDetails(){
        manga = (Manga) getIntent().getSerializableExtra("manga");
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



            }
        }

    @Override
    public void onChapterItemClick(int clickedItemIndex) {

    }
}

