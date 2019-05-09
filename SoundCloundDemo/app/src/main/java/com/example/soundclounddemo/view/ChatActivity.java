package com.example.soundclounddemo.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.FileProvider;
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
import com.example.soundclounddemo.model.event.bus.messages.TrackSticky;
import com.example.soundclounddemo.model.message.ImageMessageModel;
import com.example.soundclounddemo.model.message.MessageModel;
import com.example.soundclounddemo.model.message.MusicMessageModel;
import com.example.soundclounddemo.model.message.TextMessageModel;
import com.example.soundclounddemo.model.track.TrackModel;
import com.example.soundclounddemo.realtime.database.RealtimeMessage;
import com.example.soundclounddemo.recyclerview.adapter.MessageAdapter;
import com.example.soundclounddemo.utils.AuthenticationUtils;
import com.example.soundclounddemo.utils.ImageUtils;
import com.example.soundclounddemo.utils.MessageUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity  {

    private static final String TAG = "ChatActivity";
    private static final int PICK_IMAGE_REQUEST = 1;

    private static final int CAMERA_REQUEST = 3;
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
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
    @BindView(R.id.iv_music)
    ImageView ivMusic;
    @BindView(R.id.cl_features)
    ConstraintLayout clFeatures;
    @BindView(R.id.iv_send)
    ImageView ivSend;
    @BindView(R.id.iv_friend_avatar)
    ImageView ivFriendAvatar;

    private StorageReference mStorageReference;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mUserReference;
    private DatabaseReference mFriendReference;
    private List<MessageModel> mMessageModelList = new ArrayList<>();
    private MessageAdapter mMessageAdapter = new MessageAdapter(mMessageModelList, this);
    private Uri capturedImageUri;
    private String mUserEmail = null;
    private TrackModel mTrackModel = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        FirebaseApp.initializeApp(this);
        ButterKnife.bind(this);
        setupFirebase();
        initMessages();
        onMessageListener();
    }


    private void setupFirebase() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = mFirebaseStorage.getReference();
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if (currentUser != null) {
            mUserEmail = currentUser.getEmail();
            DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("messages");

            Log.d(TAG, "setupFirebase: ");
            mUserReference = mDatabaseReference
                    .child(AuthenticationUtils.getValidFileName(mUserEmail))
                    .child(AuthenticationUtils.getValidFileName(AuthenticationUtils.getFriendEmail(mUserEmail)));
            mFriendReference = mDatabaseReference
                    .child(AuthenticationUtils.getValidFileName(AuthenticationUtils.getFriendEmail(mUserEmail)))
                    .child(AuthenticationUtils.getValidFileName(mUserEmail));

        }


    }

    private void initMessages() {
        mMessageModelList = new ArrayList<>();
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 1);
        rvMessages.setLayoutManager(mGridLayoutManager);
        mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMessageModelList = new ArrayList<>();
                //iterate through each user, ignoring their UID
                for (DataSnapshot messageSnapShot : dataSnapshot.getChildren()) {
                    RealtimeMessage realtimeMessage = messageSnapShot.getValue(RealtimeMessage.class);
                    MessageModel messageModel = realtimeMessage.type == MessageUtil.TEXT_MESSAGE ?
                            new TextMessageModel(
                                    realtimeMessage.id,
                                    realtimeMessage.type,
                                    MessageUtil.getOwner(realtimeMessage.own, mUserEmail),
                                    realtimeMessage.content)
                            : realtimeMessage.type == MessageUtil.IMAGE_MESSAGE ?
                            new ImageMessageModel(
                                    realtimeMessage.id,
                                    realtimeMessage.type,
                                    MessageUtil.getOwner(realtimeMessage.own, mUserEmail),
                                    Uri.parse(realtimeMessage.uri),
                                    realtimeMessage.url)
                            : realtimeMessage.type == MessageUtil.MUSIC_MESSAGE ?
                            new MusicMessageModel(
                                    realtimeMessage.id,
                                    realtimeMessage.type,
                                    MessageUtil.getOwner(realtimeMessage.own, mUserEmail),
                                    new TrackModel(
                                            realtimeMessage.trackTitle,
                                            realtimeMessage.artworkUrl,
                                            realtimeMessage.streamUrl,
                                            realtimeMessage.userName
                                    ),
                                    true)
                            : null;

                    Log.d(TAG, "collectDatas: " + messageModel);
                    if (messageModel != null)
                        mMessageModelList.add(messageModel);

                }


//                TrackModel trackModel = new TrackModel(
//                        337995359,
//                        "Reality",
//                        "https://i1.sndcdn.com/artworks-000238403133-5uvmof-large.jpg",
//                        "https://api.soundcloud.com/tracks/337995359/stream",
//                        "RAY VOLPE"
//                );
//                MusicMessageModel musicMessageModel = new MusicMessageModel(
//                        System.currentTimeMillis() + "",
//                        MessageUtil.OWNER,
//                        trackModel,
//                        true
//                );
//                mMessageModelList.add(new MusicMessageModel(
//                        System.currentTimeMillis() + "",
//                        MessageUtil.OWNER,
//                        trackModel,
//                        true
//                ));
//                mMessageModelList.add(new MusicMessageModel(
//                        System.currentTimeMillis() + "",
//                        MessageUtil.FRIENDS,
//                        trackModel,
//                        true
//                ));
                if (mTrackModel != null) addMusicMessage(mTrackModel);

                Collections.sort(mMessageModelList);
                mMessageAdapter = new MessageAdapter(mMessageModelList, ChatActivity.this);
                rvMessages.setAdapter(mMessageAdapter);
                rvMessages.smoothScrollToPosition(mMessageModelList.size());
                avi.hide();
                //   Log.d(TAG, "onDataChange: " + dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @OnClick({R.id.iv_back, R.id.et_message, R.id.iv_avatar, R.id.iv_picture, R.id.iv_camera, R.id.iv_music, R.id.iv_send, R.id.iv_friend_avatar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                CountDownTimer countDownTimer = new CountDownTimer(500, 250) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        FirebaseAuth.getInstance().signOut();
                    }

                    @Override
                    public void onFinish() {
                        onBackPressed();
                    }
                }.start();


                break;
            case R.id.et_message:
                break;
            case R.id.iv_avatar:
                break;
            case R.id.iv_picture:
                openGalley();
                break;
            case R.id.iv_camera:
                openCamera();
                break;
            case R.id.iv_music:
                mTrackModel = null;
                Intent intent = new Intent(ChatActivity.this, SearchTrackActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_send:
                onSendTextMessage();
                break;
            case R.id.iv_friend_avatar:
                break;
        }
    }

    private void onSendTextMessage() {
        try {
            String content = etMessage.getText().toString().trim();
            if (content.length() == 0) return;
            addTextMessage(MessageUtil.OWNER, content);
            etMessage.setText("");
            etMessage.onWindowFocusChanged(false);
        } catch (Exception e) {
            Log.d(TAG, "onViewClicked: " + e.getMessage());
        }
    }

    private void addTextMessage(int owner, String content) {
        MessageModel messageModel = new TextMessageModel(owner, content);
        saveMessageFirebase(messageModel);
        mMessageModelList.add(messageModel);
        mMessageAdapter.notifyItemInserted(mMessageModelList.size() - 1);
        rvMessages.scrollToPosition(mMessageModelList.size() - 1);
    }

    private void addImageMessage(final Uri offlineUri) {
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
                                saveMessageFirebase(imageMessageModel);
                                mMessageModelList.add(imageMessageModel);
                                mMessageAdapter.notifyItemInserted(mMessageModelList.size() - 1);
                                rvMessages.scrollToPosition(mMessageModelList.size() - 1);
                                CountDownTimer countDownTimer = new CountDownTimer(500, 250) {
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


    private void addMusicMessage(TrackModel trackModel) {
        MessageModel messageModel = new MusicMessageModel(MessageUtil.OWNER, trackModel, true);
        saveMessageFirebase(messageModel);

        mMessageModelList.add(messageModel);
        mMessageAdapter.notifyItemInserted(mMessageModelList.size() - 1);
        rvMessages.scrollToPosition(mMessageModelList.size());
        Log.d(TAG, "addMusicMessage: " + mMessageModelList);
        Toast.makeText(ChatActivity.this, "new message", Toast.LENGTH_SHORT).show();
    }

    private void saveMessageFirebase(MessageModel messageModel) {
        if (messageModel == null) return;
        switch (messageModel.getType()) {
            case MessageUtil.TEXT_MESSAGE:
                mUserReference.child(messageModel.getId())
                        .setValue(new RealtimeMessage(
                                messageModel.getId(),
                                messageModel.getType(),
                                mUserEmail,
                                ((TextMessageModel) messageModel).getContent()
                        ));
                mFriendReference.child(messageModel.getId())
                        .setValue(new RealtimeMessage(
                                messageModel.getId(),
                                messageModel.getType(),
                                mUserEmail,
                                ((TextMessageModel) messageModel).getContent()
                        ));
                break;
            case MessageUtil.IMAGE_MESSAGE:
                mUserReference.child(messageModel.getId())
                        .setValue(new RealtimeMessage(
                                messageModel.getId(),
                                messageModel.getType(),
                                mUserEmail,
                                ((ImageMessageModel) messageModel).getUrl(),
                                ((ImageMessageModel) messageModel).getUri().toString()
                        ));
                mFriendReference.child(messageModel.getId())
                        .setValue(new RealtimeMessage(
                                messageModel.getId(),
                                messageModel.getType(),
                                mUserEmail,
                                ((ImageMessageModel) messageModel).getUrl(),
                                ((ImageMessageModel) messageModel).getUri().toString()
                        ));
                break;
            case MessageUtil.MUSIC_MESSAGE:
                mUserReference.child(messageModel.getId())
                        .setValue(new RealtimeMessage(
                                messageModel.getId(),
                                messageModel.getType(),
                                mUserEmail,
                                ((MusicMessageModel) messageModel).getTrackModel().getTitle(),
                                ((MusicMessageModel) messageModel).getTrackModel().getUserName(),
                                ((MusicMessageModel) messageModel).getTrackModel().getArtworkUrl(),
                                ((MusicMessageModel) messageModel).getTrackModel().getStreamUrl()
                        ));
                mFriendReference.child(messageModel.getId())
                        .setValue(new RealtimeMessage(
                                messageModel.getId(),
                                messageModel.getType(),
                                mUserEmail,
                                ((MusicMessageModel) messageModel).getTrackModel().getTitle(),
                                ((MusicMessageModel) messageModel).getTrackModel().getUserName(),
                                ((MusicMessageModel) messageModel).getTrackModel().getArtworkUrl(),
                                ((MusicMessageModel) messageModel).getTrackModel().getStreamUrl()
                        ));

                break;
        }
    }


    private void onMessageListener() {
        mUserReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                RealtimeMessage realtimeMessage = dataSnapshot.getValue(RealtimeMessage.class);
                if (MessageUtil.getOwner(realtimeMessage.own, mUserEmail) == MessageUtil.OWNER)
                    return;
                MessageModel messageModel = realtimeMessage.type == MessageUtil.TEXT_MESSAGE ?
                        new TextMessageModel(
                                realtimeMessage.id,
                                realtimeMessage.type,
                                MessageUtil.getOwner(realtimeMessage.own, mUserEmail),
                                realtimeMessage.content)
                        : realtimeMessage.type == MessageUtil.IMAGE_MESSAGE ?
                        new ImageMessageModel(
                                realtimeMessage.id,
                                realtimeMessage.type,
                                MessageUtil.getOwner(realtimeMessage.own, mUserEmail),
                                Uri.parse(realtimeMessage.uri),
                                realtimeMessage.url)
                        : realtimeMessage.type == MessageUtil.MUSIC_MESSAGE ?
                        new MusicMessageModel(
                                realtimeMessage.id,
                                realtimeMessage.type,
                                MessageUtil.getOwner(realtimeMessage.own, mUserEmail),
                                new TrackModel(
                                        realtimeMessage.trackTitle,
                                        realtimeMessage.artworkUrl,
                                        realtimeMessage.streamUrl,
                                        realtimeMessage.userName
                                ),
                                true)
                        : null;

                // Log.d(TAG, "onChildAdded: " + messageModel);
                //    int oldPosition = mMessageModelList.size();
                if (messageModel != null) {
                    mMessageModelList.add(messageModel);
                    mMessageAdapter.notifyItemInserted(mMessageModelList.size() - 1);
                   // rvMessages.setAdapter(mMessageAdapter);
                    rvMessages.smoothScrollToPosition(mMessageModelList.size());
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File tempFile = null;
        try {
            tempFile = File.createTempFile(
                    System.currentTimeMillis() + "",
                    ".jpg",
                    this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            );
            tempFile.deleteOnExit();

            Log.d(TAG, "getUriCapturedImage: " + tempFile.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //
        capturedImageUri = FileProvider.getUriForFile(
                this,
                this.getPackageName() + ".provider",
                tempFile
        );
        intent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri uri = data.getData();
            onPickImageRequest(uri);

        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            onGetCapturedImageRequest(capturedImageUri);
        }
    }

    private void onPickImageRequest(Uri uri) {
        try {

//            ExifInterface exifInterface = new ExifInterface(ImageUtils.getPathUri(this, uri));
//            int orientation = exifInterface.getAttributeInt(
//                    ExifInterface.TAG_ORIENTATION,
//                    ExifInterface.ORIENTATION_NORMAL
//            );
//
//            Bitmap bitmap = ImageUtils.rotateBitmap(
//                    MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
//                    orientation
//            );
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            long size = ImageUtils.getSizefromUri(this, uri);
            // Log.d(TAG, "onActivityResult: " + size);
            //  Bitmap resizedBitmap = size > 1024 ? ImageUtils.getResizedBitmap(bitmap, size) : bitmap;
            Uri resizedUri = ImageUtils.getImageUri(this, bitmap);
            //  Log.d(TAG, "onActivityResult new: "+ ImageUtils.getSizefromUri(this, resizedUri));
            addImageMessage(resizedUri);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "onPickImageRequest: " + e.getMessage());
        }
    }

    private void onGetCapturedImageRequest(Uri uri) {
        try {
//            ExifInterface exifInterface = new ExifInterface(ImageUtils.getPathUri(this, uri));
//            int orientation = exifInterface.getAttributeInt(
//                    ExifInterface.TAG_ORIENTATION,
//                    ExifInterface.ORIENTATION_NORMAL
//            );
//
//            Bitmap bitmap = ImageUtils.rotateBitmap(
//                    MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
//                    orientation
//            );

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            Uri resizedUri = ImageUtils.getImageUri(this, bitmap);
            Log.d(TAG, "onGetCapturedImageRequest: " + resizedUri);
            addImageMessage(resizedUri);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "onGetCapturedImageRequest: " + e.getMessage());
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();

    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        mTrackModel = null;
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(TrackSticky trackSticky) {
        mTrackModel = trackSticky.trackModel;
        if(mTrackModel != null) {
            addMusicMessage(mTrackModel);
            EventBus.getDefault().removeAllStickyEvents();
            mTrackModel = null;
        }
        Log.d(TAG, "onMessageEvent: " + trackSticky);

    };

    @Override
    protected void onRestart() {
        super.onRestart();


    }
}
