package com.dinhtrongdat.mangareaderapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dinhtrongdat.mangareaderapp.R;
import com.dinhtrongdat.mangareaderapp.model.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PasswordAct extends AppCompatActivity {
    ImageView ivBack;
    TextInputLayout oldPass;
    TextInputLayout newPass;
    TextInputLayout newPass2;
    Button btnClick;
    String oldPasstxt;
    String newPasstxt;
    String newPass2txt;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        ivBack = findViewById(R.id.imgBack);
        oldPass = findViewById(R.id.txtOldPass);
        newPass = findViewById(R.id.txtNewPass);
        newPass2 = findViewById(R.id.txtnewPass2);
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

                    oldPasstxt = oldPass.getEditText().getText().toString();
                    if(oldPasstxt.compareTo(pass) == 0){
                        newPasstxt = newPass.getEditText().getText().toString();
                        newPass2txt = newPass2.getEditText().getText().toString();
                        if( newPasstxt.compareTo(newPass2txt) == 0){
                            database.getReference().child("Users").child(auth.getUid()).child("passWord").setValue(newPasstxt);
                            Toast.makeText(PasswordAct.this, "Đã cập nhật mật khẩu", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(PasswordAct.this, "Xác nhận lại mật khẩu không đúng!", Toast.LENGTH_SHORT).show();

                    }
                    else
                        Toast.makeText(PasswordAct.this, "Mật khẩu cũ không chính xác!", Toast.LENGTH_SHORT).show();


            }});

    }

}