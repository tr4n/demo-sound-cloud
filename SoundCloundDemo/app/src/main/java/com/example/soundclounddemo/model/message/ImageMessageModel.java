package com.example.soundclounddemo.model.message;

import android.net.Uri;

import com.example.soundclounddemo.utils.MessageUtil;

public class ImageMessageModel extends MessageModel {
    private Uri uri;
    private String url;

    public ImageMessageModel(int owner, String url) {
        super(owner);
        super.setType(MessageUtil.IMAGE_MESSAGE);
        this.url = url;
    }

    public ImageMessageModel(String id, int type, int owner, Uri uri, String url) {
        super(id, type, owner);
        this.uri = uri;
        this.url = url;
    }

    public ImageMessageModel(String id, int owner, String url) {
        super(id, owner);
        super.setType(MessageUtil.IMAGE_MESSAGE);
        this.uri = null;
        this.url = url;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getUrl() {
        return url;
    }

}
