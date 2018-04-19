package com.example.haltaar.texting;

import android.util.Base64;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by haltaar on 26/02/18.
 */



public class Ciphers {

    private static final String TAG = "Ciphers";

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

    public static String AESEncrypt(String msg, String password) throws Exception{
        SecretKeySpec key = generateAESKey(password);
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = c.doFinal(msg.getBytes());
        String encryptedString = Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
        return encryptedString;
    }

    public static String AESDecrypt(String msg, String password) throws Exception {
        SecretKeySpec key = generateAESKey(password);
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedBytes = Base64.decode(msg, Base64.DEFAULT);
        byte[] decryptedBytes = c.doFinal(decodedBytes);
        String decryptedString = new String(decryptedBytes);
        return decryptedString;
    }

    private static SecretKeySpec generateAESKey(String password) throws Exception{
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();

        SecretKeySpec secretKeySpec = new SecretKeySpec(key, 0,key.length , "AES");
        return secretKeySpec;
    }


}
