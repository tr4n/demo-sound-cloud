package com.example.soundclounddemo.realtime.database;

public class RealtimeMessage {
    public String id;
    public int type;
    public String own;
    public String content;
    public String url;
    public String uri;
    public String trackTitle;
    public String userName;
    public String artworkUrl;
    public String streamUrl;


    public RealtimeMessage() {
    }

    public RealtimeMessage(String id, int type, String own, String content) {
        this.id = id;
        this.type = type;
        this.own = own;
        this.content = content;
    }

    public RealtimeMessage(String id, int type, String own, String url, String uri) {
        this.id = id;
        this.type = type;
        this.own = own;
        this.url = url;
        this.uri = uri;
    }

    public RealtimeMessage(String id, int type, String own, String trackTitle, String userName, String artworkUrl, String streamUrl) {
        this.id = id;
        this.type = type;
        this.own = own;
        this.trackTitle = trackTitle;
        this.userName = userName;
        this.artworkUrl = artworkUrl;
        this.streamUrl = streamUrl;
    }
}
