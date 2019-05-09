package com.example.soundclounddemo.utils;

public class MessageUtil {
    public static final int TEXT_MESSAGE = 1;
    public static final int IMAGE_MESSAGE = 2;
    public static final int MUSIC_MESSAGE = 3;
    public static final int OWNER = 10;
    public static final int FRIENDS = 20;

    public static int getOwner(String input, String email){
        if(input == null || email == null) return -1;
        return input.equals(email) ? OWNER: FRIENDS;
    }
}
