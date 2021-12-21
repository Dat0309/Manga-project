package com.dinhtrongdat.mangareaderapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dinhtrongdat.mangareaderapp.R;
import com.dinhtrongdat.mangareaderapp.adapter.FavoriteAdapter;
import com.dinhtrongdat.mangareaderapp.model.FavoriteManga;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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
                    if(fav.getUid().compareTo(user.getUid()) == 0){
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

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onFavoriteItemClick(int clickedItemIndex) {

    }
}