package com.example.haltaar.texting;

import android.util.Log;

import java.nio.charset.CharsetEncoder;

import static android.content.ContentValues.TAG;

/**
 * Created by haltaar on 26/02/18.
 */



public class ciphers {

    private static final String TAG = "ThreadActivity";

    public static String caesar(String msg, int shift) {
        int len = msg.length();
        StringBuilder result = new StringBuilder(len);

        for (int i = 0; i < len; i++){
            char c_temp = (char)(msg.charAt(i) + shift);

            if (Character.isLetter(msg.charAt(i))) {
                if (c_temp > 'z') {
                    result.append((char) (msg.charAt(i) - (26 - shift)));
                    Log.d(TAG, "caesar: 1 result: " + result.toString());
                }
                else {
                    result.append((char) (msg.charAt(i) + shift));
                    Log.d(TAG, "caesar: 2 result: " + result.toString());
                }
            }
            else {
                result.append(msg.charAt(i));
                Log.d(TAG, "caesar: 3 result: " + result.toString());
            }

        }
        Log.d(TAG, "caesar: final result: " + result.toString());
        return result.toString();
    }

}
