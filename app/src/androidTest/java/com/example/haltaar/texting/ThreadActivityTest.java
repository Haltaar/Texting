package com.example.haltaar.texting;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by haltaar on 06/04/18.
 */

public class ThreadActivityTest extends ActivityInstrumentationTestCase2<ThreadActivity> {

    private ThreadActivity testActivity;
    private EditText testText;
    private Button testCaesar;
    private Button testAES;
    private Button testSend;

    public ThreadActivityTest() {
        super(ThreadActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        testActivity = getActivity();
        testText = (EditText) testActivity.findViewById(R.id.input);
        testCaesar = (Button) testActivity.findViewById(R.id.button);
        testAES = (Button) testActivity.findViewById(R.id.buttonAES);
        testSend = (Button) testActivity.findViewById(R.id.send);
    }

    public void testPreconditions() {
        assertNotNull("testActivity is null", testActivity);
        assertNotNull("testText is null", testText);
        assertNotNull("testCaesar is null", testCaesar);
        assertNotNull("testAES is null", testAES);
        assertNotNull("testSend is null", testSend);
    }
}
