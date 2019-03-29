package com.example.soundclounddemo.recyclerview;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.example.soundclounddemo.model.track.TrackModel;

import java.util.List;

public class TrackDiffCallback extends DiffUtil.Callback {
    private final List<TrackModel> mOldTrackList;
    private final List<TrackModel> mNewTrackList;

    public TrackDiffCallback(List<TrackModel> mOldTrackList, List<TrackModel> mNewTrackList) {
        this.mOldTrackList = mOldTrackList;
        this.mNewTrackList = mNewTrackList;
    }

    @Override
    public int getOldListSize() {
        return mOldTrackList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewTrackList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldTrackList.get(oldItemPosition).getId() == mNewTrackList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int i, int i1) {
        final TrackModel oldTrack = mOldTrackList.get(i);
        final TrackModel newTrack = mNewTrackList.get(i1);

        return oldTrack.getTitle().equals(newTrack.getTitle());

    }

}
