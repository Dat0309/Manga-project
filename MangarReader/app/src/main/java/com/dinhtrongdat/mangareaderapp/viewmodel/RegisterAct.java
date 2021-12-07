package com.dinhtrongdat.mangareaderapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

public class RegisterAct extends AppCompatActivity implements View.OnClickListener {

    /**
     * Defind project variable
     */
    ImageView imgLogo;
    TextInputLayout edtUser, edtPass;
    Button btnSignup;
    ImageButton btnBack;
    ProgressBar progressBar;
    LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        initUI();
    }

    private void initUI() {
        imgLogo = findViewById(R.id.img_logo);
        edtUser = findViewById(R.id.username);
        edtPass = findViewById(R.id.password);
        btnSignup = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progres);
        linear = findViewById(R.id.linear_btn);
        btnBack = findViewById(R.id.btn_back);

        Sprite wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);

        // Set animation to component
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        Animation leftAnim = AnimationUtils.loadAnimation(this, R.anim.left_anim);

        linear.setAnimation(topAnim);
        btnBack.setAnimation(leftAnim);

        btnBack.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
    }

    private void RegisterWithEmail(){
        if (!ValidationUser() | !ValidationPass()) return;
        HideKeyboard(RegisterAct.this);
        progressBar.setVisibility(View.VISIBLE);

        String strEmail = edtUser.getEditText().getText().toString().trim();
        String strPass = edtPass.getEditText().getText().toString().trim();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(strEmail, strPass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterAct.this, "Success", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterAct.this, LoginAct.class);
                    startActivity(intent);
                    progressBar.setVisibility(View.GONE);
                }else{
                    Toast.makeText(RegisterAct.this, "Fail", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                Intent intent = new Intent(RegisterAct.this, LoginAct.class);
                startActivity(intent);
                break;
            case R.id.btn_register:
                RegisterWithEmail();
                break;
        }
    }
}