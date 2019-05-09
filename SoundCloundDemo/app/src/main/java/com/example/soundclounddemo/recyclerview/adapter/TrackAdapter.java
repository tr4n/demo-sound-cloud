package com.example.soundclounddemo.recyclerview.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.soundclounddemo.R;
import com.example.soundclounddemo.model.event.bus.messages.TrackSticky;
import com.example.soundclounddemo.model.track.TrackModel;
import com.example.soundclounddemo.recyclerview.TrackDiffCallback;
import com.example.soundclounddemo.view.ChatActivity;
import com.example.soundclounddemo.view.ISendTrackListener;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackHolder> {
    private static final String TAG = "TrackAdapter";

    private Context mContext;
    private List<TrackModel> mTrackModelList;

    public TrackAdapter(Context context, List<TrackModel> trackModelList) {
        this.mContext = context;
        this.mTrackModelList = trackModelList;

    }

    @NonNull
    @Override
    public TrackHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.track_item_layout, viewGroup, false);
        return new TrackHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackHolder trackHolder, int i) {
        trackHolder.onBind(mContext, mTrackModelList.get(i));
    }

    @Override
    public int getItemCount() {
        return mTrackModelList.size();
    }

    public void updateTrackList(List<TrackModel> addtionalTrackModelList){
        final TrackDiffCallback trackDiffCallback = new TrackDiffCallback(this.mTrackModelList, addtionalTrackModelList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(trackDiffCallback);
        this.mTrackModelList.addAll(addtionalTrackModelList);
        diffResult.dispatchUpdatesTo(this);
    }

    public class TrackHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_artwork)
        ImageView ivArtwork;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.item_layout)
        ConstraintLayout itemLayout;
        @BindView(R.id.tv_user)
        TextView tvUser;


        private static final String TAG = "TrackHolder";


        public TrackHolder(@NonNull View itemView) {
            super(itemView);
             ButterKnife.bind(this, itemView);


        }

        public void onBind(final Context context, final TrackModel trackModel) {
            Log.d(TAG, "onBind: " + trackModel);
            tvTitle.setText(trackModel.getTitle());
            tvUser.setText(trackModel.getUserName());
            Picasso.get().load(trackModel.getArtworkUrl()).into(ivArtwork);

            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("track", trackModel);
                    EventBus.getDefault().postSticky(new TrackSticky(trackModel));
                    ((Activity) mContext).onBackPressed();

                }
            });


        }
    }
}
