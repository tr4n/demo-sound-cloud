package com.example.soundclounddemo.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.example.soundclounddemo.model.Container;
import com.example.soundclounddemo.model.player.PlayerModel;
import com.example.soundclounddemo.model.track.TrackModel;
import com.example.soundclounddemo.view.IMainViewListener;
import com.google.android.exoplayer2.extractor.mp4.Track;

import java.util.List;

public class MainPresenter implements IMainPresenterListener {
    private Context mContext;
    private IMainViewListener callback;
    private PlayerModel mPlayerModel;


    public MainPresenter(Context mContext, IMainViewListener callback) {
        this.mContext = mContext;
        this.callback = callback;
        mPlayerModel = new PlayerModel(mContext, this);
    }

    @Override
    public void onSearchTrack(String q) {
        mPlayerModel.onSearchTrack(q);
    }


    @Override
    public void onSearchSuccess(List<TrackModel> trackModelList, String nextHref) {
        callback.onSearchSuccess(trackModelList, nextHref);
    }

    @Override
    public void onNextPage(String url){
        mPlayerModel.onNextPage(url);
    }
}
