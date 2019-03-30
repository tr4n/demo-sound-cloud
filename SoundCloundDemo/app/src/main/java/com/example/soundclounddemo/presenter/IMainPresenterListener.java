package com.example.soundclounddemo.presenter;

import android.support.v7.widget.RecyclerView;

import com.example.soundclounddemo.model.Container;
import com.example.soundclounddemo.model.page.PageModel;
import com.example.soundclounddemo.model.track.TrackModel;

import java.util.List;

public interface IMainPresenterListener {
    void onSearchTrack(String q);
    void onSearchSuccess(PageModel pageModel);
    void onChangePage(String url);
}
