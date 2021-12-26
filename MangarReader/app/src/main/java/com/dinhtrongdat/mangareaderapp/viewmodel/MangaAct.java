package com.dinhtrongdat.mangareaderapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dinhtrongdat.mangareaderapp.R;
import com.dinhtrongdat.mangareaderapp.adapter.BannerAdapter;
import com.dinhtrongdat.mangareaderapp.adapter.MangaAdapter;
import com.dinhtrongdat.mangareaderapp.model.BannerManga;
import com.dinhtrongdat.mangareaderapp.model.Manga;
import com.dinhtrongdat.mangareaderapp.model.User;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class MangaAct extends AppCompatActivity implements MangaAdapter.OnItemMangaClick, NavigationView.OnNavigationItemSelectedListener {

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
    TabLayout tabIndicater;
    SearchView searchView;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    CircleImageView imgUser;
    TextView txtFullName, txtEmail;
    ImageView imgUpload;

    /**
     * Danh sách quảng cáo, truyện.
     */
    List<BannerManga> Banners;
    List<Manga> Mangas;

    /**
     * Database
     */
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga);
        drawerLayout = findViewById(R.id.drawerLayout);

        initUI();

    }

    private void initUI() {
        UploadBanner();
        UploadMangaItem();
        Search();
        NavSettup();
    }

    /**
     * Hook item navbar
     */
    private void NavSettup() {
        navigationView = findViewById(R.id.navbar);
        imgUser = navigationView.getHeaderView(0).findViewById(R.id.img_user);
        txtFullName = navigationView.getHeaderView(0).findViewById(R.id.txt_nav_name);
        txtEmail = navigationView.getHeaderView(0).findViewById(R.id.txt_user_name);
        imgUpload = navigationView.getHeaderView(0).findViewById(R.id.img_upload);

        navigationView.setNavigationItemSelectedListener(this);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        // Fetch data user in firebase
        database.getReference().child("Users").child(Objects.requireNonNull(auth.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User user = snapshot.getValue(User.class);
                    Glide.with(MangaAct.this).load(Objects.requireNonNull(user).getAvatar()).into(imgUser);
                    txtFullName.setText(user.getName());
                    txtEmail.setText(user.getUserName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imgUpload.setOnClickListener(v->{
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,11);
        });

        findViewById(R.id.img_menu).setOnClickListener(v -> ShowNavigationBar());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(Objects.requireNonNull(data).getData()!=null){
            Uri uri = data.getData();
            imgUser.setImageURI(uri);

            final StorageReference reference = storage.getReference().child("avatar_user").child(Objects.requireNonNull(auth.getUid()));
            reference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                Toast.makeText(MangaAct.this, "Đã cập nhật ảnh đại diện", Toast.LENGTH_SHORT).show();
                reference.getDownloadUrl().addOnSuccessListener(uri1 -> database.getReference().child("Users").child(auth.getUid()).child("avatar").setValue(uri1.toString()));
            });
        }
    }

    /**
     * Phương thức tìm kiếm
     */
    private void Search() {
        searchView = findViewById(R.id.edtSearch);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mangaAdapter.getFilter().filter(query);
                getFilterManga(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mangaAdapter.getFilter().filter(newText);
                getFilterManga(newText);
                return false;
            }
        });
    }

    /**
     * Hàm lấy truyện theo filter tìm kiếm
     * @param query tên truyện muốn tìm
     */
    private void getFilterManga(String query) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Comic");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(Mangas.size()!=0)
                    Mangas.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Manga manga = data.getValue(Manga.class);
                    if(manga.getName().toLowerCase().contains(query)){
                        String[] category = Objects.requireNonNull(manga).getCategory().split("/");
                        Mangas.add(manga);
                    }
                }
                //setMangaAdapter(Mangas);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
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
                if(Mangas.size()!=0)
                    Mangas.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Manga manga = data.getValue(Manga.class);
                    String[] category = Objects.requireNonNull(manga).getCategory().split("/");
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

    /**
     * set manga adapter
     * @param mangas manga
     */
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
                if(Banners.size()!=0)
                    Banners.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    BannerManga bannerManga = data.getValue(BannerManga.class);
                    String[] category = Objects.requireNonNull(bannerManga).getCategory().split("/");
                    Banners.add(bannerManga);
                }
                setBannerAdapter(Banners);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     *
     * set banner manga adapter
     * @param banners banner
     */
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
     *
     * @param clickedItemIndex index of manga
     */
    @Override
    public void onMangaItemClick(int clickedItemIndex) {
        Intent intent = new Intent(MangaAct.this, MangaDetailsAct.class);
        intent.putExtra("manga", Mangas.get(clickedItemIndex));
        startActivity(intent);
    }

    /**
     * SỰ kiện click chọn item trong navbar
     * Favorite: Xuất danh sách truyện ưa thích
     * Logout: Đăng xuất khỏi ứng dụng
     * @param item item on menu navigation
     * @return item
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_fav:
                startActivity(new Intent(MangaAct.this, FavoriteAct.class));
                drawerLayout.closeDrawer(GravityCompat.END);
                break;
            case R.id.nav_type:
                startActivity(new Intent(MangaAct.this, CategoryAct.class));
                drawerLayout.closeDrawer(GravityCompat.END);
                break;
            case R.id.nav_profile:
                startActivity(new Intent(MangaAct.this, InformationAct.class));
                drawerLayout.closeDrawer(GravityCompat.END);

                break;
            case R.id.nav_pass:
                startActivity(new Intent(MangaAct.this,PasswordAct.class));
                drawerLayout.closeDrawer(GravityCompat.END);

                break;
            case R.id.nav_logout:
                auth.signOut();
                finish();
                startActivity(new Intent(MangaAct.this, LoginAct.class));
                break;

        }
        return true;
    }


    /**
     * Hàm định nghĩa phương thức hiện NavigationBar
     */
    private void ShowNavigationBar() {
        drawerLayout.openDrawer(GravityCompat.END);
    }

    /**
     * Lớp kế thừa TimerTask, định nghĩa phương thức xử lý tự động chạy của banner.
     */
    public class AutoSlider extends TimerTask {

        List<BannerManga> list;

        public AutoSlider(List<BannerManga> list) {
            this.list = list;
        }

        @Override
        public void run() {
            MangaAct.this.runOnUiThread(() -> {
                if (viewPager.getCurrentItem() < list.size() - 1) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                } else {
                    viewPager.setCurrentItem(0);
                }
            });
        }
    }
}