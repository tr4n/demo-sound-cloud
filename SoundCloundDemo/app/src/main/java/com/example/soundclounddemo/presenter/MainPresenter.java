package com.example.soundclounddemo.presenter;

import android.content.Context;

import com.example.soundclounddemo.model.page.PageModel;
import com.example.soundclounddemo.model.player.PlayerModel;
import com.example.soundclounddemo.view.IMainViewListener;

public class MainPresenter implements IMainPresenterListener {
    private IMainViewListener callback;
    private PlayerModel mPlayerModel;


    public MainPresenter(Context mContext, IMainViewListener callback) {
        Context mContext1 = mContext;
        this.callback = callback;
        mPlayerModel = new PlayerModel(mContext, this);
    }

    @Override
    public void onSearchTrack(String q) {
        mPlayerModel.onSearchTrack(q);
    }


    @Override
    public void onSearchSuccess(PageModel pageModel) {
        callback.onSearchSuccess(pageModel);
    }

    @Override
    public void onChangePage(String url){
        mPlayerModel.onChangePage(url);
    }
}
