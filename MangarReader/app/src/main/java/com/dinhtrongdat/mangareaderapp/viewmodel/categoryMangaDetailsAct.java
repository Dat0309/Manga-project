package com.dinhtrongdat.mangareaderapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.dinhtrongdat.mangareaderapp.R;
import com.dinhtrongdat.mangareaderapp.adapter.CategoryAdapter;
import com.dinhtrongdat.mangareaderapp.adapter.MangaAdapter;
import com.dinhtrongdat.mangareaderapp.model.Manga;
import com.dinhtrongdat.mangareaderapp.model.Tag;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class categoryMangaDetailsAct extends AppCompatActivity implements  MangaAdapter.OnItemMangaClick {

    ImageView ivBack;
    RecyclerView rcvItem;
    MangaAdapter mangaAdapter;
    TextView txt;

    DatabaseReference databaseReference;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    List<Manga> Mangas;

    Tag tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_manga_details);

        init();
    }

    private  void init(){
        ivBack = findViewById(R.id.imgBack);
        ivBack.setOnClickListener(v -> finish());
         UploadMangaItem();
    }

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

                    tag = (Tag) getIntent().getSerializableExtra("category");
                    for(String Cate : category){
                        if(Cate.compareTo(tag.getTag()) == 0){
                            Mangas.add(manga);
                            break;
                        }
                    }

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
        rcvItem = findViewById(R.id.rcv_category);
        mangaAdapter = new MangaAdapter(categoryMangaDetailsAct.this, mangas, this);

        rcvItem.setLayoutManager(new GridLayoutManager(this, 2));
        rcvItem.setAdapter(mangaAdapter);
        mangaAdapter.notifyDataSetChanged();
    }



    @Override
    public void onMangaItemClick(int clickedItemIndex) {
        Intent intent = new Intent(categoryMangaDetailsAct.this, MangaDetailsAct.class);
        intent.putExtra("manga", Mangas.get(clickedItemIndex));
        startActivity(intent);
    }
}