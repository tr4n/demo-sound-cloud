package com.example.soundclounddemo.model.track;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Annotation;

public class TrackModel implements Parcelable {
    private long id;
    private String title;
    private String artworkUrl;
    private String streamUrl;
    private String userName;

    public TrackModel(long id, String title, String artworkUrl, String streamUrl, String userName) {
        this.id = id;
        this.title = title;
        this.artworkUrl = artworkUrl;
        this.streamUrl = streamUrl;
        this.userName = userName;
    }

    public TrackModel(String title, String artworkUrl, String streamUrl, String userName) {
        this.title = title;
        this.artworkUrl = artworkUrl;
        this.streamUrl = streamUrl;
        this.userName = userName;
    }

    protected TrackModel(Parcel in) {
        id = in.readLong();
        title = in.readString();
        artworkUrl = in.readString();
        streamUrl = in.readString();
        userName = in.readString();
    }

    public static final Creator<TrackModel> CREATOR = new Creator<TrackModel>() {
        @Override
        public TrackModel createFromParcel(Parcel in) {
            return new TrackModel(in);
        }

        @Override
        public TrackModel[] newArray(int size) {
            return new TrackModel[size];
        }
    };

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtworkUrl() {
        return artworkUrl;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public String getUserName() {
        return userName;
    }

    public static Creator<TrackModel> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "{id="+id +",title="+ title+"}";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(artworkUrl);
        dest.writeString(streamUrl);
        dest.writeString(userName);
    }



}
