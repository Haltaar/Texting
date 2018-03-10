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

        if (shift > 26) {
            shift = shift % 26;
        }

        for (int i = 0; i < len; i++){
            char c_current = (char)(msg.charAt(i));
            char c_shift = (char)(c_current + shift);

            if (Character.isLetter(c_current)) {
                if (Character.isUpperCase(c_current)) {
                    if (c_shift > 'Z') {
                        result.append((char)(c_current - (26 - shift)));
                    } else {
                        result.append(c_shift);
                    }
                }
                else if (Character.isLowerCase(c_current)) {
                    if (c_shift > 'z') {
                        result.append((char)(c_current - (26 - shift)));
                    } else {
                        result.append(c_shift);
                    }
                }
            }
            else {
                result.append(c_current);
            }
        }
        return result.toString();
    }
}
