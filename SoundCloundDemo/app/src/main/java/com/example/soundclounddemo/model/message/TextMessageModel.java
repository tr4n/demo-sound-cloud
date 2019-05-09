package com.example.soundclounddemo.model.message;

import com.example.soundclounddemo.utils.MessageUtil;

public class TextMessageModel extends MessageModel {
    private String content;
    public TextMessageModel(int owner, String content) {
        super(owner);
        super.setType(MessageUtil.TEXT_MESSAGE);
        this.content = content;
    }

    public TextMessageModel(String id, int type, int owner, String content) {
        super(id, type, owner);
        this.content = content;
    }

    public String getContent() {
        return content;
    }


    @Override
    public String toString() {
        return "{"+ getOwner() + ": " + content + "}";
    }
}
