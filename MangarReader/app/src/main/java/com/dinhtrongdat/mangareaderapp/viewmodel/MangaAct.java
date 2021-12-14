package com.dinhtrongdat.mangareaderapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.dinhtrongdat.mangareaderapp.R;
import com.dinhtrongdat.mangareaderapp.adapter.BannerAdapter;
import com.dinhtrongdat.mangareaderapp.adapter.MangaAdapter;
import com.dinhtrongdat.mangareaderapp.model.BannerManga;
import com.dinhtrongdat.mangareaderapp.model.Manga;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import dmax.dialog.SpotsDialog;

public class MangaAct extends AppCompatActivity implements MangaAdapter.OnItemMangaClick {

    /**
     * Adapter
     */
    BannerAdapter bannerAdapter;
    MangaAdapter mangaAdapter;

    /**
     * View
     */
    ViewPager viewPager;
    RecyclerView rcvItem;
    AppBarLayout appBar;
    TabLayout tabIndicater;

    /**
     * Danh sách quảng cáo, truyện.
     */
    List<BannerManga> Banners;
    List<Manga> Mangas;

    /**
     * Database
     */
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga);
        
        initUI();

    }

    private void initUI() {
        UploadBanner();
        UploadMangaItem();
    }

    /**
     * Lấy list truyện trên database
     */
    private void UploadMangaItem() {
        android.app.AlertDialog alertDialog = new SpotsDialog.Builder().setContext(this)
                .setCancelable(false)
                .setMessage("Chờ xíu")
                .build();

        alertDialog.show();
        Mangas = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Comic");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    Manga manga = data.getValue(Manga.class);
                    String[] category = manga.getCategory().split("/");
                    Mangas.add(manga);
                }
                setMangaAdapter(Mangas);
                alertDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setMangaAdapter(List<Manga> mangas) {
        rcvItem = findViewById(R.id.rcv_item);
        mangaAdapter = new MangaAdapter(MangaAct.this, mangas, this);

        rcvItem.setLayoutManager(new GridLayoutManager(this, 2));
        rcvItem.setAdapter(mangaAdapter);
        mangaAdapter.notifyDataSetChanged();
    }

    /**
     * Lấy list banner trên database
     */
    private void UploadBanner() {
        Banners = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Banners");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    BannerManga bannerManga = data.getValue(BannerManga.class);
                    String[] category = bannerManga.getCategory().split("/");
                    Banners.add(bannerManga);
                }
                setBannerAdapter(Banners);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setBannerAdapter(List<BannerManga> banners) {
        viewPager = findViewById(R.id.banner_view_pagger);
        tabIndicater = findViewById(R.id.tab_indicator);
        bannerAdapter = new BannerAdapter(MangaAct.this, banners);
        bannerAdapter.notifyDataSetChanged();

        viewPager.setAdapter(bannerAdapter);
        tabIndicater.setupWithViewPager(viewPager);

        Timer autoSlider = new Timer();
        autoSlider.schedule(new AutoSlider(banners), 4000, 6000);
        tabIndicater.setupWithViewPager(viewPager, true);
    }

    /**
     * Sự kiện click chọn manga
     * @param clickedItemIndex
     */
    @Override
    public void onMangaItemClick(int clickedItemIndex) {
        Intent intent = new Intent(MangaAct.this, MangaDetailsAct.class);
        intent.putExtra("manga",Mangas.get(clickedItemIndex));
        startActivity(intent);
    }


    /**
     * Lớp kế thừa TimerTask, định nghĩa phương thức xử lý tự động chạy của banner.
     */
    public class AutoSlider extends TimerTask{

        List<BannerManga> list;

        public AutoSlider(List<BannerManga> list) {
            this.list = list;
        }

        @Override
        public void run() {
            MangaAct.this.runOnUiThread(() ->{
                if(viewPager.getCurrentItem() < list.size()-1){
                    viewPager.setCurrentItem(viewPager.getCurrentItem() +1);
                }else{
                    viewPager.setCurrentItem(0);
                }
            });
        }
    }
}