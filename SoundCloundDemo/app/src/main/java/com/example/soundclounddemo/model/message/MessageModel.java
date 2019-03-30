package com.example.soundclounddemo.model.message;

import com.example.soundclounddemo.utils.MessageUtil;
import com.example.soundclounddemo.utils.TimeHandle;

import java.util.Calendar;
import java.util.Date;

public class MessageModel {
    private String id;
    private String time;
    private int type;
    private int owner;
    private Date createAt;

    public MessageModel(String id, int owner) {
        this.id = id;
        this.owner = owner;
        this.owner = owner;
        this.type = MessageUtil.IMAGE_MESSAGE;
        this.createAt = Calendar.getInstance().getTime();
        this.time = TimeHandle.getInstance().getDisplayTime(createAt);
    }

    public MessageModel(int owner) {
        this.owner = owner;
        this.type = MessageUtil.TEXT_MESSAGE;
        this.owner = owner;
        this.id = "" + System.currentTimeMillis();
        this.createAt = Calendar.getInstance().getTime();
        this.time = TimeHandle.getInstance().getDisplayTime(createAt);
    }

    public String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    void setType(int type) {
        this.type = type;
    }

    public int getOwner() {
        return owner;
    }



    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
