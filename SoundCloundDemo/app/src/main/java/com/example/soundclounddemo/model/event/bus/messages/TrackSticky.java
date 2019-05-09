package com.example.soundclounddemo.model.event.bus.messages;

import com.example.soundclounddemo.model.track.TrackModel;

public  class TrackSticky {
    public TrackModel trackModel;

    public TrackSticky(TrackModel trackModel) {
        this.trackModel = trackModel;
    }
}
