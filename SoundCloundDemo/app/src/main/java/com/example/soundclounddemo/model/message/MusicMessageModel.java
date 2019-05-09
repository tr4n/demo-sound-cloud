package com.example.soundclounddemo.model.message;

import com.example.soundclounddemo.model.track.TrackModel;
import com.example.soundclounddemo.utils.MessageUtil;

public class MusicMessageModel extends MessageModel {
   private TrackModel trackModel;
   private boolean isRequest;

    public MusicMessageModel(String id, int owner, TrackModel trackModel, boolean isRequest) {
        super(id, owner);
        super.setType(MessageUtil.MUSIC_MESSAGE);
        this.trackModel = trackModel;
        this.isRequest = isRequest;
    }

    public MusicMessageModel(String id, int type, int owner, TrackModel trackModel, boolean isRequest) {
        super(id, type, owner);
        this.trackModel = trackModel;
        this.isRequest = isRequest;
    }

    public MusicMessageModel(int owner, TrackModel trackModel, boolean isRequest) {
        super(owner);
        super.setType(MessageUtil.MUSIC_MESSAGE);
        this.trackModel = trackModel;
        this.isRequest = isRequest;
    }

    public TrackModel getTrackModel() {
        return trackModel;
    }

    public boolean isRequest() {
        return isRequest;
    }

    @Override
    public String toString() {
        return "{" + trackModel.getTitle() + ", " + trackModel.getUserName() +"}";
    }
}
