package com.dinhtrongdat.mangareaderapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.dinhtrongdat.mangareaderapp.R;
import com.dinhtrongdat.mangareaderapp.adapter.MangaAdapter;
import com.dinhtrongdat.mangareaderapp.model.Manga;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class MangaCategoryAct extends AppCompatActivity implements MangaAdapter.OnItemMangaClick{

    ImageView ivBack;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_category);

        initUI();
    }

    private void initUI(){
        ivBack = findViewById(R.id.imgBack);
        ivBack.setOnClickListener(v -> finish());
    }


    @Override
    public void onMangaItemClick(int clickedItemIndex) {

    }
}