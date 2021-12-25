package com.dinhtrongdat.mangareaderapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dinhtrongdat.mangareaderapp.R;
import com.dinhtrongdat.mangareaderapp.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class InformationAct extends AppCompatActivity {

    ImageView ivBack;
    ImageView ivAvatar;
    TextView txtGmail;
    EditText txtFullName;

    Button btnClick;

    String name;

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        initUI();
    }

    private void initUI() {
        ivBack = findViewById(R.id.imgBack);
        ivAvatar = findViewById(R.id.iv_avt_info);
        txtGmail = findViewById(R.id.txtGmail);
        txtFullName = findViewById(R.id.txtFullName);

        btnClick = findViewById(R.id.btnClick);


        ivBack.setOnClickListener(v -> finish());

        FirebaseAuth auth;
        FirebaseDatabase database;

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();


        // Fetch data user in firebase
        database.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    Glide.with(InformationAct.this).load(user.getAvatar()).into(ivAvatar);
                    txtFullName.setText(user.getName());
                    txtGmail.setText(user.getUserName());




                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = txtFullName.getText().toString();
                database.getReference().child("Users").child(auth.getUid()).child("name").setValue(name);
                Toast.makeText(InformationAct.this, "Đã cập nhật tên đầy đủ", Toast.LENGTH_SHORT).show();
            }
        });

    }




}