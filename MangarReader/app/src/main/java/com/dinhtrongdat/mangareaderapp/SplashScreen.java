package com.dinhtrongdat.mangareaderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.dinhtrongdat.mangareaderapp.viewmodel.LoginAct;

/**
 * SplashScreen of my app
 */
public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN = 4200;
    ImageView imgLogo;
    TextView txtDescription;
    Animation topAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_anim);

        imgLogo = findViewById(R.id.img_logo);
        txtDescription = findViewById(R.id.txt_des);

        imgLogo.setAnimation(topAnim);
        txtDescription.setAnimation(topAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isLogin();
            }
        }, SPLASH_SCREEN);
    }

    /**
     * Hàm định nghĩa phương thức kiểm tra người dùng đã đăng nhập hay chưa
     * Nếu Đã đăng nhập thì sẽ chuển qua màn hình main
     * Nếu chưa đăng nhập sẽ chuyển qua màn hình login
     */
    private void isLogin() {
        Intent intent = new Intent(SplashScreen.this, LoginAct.class);
        startActivity(intent);
    }
}