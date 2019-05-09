package com.example.soundclounddemo.utils;

import android.util.Log;

public class AuthenticationUtils {

    private static final String TAG = "AuthenticationUtils";
    private static final String FIRST_EMAIL = "user1@tr4n.com";
    private static final String SECOND_EMAIL = "user2@tr4n.com";

    public static String getValidFileName(String input) {
        if (input == null) return null;
        Log.d(TAG, "getValidFileName: " + input);
        String output = input.replace('@', '-').replace(".", "--");
        Log.d(TAG, "getValidFileName: " + output);
        return output;

    }
    public static String getIdBox(String first, String second){
        return first.compareTo(second) > 0 ? getValidFileName(first + "_" +second)
                : getValidFileName(second + "_" + first);
    }
    public static String getFriendEmail(String email){
        return email.equals(FIRST_EMAIL) ? SECOND_EMAIL : FIRST_EMAIL;
    }
}
