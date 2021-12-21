package com.dinhtrongdat.mangareaderapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dinhtrongdat.mangareaderapp.R;
import com.dinhtrongdat.mangareaderapp.adapter.ChapterAdapter;
import com.dinhtrongdat.mangareaderapp.adapter.MangaAdapter;
import com.dinhtrongdat.mangareaderapp.adapter.TagAdapter;
import com.dinhtrongdat.mangareaderapp.model.BannerManga;
import com.dinhtrongdat.mangareaderapp.model.Chapter;
import com.dinhtrongdat.mangareaderapp.model.FavoriteManga;
import com.dinhtrongdat.mangareaderapp.model.Manga;
import com.dinhtrongdat.mangareaderapp.model.Tag;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MangaDetailsAct extends AppCompatActivity implements  ChapterAdapter.OnItemChapterClick{

    /**
     * Model
     */
    Manga manga;
    BannerManga bannerManga;
    FavoriteManga favorite;
    boolean IS_ADD = false;

    /**
     * View
     */
    Button btnReadManga;
    ImageView btnBack, btnShare, btnFavorite ;
    ImageView ivPosterManga, ivBannerManga;
    TextView tvNameManga, tvAuthor,tvDescription;
    RecyclerView rcvChapter, rcvTag;

    /**
     * Adapter
     */
    ChapterAdapter chapterAdapter;
    List<Chapter> listChapter;
    TagAdapter tagAdapter;
    List<Tag> listTag ;

    /**
     * Database
     */
    DatabaseReference reference;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_details);
        user = FirebaseAuth.getInstance().getCurrentUser();
        favorite = (FavoriteManga) getIntent().getSerializableExtra("fav");
        manga = (Manga) getIntent().getSerializableExtra("manga");
        bannerManga = (BannerManga) getIntent().getSerializableExtra("banner");

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

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!IS_ADD)
                    AddToFavorite(user);
                else
                    Toast.makeText(MangaDetailsAct.this, "Truyện đã tồn tại trong mục ưa thích", Toast.LENGTH_LONG).show();
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, manga.getName());
                intent.putExtra(Intent.EXTRA_SUBJECT, manga.getName());
                intent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(intent, null);
                startActivity(shareIntent);
            }
        });
    }
    private void LoadDetails(){
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

    /**
     * Hàm kiểm tra truyện có nằm trong danh sách yêu thích của người dùng không
     */
    private void CheckFav(){
        FavoriteManga fav;
        if(manga != null)
            fav = new FavoriteManga(manga.getName(), manga.getImage(), manga.getCategory(), manga.getDescription(), manga.getAuthor(),manga.getBackdrop(), user.getUid(), manga.getChapters());
        else
            fav = favorite;

        reference = FirebaseDatabase.getInstance().getReference("favorite");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    FavoriteManga favorite = data.getValue(FavoriteManga.class);
                    if(favorite.getUid().compareTo(fav.getUid())==0){
                        if(String.valueOf(favorite.getName()).compareTo(String.valueOf(fav.getName())) == 0){
                            IS_ADD = true;
                            btnFavorite.setImageResource(R.drawable.ic_favorite_red);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Hàm thêm truyện vào danh sách truyện yêu thích của người dùng
     * @param user tài khoản người dùng
     */
    private void AddToFavorite(FirebaseUser user){
        FavoriteManga fav;
        if(manga != null)
            fav = new FavoriteManga(manga.getName(), manga.getImage(), manga.getCategory(), manga.getDescription(), manga.getAuthor(),manga.getBackdrop(), user.getUid(), manga.getChapters());
        else
            fav = favorite;

        reference = FirebaseDatabase.getInstance().getReference("favorite");
        reference.push().setValue(fav).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MangaDetailsAct.this, "Đã thêm vào mục yêu thích", Toast.LENGTH_SHORT).show();
                    IS_ADD = true;
                    btnFavorite.setImageResource(R.drawable.ic_favorite_red);
                }
                else{
                    Toast.makeText(MangaDetailsAct.this, "Thất bại", Toast.LENGTH_SHORT).show();
                    IS_ADD = false;
                    btnFavorite.setImageResource(R.drawable.ic_favorite);
                }
            }
        });
    }

    @Override
    public void onChapterItemClick(int clickedItemIndex) {
        Intent intent = new Intent(MangaDetailsAct.this, ViewMangaAct.class);
        intent.putExtra("chapter", listChapter.get(clickedItemIndex));
        startActivity(intent);

    }
}

