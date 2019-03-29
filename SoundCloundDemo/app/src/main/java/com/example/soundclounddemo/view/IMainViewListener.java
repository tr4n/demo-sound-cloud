package com.example.soundclounddemo.view;

import com.example.soundclounddemo.model.track.TrackModel;

import java.util.List;

public interface IMainViewListener {
    void onSearchSuccess(List<TrackModel> trackModelList, String nextHref);

}
