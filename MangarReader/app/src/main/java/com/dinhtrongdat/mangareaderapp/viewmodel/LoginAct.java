package com.dinhtrongdat.mangareaderapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dinhtrongdat.mangareaderapp.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginAct extends AppCompatActivity implements View.OnClickListener {

    /**
     * Defind project variable
     */
    ImageView imgLogo;
    TextInputLayout edtUser, edtPass;
    Button btnSignin, btnSignup, btnForgot;
    ImageButton btnFacebook, btnGoogle;
    ProgressBar progressBar;
    LinearLayout linear;
    TextView txtDes, txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        initUI();
    }

    /**
     * Hàm định nghĩa các thao tác với giao diện
     */
    private void initUI() {
        imgLogo = findViewById(R.id.img_logo);
        edtUser = findViewById(R.id.username);
        edtPass = findViewById(R.id.password);
        btnSignin = findViewById(R.id.btn_signin);
        btnSignup = findViewById(R.id.btn_signup);
        btnForgot = findViewById(R.id.btn_forgot_pass);
        progressBar = findViewById(R.id.progres);
        linear = findViewById(R.id.linear_btn);
        btnFacebook = findViewById(R.id.btn_facebook);
        btnGoogle = findViewById(R.id.btn_google);
        txtDes = findViewById(R.id.txt_des);
        txtTitle = findViewById(R.id.txt_title);

        Sprite wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);

        // Set animation to component
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        linear.setAnimation(topAnim);
        btnGoogle.setAnimation(topAnim);
        btnFacebook.setAnimation(topAnim);

        btnSignin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
        btnForgot.setOnClickListener(this);
    }

    /**
     * Hàm kiểm tra đăng nhập với google
     */
    private void SigninGmail() {
        if (!ValidationUser() | !ValidationPass()) return;
        HideKeyboard(LoginAct.this);
        progressBar.setVisibility(View.VISIBLE);

        String strEmail = edtUser.getEditText().getText().toString().trim();
        String strPass = edtPass.getEditText().getText().toString().trim();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(strEmail, strPass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Pair[] pair = new Pair[3];
                    pair[0] = new Pair<View, String>(imgLogo, "logo_trans");
                    pair[1] = new Pair<View, String>(txtDes, "text_trans");
                    pair[2] = new Pair<View, String>(txtTitle, "text_trans");

                    Toast.makeText(LoginAct.this, "Login Success", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = auth.getCurrentUser();

                    Intent intent = new Intent(LoginAct.this, MangaAct.class);
                    ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(LoginAct.this, pair);
                    startActivity(intent, option.toBundle());
                    finishAffinity();
                    progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(LoginAct.this, "Fail", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * Hàm kiểm tra giá trị gmail nhập vào của người dùng
     *
     * @return true or false
     */
    private boolean ValidationUser() {
        String val = edtUser.getEditText().getText().toString();
        String space = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            edtUser.setError("Nhập tài khoản");
            return false;
        } else {
            edtUser.setError(null);
            return true;
        }
    }

    /**
     * Hàm kiểm tra giá trị mật khẩu người dùng nhập vào
     *
     * @return true or false
     */
    private boolean ValidationPass() {
        String val = edtPass.getEditText().getText().toString();
        String space = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            edtPass.setError("Nhập mật khẩu");
            return false;
        } else if (val.length() >= 15) {
            edtPass.setError("Mật khẩu không được quá 15 ký tự");
            return false;
        } else if (!val.matches(space)) {
            edtPass.setError("Mật khẩu không được chứa khoảng trắng");
            return false;
        } else {
            edtPass.setError(null);
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

    /**
     * Hàm chuyển màn hình đăng ký
     */
    private void RegisterUser() {
        Pair[] pair = new Pair[2];
        pair[0] = new Pair<View, String>(imgLogo, "logo_trans");
        pair[1] = new Pair<View, String>(txtDes, "text_trans");

        Intent intent = new Intent(LoginAct.this, RegisterAct.class);
        ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(LoginAct.this, pair);
        startActivity(intent, option.toBundle());
        finish();
    }

    /**
     * Hàm chuyển màn hình đăng ký
     */
    private void SignInWithGoogle() {
        Pair[] pair = new Pair[1];
        pair[0] = new Pair<View, String>(imgLogo, "logo_trans");

        Intent intent = new Intent(LoginAct.this, GoogleLoginAct.class);
        ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(LoginAct.this, pair);
        startActivity(intent, option.toBundle());
        finish();
    }

    /**
     * Chuyển màn hình quên mật khẩu
     */
    private void ForgotPass() {
        startActivity(new Intent(LoginAct.this, ForgotAct.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signin:
                SigninGmail();
                break;
            case R.id.btn_signup:
                RegisterUser();
                break;
            case R.id.btn_google:
                SignInWithGoogle();
                break;
            case R.id.btn_forgot_pass:
                ForgotPass();
                break;
        }
    }
}