package com.dinhtrongdat.mangareaderapp.viewmodel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.dinhtrongdat.mangareaderapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class GoogleLoginAct extends AppCompatActivity {

    final static int RC_SIGN_IN = 123;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    ImageView imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_google_login);
        mAuth = FirebaseAuth.getInstance();
        imgLogo = findViewById(R.id.img_logo);

        createRequest();

        findViewById(R.id.btn_gg_signin).setOnClickListener(v -> SignIn());
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        Pair[] pair = new Pair[1];
        pair[0] = new Pair<View, String>(imgLogo,"logo_trans");

        if(user!=null){
            Intent intent = new Intent(GoogleLoginAct.this, MangaAct.class);

            ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(GoogleLoginAct.this, pair);
            startActivity(intent, option.toBundle());
            finish();
        }
    }

    /**
     * Kết nối tới Google signin
     */
    private void createRequest() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    /**
     * Gửi yêu cầu đăng nhập
     */
    private void SignIn() {
        Intent signinIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signinIntent, RC_SIGN_IN);
    }

    /**
     * Xử lý yêu cầu đăng nhập được trả về
     * @param requestCode RC_SIGN_IN
     * @param resultCode RC_SIGN_IN
     * @param data intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Hàm định nghĩa xử lý quá trình gửi yêu cầu đưng nhập bằng google
     * @param idToken token gg
     */
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(GoogleLoginAct.this, "Success", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(GoogleLoginAct.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}