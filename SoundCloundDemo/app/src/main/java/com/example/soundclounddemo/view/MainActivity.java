package com.example.soundclounddemo.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.soundclounddemo.R;
import com.example.soundclounddemo.recyclerview.adapter.TrackAdapter;
import com.example.soundclounddemo.model.track.TrackModel;
import com.example.soundclounddemo.presenter.MainPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements IMainViewListener {
    private static final String TAG = "MainActivity";

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.rv_tracks)
    RecyclerView rvTracks;
    @BindView(R.id.bt_next)
    Button btNext;
    private MainPresenter mMainPresenter;
    private List<TrackModel> mTrackModelList;
    private String nextHref = null;
    private TrackAdapter mTrackAdapter ;
    private GridLayoutManager mGridLayoutManager ;

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
         mGridLayoutManager = new GridLayoutManager(this, 1);
         rvTracks.setAdapter(mTrackAdapter);
    }

    private void setupUI() {


    }


    @OnClick({R.id.iv_search, R.id.bt_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_next:
                if(nextHref != null)
                    mMainPresenter.onNextPage(nextHref);
                break;
            case R.id.iv_search:
                nextHref = null;
                mTrackModelList.clear();
                mMainPresenter.onSearchTrack(etSearch.getText().toString());
                break;
        }
    }

    @Override
    public void onSearchSuccess(List<TrackModel> trackModelList, String nextHref) {
        Log.d(TAG, "onSearchSuccess: " + trackModelList);
        this.nextHref = nextHref;
        mTrackAdapter.updateTrackList(trackModelList);

    }
}
