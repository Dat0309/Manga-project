package com.dinhtrongdat.mangareaderapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dinhtrongdat.mangareaderapp.R;
import com.dinhtrongdat.mangareaderapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PasswordAct extends AppCompatActivity {
    ImageView ivBack;
    EditText oldPass;
    EditText newPass;
    Button btnClick;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        ivBack = findViewById(R.id.imgBack);

        newPass = findViewById(R.id.txtNewPass);
        btnClick = findViewById(R.id.btnChangePass);
        ivBack.setOnClickListener(v -> finish());
        FirebaseAuth auth;
        FirebaseDatabase database;


        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        database.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                pass = user.getPassWord();}}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


            btnClick.setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View view){

                          database.getReference().child("Users").child(auth.getUid()).child("passWord").setValue(newPass.getText().toString());
                         Toast.makeText(PasswordAct.this, "Đã cập nhật mật khẩu", Toast.LENGTH_SHORT).show();
            }});

    }
}