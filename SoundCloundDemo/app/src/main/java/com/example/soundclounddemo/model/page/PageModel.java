package com.example.soundclounddemo.model.page;

import com.example.soundclounddemo.model.track.TrackModel;

import java.util.List;

public class PageModel {
    private List<TrackModel> trackModelList;
    private String forwardHref;
    private String previousHref;

    public PageModel(List<TrackModel> trackModelList, String forwardHref, String previousHref) {
        this.trackModelList = trackModelList;
        this.forwardHref = forwardHref;
        this.previousHref = previousHref;
    }

    public List<TrackModel> getTrackModelList() {
        return trackModelList;
    }

    public String getForwardHref() {
        return forwardHref;
    }

    public String getPreviousHref() {
        return previousHref;
    }
}
