package com.example.soundclounddemo.view;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.soundclounddemo.R;
import com.example.soundclounddemo.model.event.bus.messages.TrackSticky;
import com.example.soundclounddemo.model.page.PageModel;
import com.example.soundclounddemo.model.track.TrackModel;
import com.example.soundclounddemo.presenter.MainPresenter;
import com.example.soundclounddemo.recyclerview.adapter.TrackAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchTrackActivity extends AppCompatActivity implements IMainViewListener {
    private static final String TAG = "SearchTrackActivity";

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.rv_tracks)
    RecyclerView rvTracks;
    @BindView(R.id.bt_next)
    Button btNext;
    @BindView(R.id.bt_back)
    Button btBack;
    private MainPresenter mMainPresenter;
    private List<TrackModel> mTrackModelList;
    private String forwardHref = null, previousHref = null;
    private TrackAdapter mTrackAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initialization();
        setupUI();

    }


    private void initialization() {
        mMainPresenter = new MainPresenter(getApplicationContext(), this);
        mTrackModelList = new ArrayList<>();
        mTrackAdapter = new TrackAdapter(this, mTrackModelList);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 1);
        rvTracks.setLayoutManager(mGridLayoutManager);
        rvTracks.setAdapter(mTrackAdapter);
    }

    private void setupUI() {


    }


    @OnClick({R.id.iv_search, R.id.bt_next, R.id.bt_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_next:
                if (forwardHref != null)
                    mMainPresenter.onChangePage(forwardHref);
                break;
            case R.id.bt_back:
                if (previousHref != null)
                    mMainPresenter.onChangePage(previousHref);
                break;
            case R.id.iv_search:
                previousHref = null;
                forwardHref = null;
                mTrackModelList.clear();
                mMainPresenter.onSearchTrack(etSearch.getText().toString());
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onSearchSuccess(PageModel pageModel) {
        Log.d(TAG, "onSearchSuccess: " + pageModel.getTrackModelList());
        this.forwardHref = pageModel.getForwardHref();
        this.previousHref = pageModel.getPreviousHref();
        mTrackAdapter = new TrackAdapter(this, pageModel.getTrackModelList());
        rvTracks.setAdapter(mTrackAdapter);
        //  mTrackAdapter.updateTrackList(pageModel.getTrackModelList());

    }



    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }
    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();

    }
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(TrackSticky trackSticky) {

    };


}
