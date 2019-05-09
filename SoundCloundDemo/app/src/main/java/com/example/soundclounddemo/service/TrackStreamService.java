package com.example.soundclounddemo.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.soundclounddemo.R;
import com.example.soundclounddemo.model.track.TrackModel;

import hybridmediaplayer.HybridMediaPlayer;

public class TrackStreamService extends Service {

    private static final String TAG = "TrackStreamService";

    public TrackStreamService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        TrackModel mTrackModel = intent.getParcelableExtra("track");
        Log.d(TAG, "onStartCommand: " + mTrackModel);
        playTrack(mTrackModel);
        return super.onStartCommand(intent, flags, startId);
    }

    private void playTrack(TrackModel trackModel) {
        try {
            Log.d(TAG, "playTrack: " + trackModel.getStreamUrl());
            final HybridMediaPlayer hybridMediaPlayer = HybridMediaPlayer.getInstance(this);
            String url = trackModel.getStreamUrl() + "?client_id=" + this.getString(R.string.client_id);
            hybridMediaPlayer.setDataSource(url);
            hybridMediaPlayer.setOnPreparedListener(new HybridMediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(HybridMediaPlayer player) {

                    Toast.makeText(TrackStreamService.this, "Đang load bài hát của Nung ^^ ", Toast.LENGTH_SHORT).show();
                }
            });
            hybridMediaPlayer.setOnCompletionListener(new HybridMediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(HybridMediaPlayer player) {
                    // Toast.makeText(TrackStreamService.this, "Hết rồi ^^ Cảm ơn các bạn đã lắng nghe", Toast.LENGTH_SHORT).show();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hybridMediaPlayer.seekTo(0);
                        }
                    }, 1000);

                }
            });


            hybridMediaPlayer.prepare();
            hybridMediaPlayer.play();
//        CountDownTimer countDownTimer = new CountDownTimer(20000, 10000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//
//            }
//
//            @Override
//            public void onFinish() {
//                hybridMediaPlayer.seekTo(100000);
//            }
//        }.start();

            Log.d(TAG, "playTrack: " + "playing");
        } catch (NullPointerException e) {
            Log.e(TAG, "playTrack: " + e.getMessage());
        }


    }

    @Override
    public void onDestroy() {
        HybridMediaPlayer.getInstance(this).release();
        super.onDestroy();
    }
}
