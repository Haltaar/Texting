package com.example.haltaar.texting;

import android.util.Base64;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test public void mainActivity_notNull() throws Exception {
        MainActivity mainActivity = new MainActivity();
        assertNotNull(mainActivity);
    }

    @Test public void ciphers_notNull() throws Exception {
        Ciphers ciphers = new Ciphers();
        assertNotNull(ciphers);
    }

    @Test public void ciphers_validateCaeser() throws Exception {
        String message = "Hello World!";
        String encryptedMessage = "Uryyb Jbeyq!";
        int shift = 13;

        assertEquals(encryptedMessage, Ciphers.caesar(message, shift));
        assertEquals(message, Ciphers.caesar(encryptedMessage, shift));
    }

//    @Test public void ciphers_validateAES() throws Exception { //This test fails due to issues with mocking the required classes such as Base64
//        String message = "Hello World!";
//        String password = "Password";
//        String encryptedMessage = Ciphers.AESEncrypt(message, password);
//
//        assertEquals(message, Ciphers.AESDecrypt(encryptedMessage, password));
//    }
}
