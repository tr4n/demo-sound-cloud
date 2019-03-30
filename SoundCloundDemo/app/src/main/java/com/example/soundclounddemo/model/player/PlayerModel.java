package com.example.soundclounddemo.model.player;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.soundclounddemo.R;
import com.example.soundclounddemo.model.page.PageModel;
import com.example.soundclounddemo.model.track.TrackModel;
import com.example.soundclounddemo.network.response.ISoundCloudService;
import com.example.soundclounddemo.network.response.SearchPaginationResponse;
import com.example.soundclounddemo.network.singleton.RetrofitSingleton;
import com.example.soundclounddemo.presenter.IMainPresenterListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayerModel {
    private IMainPresenterListener callback;
    private static final String TAG = "PlayerModel";
    private Context mContext;


    public PlayerModel(Context context, IMainPresenterListener callback) {
        this.callback = callback;
        this.mContext = context;
    }

    public void onSearchTrack(String q) {
        Log.d(TAG, "onSearchTrack: " + "onsearch" + " " + q);
        RetrofitSingleton.getInstance().create(ISoundCloudService.class)
                .getSearchPaginationResponse(q, mContext.getString(R.string.client_id),1, 15)
                .enqueue(new Callback<SearchPaginationResponse>() {
                    @Override
                    public void onResponse(Call<SearchPaginationResponse> call, Response<SearchPaginationResponse> response) {
                        List<TrackModel> trackModelList = new ArrayList<>();

                        for (SearchPaginationResponse.TrackJson trackJson : response.body().getCollection()) {
                            trackModelList.add(new TrackModel(
                                    trackJson.getId(),
                                    trackJson.getTitle(),
                                    trackJson.getArtwork_url(),
                                    trackJson.getStream_url(),
                                    trackJson.getUser().getUsername()
                            ));
                        }

                        Log.d(TAG, "onResponse: " + trackModelList);
                        PageModel pageModel = new PageModel(
                                trackModelList,
                                response.body().getNext_href(),
                                response.raw().request().url().toString()
                                );
                        callback.onSearchSuccess(pageModel);
                    }

                    @Override
                    public void onFailure(Call<SearchPaginationResponse> call, Throwable t) {
                        Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });

    }

    public void onChangePage(String url){
        RetrofitSingleton.getInstance().create(ISoundCloudService.class)
                .getSearchPaginationNextHrefResponse(url)
                .enqueue(new Callback<SearchPaginationResponse>() {
                    @Override
                    public void onResponse(Call<SearchPaginationResponse> call, Response<SearchPaginationResponse> response) {
                        List<TrackModel> trackModelList = new ArrayList<>();

                        for (SearchPaginationResponse.TrackJson trackJson : response.body().getCollection()) {
                            trackModelList.add(new TrackModel(
                                    trackJson.getId(),
                                    trackJson.getTitle(),
                                    trackJson.getArtwork_url(),
                                    trackJson.getStream_url(),
                                    trackJson.getUser().getUsername()
                            ));
                        }
                        PageModel pageModel = new PageModel(
                                trackModelList,
                                response.body().getNext_href(),
                                response.raw().request().url().toString()
                        );
                        callback.onSearchSuccess(pageModel);
                    }

                    @Override
                    public void onFailure(Call<SearchPaginationResponse> call, Throwable t) {
                        Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });


    }




}
