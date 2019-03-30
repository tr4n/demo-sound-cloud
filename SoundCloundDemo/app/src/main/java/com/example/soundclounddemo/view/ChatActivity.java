package com.example.soundclounddemo.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soundclounddemo.R;
import com.example.soundclounddemo.model.message.ImageMessageModel;
import com.example.soundclounddemo.model.message.MessageModel;
import com.example.soundclounddemo.model.message.TextMessageModel;
import com.example.soundclounddemo.recyclerview.adapter.MessageAdapter;
import com.example.soundclounddemo.utils.ImageUtil;
import com.example.soundclounddemo.utils.MessageUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";
    private static final int PICK_IMAGE_REQUEST = 1;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_friend_name)
    TextView tvFriendName;
    @BindView(R.id.tv_friend_email)
    TextView tvFriendEmail;
    @BindView(R.id.et_message)
    EditText etMessage;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.iv_picture)
    ImageView ivPicture;
    @BindView(R.id.rv_messages)
    RecyclerView rvMessages;
    @BindView(R.id.constraintLayout3)
    ConstraintLayout constraintLayout3;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;
    private FirebaseAuth mFirebaseAuth;
    private List<MessageModel> mMessageModelList = new ArrayList<>();
    private MessageAdapter mMessageAdapter = new MessageAdapter(mMessageModelList, this);
    private GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        FirebaseApp.initializeApp(this);
        ButterKnife.bind(this);

        initialization();
        setupFirebaseStorage();
    }

    private void initialization() {

        mMessageModelList.add(new TextMessageModel(MessageUtil.OWNER, "Hello"));
        mMessageModelList.add(new TextMessageModel(MessageUtil.FRIENDS, "Hi"));
        mMessageModelList.add(new TextMessageModel(MessageUtil.OWNER, "What is your name ?"));
        mMessageModelList.add(new TextMessageModel(MessageUtil.FRIENDS, "My name is Huy"));
        mMessageModelList.add(new TextMessageModel(MessageUtil.OWNER, "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."));
        mMessageModelList.add(new TextMessageModel(MessageUtil.FRIENDS, "Nice to meet you too"));
        mMessageModelList.add(new TextMessageModel(MessageUtil.FRIENDS, "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."));

        mMessageAdapter = new MessageAdapter(mMessageModelList, this);
        mGridLayoutManager = new GridLayoutManager(this, 1);
        rvMessages.setLayoutManager(mGridLayoutManager);
        rvMessages.setAdapter(mMessageAdapter);
        avi.hide();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();


            try {
                long size = ImageUtil.getSizefromUri(this, uri);
                Log.d(TAG, "onActivityResult: " + size);
                if (size > 4096) {
                    Toast.makeText(ChatActivity.this, "The image is langer than 4Mb", Toast.LENGTH_SHORT).show();
                } else {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                    // Log.d(TAG, String.valueOf(bitmap));;
                    //     ivAvatar.setImageBitmap(bitmap);
                    uploadImage(uri);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
    }

    @OnClick({R.id.iv_back, R.id.et_message, R.id.iv_avatar, R.id.iv_picture})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                break;
            case R.id.et_message:
                break;
            case R.id.iv_avatar:
                break;
            case R.id.iv_picture:
                openGalley();
                break;
        }
    }

    private void uploadImage(final Uri offlineUri) {
        avi.show();
        final String id = "" + System.currentTimeMillis();
        final StorageReference imagesReference = mStorageReference.child("images/" + id + ".jpg");
        //  imagesReference.child(name);
        imagesReference.putFile(offlineUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Log.d(TAG, "onSuccess: ");
                        Toast.makeText(ChatActivity.this, "success", Toast.LENGTH_SHORT).show();
                        imagesReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                ImageMessageModel imageMessageModel = new ImageMessageModel(
                                        id,
                                        MessageUtil.OWNER,
                                        uri.toString()
                                );
                                imageMessageModel.setUri(offlineUri);
                                mMessageModelList.add(imageMessageModel);
                                mMessageAdapter.notifyItemInserted(mMessageModelList.size() - 1);
                                rvMessages.scrollToPosition(mMessageModelList.size() - 1);
                                CountDownTimer countDownTimer = new CountDownTimer(500,250) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }

                                    @Override
                                    public void onFinish() {
                                        avi.hide();
                                    }
                                }.start();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                        Toast.makeText(ChatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupFirebaseStorage() {
        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = mFirebaseStorage.getReferenceFromUrl("gs://soundclounddemo.appspot.com");
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            //  updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(ChatActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //    updateUI(null);
                        }

                        // ...
                    }
                });


    }

    private void openGalley() {
        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

}
