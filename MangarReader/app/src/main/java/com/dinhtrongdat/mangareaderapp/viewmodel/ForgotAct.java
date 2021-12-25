package com.dinhtrongdat.mangareaderapp.viewmodel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.dinhtrongdat.mangareaderapp.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgotAct extends AppCompatActivity {

    /**
     * View
     */
    TextInputLayout edtGmail;

    /**
     * Database
     */
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgot);

        initUI();
    }

    private void initUI() {
        auth = FirebaseAuth.getInstance();
        edtGmail = findViewById(R.id.gmail_forgot);

        findViewById(R.id.btn_accept).setOnClickListener(v->{
            String gmail = Objects.requireNonNull(edtGmail.getEditText()).getText().toString();
            if(!ValidationUser()) return;
            HideKeyboard(ForgotAct.this);
            auth.sendPasswordResetEmail(gmail).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotAct.this, "Vui lòng kiẻme tra email", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgotAct.this, LoginAct.class));
                    finish();
                }else{
                    Toast.makeText(ForgotAct.this, "Thất bại", Toast.LENGTH_SHORT).show();
                }
            });
        });

        findViewById(R.id.btn_back_login).setOnClickListener(v-> finish());
    }

    /**
     * Hàm kiểm tra giá trị gmail nhập vào của người dùng
     *
     * @return true or false
     */
    private boolean ValidationUser() {
        String val = Objects.requireNonNull(edtGmail.getEditText()).getText().toString();
        String space = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            edtGmail.setError("Nhập tài khoản");
            return false;
        } else {
            edtGmail.setError(null);
            return true;
        }
    }

    /**
     * Hàm ẩn bàn phím khi nhấn đăng nhập
     *
     * @param activity
     */
    private static void HideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null)
            view = new View(activity);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}