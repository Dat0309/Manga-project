package com.dinhtrongdat.mangareaderapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dinhtrongdat.mangareaderapp.R;
import com.dinhtrongdat.mangareaderapp.adapter.ChapterAdapter;
import com.dinhtrongdat.mangareaderapp.adapter.CommentAdapter;
import com.dinhtrongdat.mangareaderapp.adapter.TagAdapter;
import com.dinhtrongdat.mangareaderapp.model.BannerManga;
import com.dinhtrongdat.mangareaderapp.model.Chapter;
import com.dinhtrongdat.mangareaderapp.model.FavoriteManga;
import com.dinhtrongdat.mangareaderapp.model.Manga;
import com.dinhtrongdat.mangareaderapp.model.Rating;
import com.dinhtrongdat.mangareaderapp.model.Tag;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MangaDetailsAct extends AppCompatActivity implements ChapterAdapter.OnItemChapterClick {

    float rating = 0;
    /**
     * Model
     */
    Manga manga;
    BannerManga bannerManga;
    FavoriteManga favorite;
    Rating _rate;
    boolean IS_ADD = false;

    /**
     * View
     */
    Button btnReadManga;
    ImageView btnBack, btnShare, btnFavorite, btnRate;
    ImageView ivPosterManga, ivBannerManga;
    TextView tvNameManga, tvAuthor, tvDescription, tvNumRate;
    RecyclerView rcvChapter, rcvTag;
    EditText rateCmt;
    CardView viewCmt;

    /**
     * Adapter
     */
    ChapterAdapter chapterAdapter;
    CommentAdapter commentAdapter;
    List<Rating> listRating;
    List<Chapter> listChapter;
    TagAdapter tagAdapter;
    List<Tag> listTag;

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

    private void initUI() {
        btnReadManga = findViewById(R.id.btn_read_manga);
        btnBack = findViewById(R.id.btn_back);
        btnShare = findViewById(R.id.btn_share);
        btnFavorite = findViewById(R.id.btn_favorite);
        ivPosterManga = findViewById(R.id.img_poster_manga);
        ivBannerManga = findViewById(R.id.img_banner_manga);
        tvNameManga = findViewById(R.id.tv_name_manga);
        tvAuthor = findViewById(R.id.txt_author);
        tvDescription = findViewById(R.id.tv_description);
        tvNumRate = findViewById(R.id.num_rate);
        rcvChapter = findViewById(R.id.rcv_chapter);
        rcvTag = findViewById(R.id.rcv_tag);
        btnRate = findViewById(R.id.btn_rate);
        viewCmt = findViewById(R.id.view_cmt);

        CheckFav();
        CheckRate();


        btnBack.setOnClickListener(view -> finish());
        btnReadManga.setOnClickListener(view -> {
            Intent intent = new Intent(MangaDetailsAct.this, ViewMangaAct.class);
            intent.putExtra("chapter", listChapter.get(0));
            startActivity(intent);
        });
        LoadDetails();

        btnFavorite.setOnClickListener(v -> {
            if (!IS_ADD)
                AddToFavorite(user);
            else
                Toast.makeText(MangaDetailsAct.this, "Truy???n ???? t???n t???i trong m???c ??a th??ch", Toast.LENGTH_LONG).show();
        });

        btnShare.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, manga.getName());
            intent.putExtra(Intent.EXTRA_SUBJECT, manga.getName());
            intent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(intent, null);
            startActivity(shareIntent);
        });

        btnRate.setOnClickListener(view -> OpenDiaLogRateManga());

        viewCmt.setOnClickListener(view -> OpenDiaLogViewCmt());
    }

    /**
     * L???y l?????t rate c???a manga
     */
    private void GetRatingManga(String name) {
        reference = FirebaseDatabase.getInstance().getReference("rating");
        Query mangaRating = reference.orderByChild("mangaName").equalTo(name);
        mangaRating.addValueEventListener(new ValueEventListener() {
            float count = 0, sum = 0;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    Rating item = postSnapshot.getValue(Rating.class);
                    assert item != null;
                    sum+= Float.parseFloat(item.getRateValue());
                    count++;
                }
                if(count!=0){
                    float average = sum/count;
                    tvNumRate.setText(String.valueOf(average));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    /**
     * Hi???n b??nh lu???n
     */
    private void OpenDiaLogViewCmt(){
        // hi???n th??? dialog + set v??? tr??, size c???a dialog
        final Dialog dialog = new Dialog(MangaDetailsAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.view_rate_manga);
        Window window = dialog.getWindow();

        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowA = window.getAttributes();
        windowA.gravity = Gravity.BOTTOM;
        window.setAttributes(windowA);

        View btnBack = (View) dialog.findViewById(R.id.btn_back_rate);
        btnBack.setOnClickListener(view -> dialog.dismiss());

        if(manga != null)
            GetCmtRate(manga.getName());
        else if(bannerManga != null)
            GetCmtRate(bannerManga.getName());
        else if(favorite != null)
            GetCmtRate(favorite.getName());

        RecyclerView rcvCmt = dialog.findViewById(R.id.rcv_cmt);
        commentAdapter = new CommentAdapter(dialog.getContext(), listRating);
        commentAdapter.notifyDataSetChanged();
        rcvCmt.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rcvCmt.setAdapter(commentAdapter);

        dialog.show();

    }

    private void GetCmtRate(String name) {
        listRating = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("rating");
        Query mangaRating = reference.orderByChild("mangaName").equalTo(name);
        mangaRating.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    Rating item = postSnapshot.getValue(Rating.class);
                    listRating.add(item);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Hi???n rate_manga (Ch??? n??y v???a hi???n ra dialog v???a l???y s??? l?????ng)
     */
    private void OpenDiaLogRateManga() {
        // hi???n th??? dialog + set v??? tr??, size c???a dialog
        final Dialog dialog = new Dialog(MangaDetailsAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rate_manga);
        Window window = dialog.getWindow();

        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowA = window.getAttributes();
        windowA.gravity = Gravity.BOTTOM;
        window.setAttributes(windowA);

        View btnBack = (View) dialog.findViewById(R.id.btn_back_rate);
        rateCmt = (EditText) dialog.findViewById(R.id.edt_cmt);
        btnBack.setOnClickListener(view -> dialog.dismiss());

        // L???y s??? l?????ng v?? g???i ??i
        RatingBar ratingStar = (RatingBar) dialog.findViewById(R.id.ratingStar);


        ratingStar.setOnRatingBarChangeListener((ratingBar, v, b) -> {
            rating = ratingBar.getRating(); // L???y s??? l?????ng
        });

        View btnSend = (View) dialog.findViewById(R.id.btn_send);
        btnSend.setOnClickListener(v -> {
            String cmt = rateCmt.getText().toString();
            if (manga != null)
                _rate = new Rating(user.getUid(), manga.getName(), String.valueOf(rating), cmt);
            else if (bannerManga != null)
                _rate = new Rating(user.getUid(), bannerManga.getName(), String.valueOf(rating), cmt);
            else if (favorite != null)
                _rate = new Rating(user.getUid(), favorite.getName(), String.valueOf(rating), cmt);

            addRateManga();
            dialog.dismiss();
            Toast.makeText(this, "C???m ??n ???? ????nh gi??! M??i y??u", Toast.LENGTH_SHORT).show();
        });
        dialog.show();

    }



    /**
     * L???y s??? l?????ng Star
     */
    private void addRateManga() {
        reference = FirebaseDatabase.getInstance().getReference("rating");
        reference.push().setValue(_rate).addOnCompleteListener(task -> {
        });
    }

    /**
     * Xu???t chi ti???t manga
     */
    private void LoadDetails() {
        if (manga != null) {
            Glide.with(this).load(manga.getImage()).into(ivPosterManga);
            Glide.with(this).load(manga.getBackdrop()).into(ivBannerManga);
            tvNameManga.setText(manga.getName());
            tvAuthor.setText(manga.getAuthor());
            tvDescription.setText(manga.getDescription());
            listChapter = manga.getChapters();

            chapterAdapter = new ChapterAdapter(this, listChapter, this);
            chapterAdapter.notifyDataSetChanged();
            rcvChapter.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            rcvChapter.setAdapter(chapterAdapter);

            String[] tag = manga.getCategory().split("/");
            listTag = new ArrayList<>();
            for (String cate : tag) {
                listTag.add(new Tag(cate));
            }

            tagAdapter = new TagAdapter(this, listTag);
            tagAdapter.notifyDataSetChanged();
            rcvTag.setLayoutManager(new GridLayoutManager(this, 2));
            rcvTag.setAdapter(tagAdapter);

            // L???y l?????t rate
            GetRatingManga(manga.getName());



        } else if (bannerManga != null) {
            Glide.with(this).load(bannerManga.getImage()).into(ivPosterManga);
            Glide.with(this).load(bannerManga.getBackdrop()).into(ivBannerManga);
            tvNameManga.setText(bannerManga.getName());
            tvAuthor.setText(bannerManga.getAuthor());
            tvDescription.setText(bannerManga.getDescription());
            listChapter = bannerManga.getChapters();

            chapterAdapter = new ChapterAdapter(this, listChapter, this);
            chapterAdapter.notifyDataSetChanged();
            rcvChapter.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            rcvChapter.setAdapter(chapterAdapter);

            String[] tag = bannerManga.getCategory().split("/");
            listTag = new ArrayList<>();
            for (String cate : tag) {
                listTag.add(new Tag(cate));
            }
            tagAdapter = new TagAdapter(this, listTag);
            tagAdapter.notifyDataSetChanged();
            rcvTag.setLayoutManager(new GridLayoutManager(this, 2));
            rcvTag.setAdapter(tagAdapter);

            // L???y l?????t rate
            GetRatingManga(bannerManga.getName());
        } else if (favorite != null) {
            Glide.with(this).load(favorite.getImage()).into(ivPosterManga);
            Glide.with(this).load(favorite.getBackdrop()).into(ivBannerManga);
            tvNameManga.setText(favorite.getName());
            tvAuthor.setText(favorite.getAuthor());
            tvDescription.setText(favorite.getDescription());
            listChapter = favorite.getChapters();

            chapterAdapter = new ChapterAdapter(this, listChapter, this);
            chapterAdapter.notifyDataSetChanged();
            rcvChapter.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            rcvChapter.setAdapter(chapterAdapter);

            String[] tag = favorite.getCategory().split("/");
            listTag = new ArrayList<>();
            for (String cate : tag) {
                listTag.add(new Tag(cate));
            }
            tagAdapter = new TagAdapter(this, listTag);
            tagAdapter.notifyDataSetChanged();
            rcvTag.setLayoutManager(new GridLayoutManager(this, 2));
            rcvTag.setAdapter(tagAdapter);

            // L???y l?????t rate
            GetRatingManga(favorite.getName());
        }
    }

    /**
     * H??m ki???m tra truy???n c?? n???m trong danh s??ch y??u th??ch c???a ng?????i d??ng kh??ng
     */
    private void CheckFav() {
        FavoriteManga fav;
        if (manga != null)
            fav = new FavoriteManga(manga.getName(), manga.getImage(), manga.getCategory(), manga.getDescription(), manga.getAuthor(), manga.getBackdrop(), user.getUid(), manga.getChapters());
        else
            fav = favorite;

        reference = FirebaseDatabase.getInstance().getReference("favorite");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    FavoriteManga favorite = data.getValue(FavoriteManga.class);
                    if (Objects.requireNonNull(favorite).getUid().compareTo(fav.getUid()) == 0) {
                        if (String.valueOf(favorite.getName()).compareTo(String.valueOf(fav.getName())) == 0) {
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
     * H??m ki???m tra truy???n ???? ???????c rate hay ch??a
     */
    private void CheckRate(){
        reference = FirebaseDatabase.getInstance().getReference("rating");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()){
                    Rating rating = data.getValue(Rating.class);
                    if(Objects.requireNonNull(rating).getUid().compareTo(rating.getUid()) == 0){
                        if(String.valueOf(rating.getMangaName()).compareTo(String.valueOf(manga.getName())) == 0){
                            if(rating.getUid().compareTo(user.getUid()) == 0){
                                IS_ADD = true;
                                btnRate.setImageResource(R.drawable.ic_star_check);
                                break;
                            }
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
     * H??m th??m truy???n v??o danh s??ch truy???n y??u th??ch c???a ng?????i d??ng
     *
     * @param user t??i kho???n ng?????i d??ng
     */
    private void AddToFavorite(FirebaseUser user) {
        FavoriteManga fav;
        if (manga != null)
            fav = new FavoriteManga(manga.getName(), manga.getImage(), manga.getCategory(), manga.getDescription(), manga.getAuthor(), manga.getBackdrop(), user.getUid(), manga.getChapters());
        else
            fav = favorite;

        reference = FirebaseDatabase.getInstance().getReference("favorite");
        reference.push().setValue(fav).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(MangaDetailsAct.this, "???? th??m v??o m???c y??u th??ch", Toast.LENGTH_SHORT).show();
                IS_ADD = true;
                btnFavorite.setImageResource(R.drawable.ic_favorite_red);
            } else {
                Toast.makeText(MangaDetailsAct.this, "Th???t b???i", Toast.LENGTH_SHORT).show();
                IS_ADD = false;
                btnFavorite.setImageResource(R.drawable.ic_favorite);
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

