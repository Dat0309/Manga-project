package com.dinhtrongdat.mangareaderapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
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
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
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
    AppBarLayout appBar;
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

    List<Manga> MangasTragedy;
    List<Manga> MangasMature;
    List<Manga> MangasSliceOfLife;
    List<Manga> MangasGenderBender;
    List<Manga> MangasEcchi;
    List<Manga> MangasHarem;
    List<Manga> MangasAction;
    List<Manga> MangasComedy;
    List<Manga> MangasDrama;
    List<Manga> MangasSciFi;
    List<Manga> MangasHorror;
    List<Manga> MangasRomance;
    List<Manga> MangasHistorical;
    List<Manga> MangasAdventure;
    List<Manga> MangasSupernatural;
    List<Manga> MangasShounen;
    List<Manga> MangasFantasy;
    List<Manga> MangasSeinen;
    List<Manga> MangasMystery;
    List<Manga> MangasPsychological;

    /* số thứ tự đánh dấu chọn list manga nào*/
    final  int MANGA = 0;
    final  int MANGA_TRADEDY = 1;
    final  int MANGA_MATURE = 2;
    final  int MANGA_SOL = 3;
    final  int MANGA_GB = 4;
    final  int MANGA_ECCHI = 5;
    final  int MANGA_HAREM = 6;
    final  int MANGA_ACTION = 7;
    final  int MANGA_COMEDY = 8;
    final  int MANGA_DRAMA = 9;
    final  int MANGA_SF = 10;
    final  int MANGA_HORROR = 11;
    final  int MANGA_ROMANCE = 12;
    final  int MANGA_HT = 13;
    final  int MANGA_ADVENTURE = 14;
    final  int MANGA_SUPERNATURAL = 15;
    final  int MANGA_SHOUNEN = 16;
    final  int MANGA_FANTASY = 17;
    final  int MANGA_SEINEN = 18;
    final  int MANGA_MYSTERY = 19;
    final  int MANGA_PSLC = 20;
    private  int kq = MANGA;
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
        database.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User user = snapshot.getValue(User.class);
                    Glide.with(MangaAct.this).load(user.getAvatar()).into(imgUser);
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

        findViewById(R.id.img_menu).setOnClickListener(v -> {
            ShowNavigationBar();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData()!=null){
            Uri uri = data.getData();
            imgUser.setImageURI(uri);

            final StorageReference reference = storage.getReference().child("avatar_user").child(auth.getUid());
            reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(MangaAct.this, "Đã cập nhật ảnh đại diện", Toast.LENGTH_SHORT).show();
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Users").child(auth.getUid()).child("avatar").setValue(uri.toString());
                        }
                    });
                }
            });
        }
    }

    private void Search() {
        Animation rightAnim = AnimationUtils.loadAnimation(this, R.anim.right_anim);
        searchView = findViewById(R.id.edtSearch);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setAnimation(rightAnim);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mangaAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mangaAdapter.getFilter().filter(newText);
                return false;
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
        MangasTragedy = new ArrayList<>();
        MangasMature = new ArrayList<>();
        MangasSliceOfLife= new ArrayList<>();
        MangasGenderBender= new ArrayList<>();
        MangasEcchi = new ArrayList<>();
        MangasHarem = new ArrayList<>();
        MangasAction = new ArrayList<>();
        MangasComedy = new ArrayList<>();
        MangasDrama = new ArrayList<>();
        MangasSciFi = new ArrayList<>();
        MangasHorror = new ArrayList<>();
        MangasRomance = new ArrayList<>();
        MangasHistorical = new ArrayList<>();
        MangasAdventure = new ArrayList<>();
        MangasSupernatural = new ArrayList<>();
        MangasShounen = new ArrayList<>();
        MangasFantasy = new ArrayList<>();
        MangasSeinen = new ArrayList<>();
        MangasMystery = new ArrayList<>();
        MangasPsychological = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Comic");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Manga manga = data.getValue(Manga.class);
                    String[] category = manga.getCategory().split("/");
                    Mangas.add(manga);

                    for (int i = 0; i<category.length; i++){
                        if(category[i].compareTo("Adventure")==0){
                            MangasAdventure.add(manga);
                            continue;
                        }

                        if(category[i].compareTo("Comedy")==0){
                            MangasComedy.add(manga);
                            continue;
                        }

                        if(category[i].compareTo("Fantasy")==0){
                            MangasFantasy.add(manga);
                            continue;
                        }

                        if(category[i].compareTo("Sci-Fi")==0){
                            MangasSciFi.add(manga);
                            continue;
                        }

                        if(category[i].compareTo("Slice Of Life")==0){
                            MangasSliceOfLife.add(manga);
                            continue;
                        }

                        if(category[i].compareTo("Tragedy")==0){
                            MangasTragedy.add(manga);
                            continue;
                        }
                        if(category[i].compareTo("Adventure")==0){
                            MangasAdventure.add(manga);
                            continue;
                        }

                        if(category[i].compareTo("Comedy")==0){
                            MangasComedy.add(manga);
                            continue;
                        }

                        if(category[i].compareTo("Mature")==0){
                            MangasMature.add(manga);
                            continue;
                        }
                        if(category[i].compareTo("Gender Bender")==0){
                            MangasGenderBender.add(manga);
                            continue;
                        }

                        if(category[i].compareTo("Ecchi")==0){
                            MangasEcchi.add(manga);
                            continue;
                        }
                        if(category[i].compareTo("Harem")==0){
                            MangasHarem.add(manga);
                            continue;
                        }

                        if(category[i].compareTo("Action")==0){
                            MangasAction.add(manga);
                            continue;
                        }

                        if(category[i].compareTo("Drama")==0){
                            MangasDrama.add(manga);
                            continue;
                        }

                        if(category[i].compareTo("Horror")==0){
                            MangasHorror.add(manga);
                            continue;
                        }
                        if(category[i].compareTo("Romance")==0){
                            MangasRomance.add(manga);
                            continue;
                        }

                        if(category[i].compareTo("Historical")==0){
                            MangasHistorical.add(manga);
                            continue;
                        }

                        if(category[i].compareTo("Supernatural")==0){
                            MangasSupernatural.add(manga);
                            continue;
                        }
                        if(category[i].compareTo("Shounen")==0){
                            MangasShounen.add(manga);
                            continue;
                        }
                        if(category[i].compareTo("Fantasy")==0){
                            MangasFantasy.add(manga);
                            continue;
                        }

                        if(category[i].compareTo("Seinen")==0){
                            MangasSeinen.add(manga);
                            continue;
                        }

                        if(category[i].compareTo("Mystery")==0){
                            MangasMystery.add(manga);
                            continue;
                        }

                        if(category[i].compareTo("Psychological")==0){
                            MangasPsychological.add(manga);
                            continue;
                        }
                    }
                }
                if(kq == MANGA)
                   setMangaAdapter(Mangas);
                if(kq == MANGA_ACTION)
                    setMangaAdapter(MangasAction);
                if(kq == MANGA_ADVENTURE)
                    setMangaAdapter(MangasAdventure);
                if(kq == MANGA_COMEDY)
                    setMangaAdapter(MangasComedy);
                if(kq == MANGA_DRAMA)
                    setMangaAdapter(MangasDrama);
                if(kq == MANGA_ECCHI)
                    setMangaAdapter(MangasEcchi);
                if(kq == MANGA_FANTASY)
                    setMangaAdapter(MangasFantasy);
                if(kq == MANGA_GB)
                    setMangaAdapter(MangasGenderBender);
                if(kq == MANGA_HAREM)
                    setMangaAdapter(MangasHarem);
                if(kq == MANGA_HORROR)
                    setMangaAdapter(MangasHorror);
                if(kq == MANGA_HT)
                    setMangaAdapter(MangasHistorical);
                if(kq == MANGA_MATURE)
                    setMangaAdapter(MangasMature);
                if(kq == MANGA_MYSTERY)
                    setMangaAdapter(MangasMystery);
                if(kq == MANGA_PSLC)
                    setMangaAdapter(MangasPsychological);
                if(kq == MANGA_ROMANCE)
                    setMangaAdapter(MangasRomance);
                if(kq == MANGA_SEINEN)
                    setMangaAdapter(MangasSeinen);
                if(kq == MANGA_SF)
                    setMangaAdapter(MangasSciFi);
                if(kq == MANGA_SHOUNEN)
                    setMangaAdapter(MangasShounen);
                if(kq == MANGA_SOL)
                    setMangaAdapter(MangasSliceOfLife);
                if(kq == MANGA_SUPERNATURAL)
                    setMangaAdapter(MangasSupernatural);
                if(kq == MANGA_TRADEDY)
                    setMangaAdapter(MangasTragedy);
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
                for (DataSnapshot data : snapshot.getChildren()) {
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
     *
     * @param clickedItemIndex
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
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_fav:
                startActivity(new Intent(MangaAct.this, FavoriteAct.class));
                drawerLayout.closeDrawer(GravityCompat.END);
                break;
            case R.id.nav_home:
                kq = MANGA;
                initUI();
                drawerLayout.closeDrawer(GravityCompat.END);
                break;
            case R.id.nav_type:
                showDialogCategory();

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


    private  void showDialogCategory(){
        Dialog dl = new Dialog(MangaAct.this);
        dl.setTitle("Thể loại");
        dl.setContentView(R.layout.dialog_category);
        Window window = dl.getWindow();
        window.setGravity(Gravity.LEFT);

        TextView tvAct = dl.findViewById(R.id.tv_Action);
        tvAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kq = MANGA_ACTION;
                initUI();
                drawerLayout.closeDrawer(GravityCompat.END);
                dl.dismiss();
            }
        });

        TextView tvAdven = dl.findViewById(R.id.tv_Adventure);
        tvAdven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kq = MANGA_ADVENTURE;
                initUI();
                //tvAdven.setTextColor(Color.RED);
                drawerLayout.closeDrawer(GravityCompat.END);
                dl.dismiss();
            }
        });

        TextView tvCo = dl.findViewById(R.id.tv_Comedy);
        tvCo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kq = MANGA_COMEDY;
                initUI();

                drawerLayout.closeDrawer(GravityCompat.END);
                dl.dismiss();
            }
        });

        TextView tvDra = dl.findViewById(R.id.tv_Drama);
        tvDra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kq = MANGA_DRAMA;
                initUI();

                drawerLayout.closeDrawer(GravityCompat.END);
                dl.dismiss();
            }
        });

        TextView tvEcc = dl.findViewById(R.id.tv_Ecchi);
        tvEcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kq = MANGA_ECCHI;
                initUI();

                drawerLayout.closeDrawer(GravityCompat.END);
                dl.dismiss();
            }
        });

        TextView tvFan = dl.findViewById(R.id.tv_Fantasy);
        tvFan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kq = MANGA_FANTASY;
                initUI();

                drawerLayout.closeDrawer(GravityCompat.END);
                dl.dismiss();
            }
        });

        TextView tvGB = dl.findViewById(R.id.tv_GenderBender);
        tvGB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kq = MANGA_GB;
                initUI();

                drawerLayout.closeDrawer(GravityCompat.END);
                dl.dismiss();
            }
        });

        TextView tvHa = dl.findViewById(R.id.tv_Harem);
        tvHa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kq = MANGA_HAREM;
                initUI();

                drawerLayout.closeDrawer(GravityCompat.END);
                dl.dismiss();
            }
        });

        TextView tvHorr = dl.findViewById(R.id.tv_Horror);
        tvHorr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kq = MANGA_HORROR;
                initUI();

                drawerLayout.closeDrawer(GravityCompat.END);
                dl.dismiss();
            }
        });

        TextView tvHT = dl.findViewById(R.id.tv_Historical);
        tvHT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kq = MANGA_HT;
                initUI();

                drawerLayout.closeDrawer(GravityCompat.END);
                dl.dismiss();
            }
        });

        TextView tvMa = dl.findViewById(R.id.tv_Mature);
        tvMa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kq = MANGA_MATURE;
                initUI();

                drawerLayout.closeDrawer(GravityCompat.END);
                dl.dismiss();
            }
        });

        TextView tvMys = dl.findViewById(R.id.tv_Mystery);
        tvMys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kq = MANGA_MYSTERY;
                initUI();

                drawerLayout.closeDrawer(GravityCompat.END);
                dl.dismiss();
            }
        });

        TextView tvPsy = dl.findViewById(R.id.tv_Psychological);
        tvPsy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kq = MANGA_PSLC;
                initUI();

                drawerLayout.closeDrawer(GravityCompat.END);
                dl.dismiss();
            }
        });

        TextView tvRo = dl.findViewById(R.id.tv_Romance);
        tvRo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kq = MANGA_ROMANCE;
                initUI();

                drawerLayout.closeDrawer(GravityCompat.END);
                dl.dismiss();
            }
        });

        TextView tvSei = dl.findViewById(R.id.tv_Seinen);
        tvSei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kq = MANGA_SEINEN;
                initUI();

                drawerLayout.closeDrawer(GravityCompat.END);
                dl.dismiss();
            }
        });

        TextView tvSF = dl.findViewById(R.id.tv_SciFi);
        tvSF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kq = MANGA_SF;
                initUI();

                drawerLayout.closeDrawer(GravityCompat.END);
                dl.dismiss();
            }
        });

        TextView tvShou = dl.findViewById(R.id.tv_Shounen);
        tvShou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kq = MANGA_SHOUNEN;
                initUI();

                drawerLayout.closeDrawer(GravityCompat.END);
                dl.dismiss();
            }
        });

        TextView tvSOL = dl.findViewById(R.id.tv_SliceOfLife);
        tvSOL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kq = MANGA_SOL;
                initUI();

                drawerLayout.closeDrawer(GravityCompat.END);
                dl.dismiss();
            }
        });

        TextView tvSup = dl.findViewById(R.id.tv_Supernatural);
        tvSup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kq = MANGA_SUPERNATURAL;
                initUI();

                drawerLayout.closeDrawer(GravityCompat.END);
                dl.dismiss();
            }
        });

        TextView tvTra = dl.findViewById(R.id.tv_Tragedy);
        tvTra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kq = MANGA_TRADEDY;
                initUI();

                drawerLayout.closeDrawer(GravityCompat.END);
                dl.dismiss();
            }
        });
        dl.show();
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