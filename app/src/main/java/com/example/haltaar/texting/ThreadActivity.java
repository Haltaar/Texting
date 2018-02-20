package com.example.haltaar.texting;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

import static java.sql.Types.NULL;

public class ThreadActivity extends AppCompatActivity {

    private static final String TAG = "ThreadActivity";


    ArrayList<String> smsMessagesList = new ArrayList<>();
    ListView messages;
    EditText input;
    SmsManager smsManager = SmsManager.getDefault();
    ArrayAdapter arrayAdapter;

    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    PendingIntent sentPI, deliveredPI;
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;

    String ContactNumber;
    Integer ThreadID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        messages = (ListView) findViewById(R.id.messages);
        input = (EditText) findViewById(R.id.input);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, smsMessagesList);
        messages.setAdapter(arrayAdapter);

        sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);
        ContactNumber = getIntent().getStringExtra("EXTRA_NUMBER");
        ThreadID = getIntent().getIntExtra("EXTRA_THREAD_ID", 0);
        setTitle(getIntent().getStringExtra("EXTRA_NAME") + ": " + ContactNumber);

        refreshThread();

    }

    protected void onResume() {
        super.onResume();

        smsSentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(ThreadActivity.this, "SMS Sent!", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(ThreadActivity.this, "Generic Failure!", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(ThreadActivity.this, "No Service!", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(ThreadActivity.this, "Null PDU!", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(ThreadActivity.this, "Radio Off!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        smsDeliveredReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(ThreadActivity.this, "SMS Delivered", Toast.LENGTH_SHORT).show();
                        break;

                    case  Activity.RESULT_CANCELED:
                        Toast.makeText(ThreadActivity.this, "SMS Not Delivered!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        registerReceiver(smsSentReceiver, new IntentFilter(SENT));
        registerReceiver(smsDeliveredReceiver, new IntentFilter(DELIVERED));

    }

    protected void onPause() {
        super.onPause();

        unregisterReceiver(smsDeliveredReceiver);
        unregisterReceiver(smsSentReceiver);
    }

    public void refreshThread() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexThreadID = smsInboxCursor.getColumnIndex("thread_id");
        String messageLine = "";
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        arrayAdapter.clear();
        do {
            if (smsInboxCursor.getInt(indexThreadID) == ThreadID) {
                Log.d(TAG, "refreshThread: type: " + smsInboxCursor.getInt(8));
                if (smsInboxCursor.getInt(9) == 1){ //checking type for sent or recieved
                    messageLine = getIntent().getStringExtra("EXTRA_NAME") + ": " + smsInboxCursor.getString(indexBody);
                }
                else if (smsInboxCursor.getInt(9) == 2){
                    messageLine = "Me: " + smsInboxCursor.getString(indexBody);

                }
                arrayAdapter.insert(messageLine, 0);
                Log.d(TAG, "0:" + smsInboxCursor.getString(0) +
                        "   1:" + smsInboxCursor.getString(1) +
                        "   2:" + smsInboxCursor.getString(2) +
                        "   3:" + smsInboxCursor.getString(3) +
                        "   4:" + smsInboxCursor.getString(4) +
                        "   5:" + smsInboxCursor.getString(5) +
                        "   6:" + smsInboxCursor.getString(6) +
                        "   7:" + smsInboxCursor.getString(7) +
                        "   8:" + smsInboxCursor.getString(8) +
                        "   9:" + smsInboxCursor.getString(9) +
                        "   10:" + smsInboxCursor.getString(10) +
                        "   11:" + smsInboxCursor.getString(11) +
                        "   12:" + smsInboxCursor.getString(12) +
                        "   13:" + smsInboxCursor.getString(13));
            }
        } while (smsInboxCursor.moveToNext());
    }

    public void onSendClick(View view) {
        String message = input.getText().toString();
            smsManager.sendTextMessage(ContactNumber, null, message, sentPI  , deliveredPI);
    }
}
