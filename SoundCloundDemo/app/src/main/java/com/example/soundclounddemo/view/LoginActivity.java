package com.example.soundclounddemo.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.soundclounddemo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    @BindView(R.id.bt_first_user)
    Button btFirstUser;
    @BindView(R.id.bt_second_user)
    Button btSecondUser;
    private FirebaseAuth mFirebaseAuth;
    private static final int REQUEST_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        FirebaseApp.initializeApp(this);
        setupPermission();
        setupFirebaseAuth();
    }



    private void setupFirebaseAuth() {
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser == null || currentUser.isAnonymous()) return;

        startActivity(new Intent(LoginActivity.this, ChatActivity.class));


    }

    @OnClick({R.id.bt_first_user, R.id.bt_second_user})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_first_user:
                signInUser("user1@tr4n.com", "123456");
                break;
            case R.id.bt_second_user:
                signInUser("user2@tr4n.com", "123456");
                break;
        }
    }

    private void signInUser(String email, String password) {
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private void setupPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION
                );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Warning!")
                        .setMessage("Without permission you can not use this app. " +
                                "Do you want to grant permission?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(
                                        LoginActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        REQUEST_PERMISSION
                                );
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                LoginActivity.this.finish();
                            }
                        })
                        .show();
            }
        }
    }


}
