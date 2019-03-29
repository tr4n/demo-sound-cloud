package com.example.soundclounddemo.presenter;

import android.support.v7.widget.RecyclerView;

import com.example.soundclounddemo.model.Container;
import com.example.soundclounddemo.model.track.TrackModel;

import java.util.List;

public interface IMainPresenterListener {
    void onSearchTrack(String q);
    void onSearchSuccess(List<TrackModel> trackModelList, String nextHref);
    void onNextPage(String url);
}
