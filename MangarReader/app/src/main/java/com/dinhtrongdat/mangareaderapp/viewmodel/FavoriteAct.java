package com.dinhtrongdat.mangareaderapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dinhtrongdat.mangareaderapp.R;
import com.dinhtrongdat.mangareaderapp.adapter.FavoriteAdapter;
import com.dinhtrongdat.mangareaderapp.model.FavoriteManga;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FavoriteAct extends AppCompatActivity implements FavoriteAdapter.ListFavoriteItemClickListener {

    /**
     * View component
     */
    ImageView imgBack;
    RecyclerView rcvFav;
    LinearLayout linear;

    /**
     * Adapter
     */
    FavoriteAdapter adapter;
    List<FavoriteManga> mdata;

    /**
     * Database
     */
    FirebaseUser user;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        initUI();
        Swipe();
    }

    private void initUI() {
        linear = findViewById(R.id.layout_main_fav);
        mdata = new ArrayList<>();
        imgBack = findViewById(R.id.imgBack_frg);
        user = FirebaseAuth.getInstance().getCurrentUser();
        rcvFav = findViewById(R.id.recycle_fav);
        reference = FirebaseDatabase.getInstance().getReference("favorite");

        rcvFav.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(mdata.size()!=0)
                    mdata.clear();
                for (DataSnapshot data : snapshot.getChildren()){
                    FavoriteManga fav = data.getValue(FavoriteManga.class);
                    if(Objects.requireNonNull(fav).getUid().compareTo(user.getUid()) == 0){
                        mdata.add(fav);
                        adapter = new FavoriteAdapter(FavoriteAct.this, mdata,FavoriteAct.this::onFavoriteItemClick);
                        rcvFav.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imgBack.setOnClickListener(v -> finish());
    }


    /**
     * Hàm định nghĩa các phương thức thao tác : kéo, lướt, trượt
     */
    private void Swipe(){
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            // Hàm onMove xử lý code drag và drop
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {
                int positionDragged = dragged.getAdapterPosition();
                int positionTarget = target.getAdapterPosition();
                Collections.swap(mdata, positionDragged, positionTarget);
                adapter.notifyItemMoved(positionDragged, positionTarget);
                return false;
            }

            // Trượt để xoá
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int deleteIndex = viewHolder.getAdapterPosition();
                FavoriteManga item = mdata.get(deleteIndex);
                RemoveFavorite(user, item);
            }
        });
        itemTouchHelper.attachToRecyclerView(rcvFav);
    }

    /**
     * Xoá truyện khỏi danh sách ưa thích
     * @param user tài khoản đăng nhập
     * @param item truyện bị xoá
     */
    private void RemoveFavorite(FirebaseUser user, FavoriteManga item) {
        FavoriteManga curFav;
        curFav = new FavoriteManga(item.getName(), item.getImage(), item.getCategory(), item.getDescription(), item.getAuthor(), item.getBackdrop(), item.getUid(),item.getChapters());
        reference = FirebaseDatabase.getInstance().getReference("favorite");

        Snackbar snackbar = Snackbar.make(linear, "Đã xoá truyện khỏi danh sách ưa thích",Snackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(Color.RED);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    FavoriteManga fav = data.getValue(FavoriteManga.class);
                    if(Objects.requireNonNull(fav).getUid().compareTo(curFav.getUid())==0){
                        if(fav.getName().compareTo(curFav.getName()) == 0){
                            reference.child(Objects.requireNonNull(data.getKey())).removeValue().addOnCompleteListener(task -> {
                                if(task.isSuccessful()){
                                    snackbar.show();
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onFavoriteItemClick(int clickedItemIndex) {
        Intent intent = new Intent(FavoriteAct.this, MangaDetailsAct.class);
        intent.putExtra("fav", mdata.get(clickedItemIndex));
        startActivity(intent);
    }
}