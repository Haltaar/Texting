package com.example.haltaar.texting;

import org.junit.Test;

import javax.crypto.Cipher;

import static org.junit.Assert.*;

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
        assertEquals("Uryyb Jbeyq!", Ciphers.caesar("Hello World!", 13));
    }

    @Test public void ciphers_validateAES() throws Exception {
        assertEquals("PG52LspJHfCa/Et2KTGqBw==", Ciphers.AESEncrypt("Hello World!", "Password"));
    }
}
