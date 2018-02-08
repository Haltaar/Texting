package com.example.haltaar.texting;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ConvosActivity extends AppCompatActivity {

    ArrayList<String> convoThreads = new ArrayList<>();
    ArrayList<Integer> threads = new ArrayList<>();
    ListView convos;
    ArrayAdapter convoAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convos);

        convos = (ListView) findViewById(R.id.convos);
        convoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, convoThreads);
        convos.setAdapter(convoAdapter);

        refreshConvos();

    }

    public void refreshConvos() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexThreadID = smsInboxCursor.getColumnIndex("thread_id");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        if (indexThreadID < 0 || !smsInboxCursor.moveToFirst()) return;
        convoAdapter.clear();
        do {
            if(!threads.contains(smsInboxCursor.getInt(indexThreadID)))
            {
                threads.add(smsInboxCursor.getInt(indexThreadID));
                String str = "SMS From: " + smsInboxCursor.getString(indexAddress) +
                        "\n" + smsInboxCursor.getString(indexThreadID) + "\n";
                convoAdapter.add(str);

            }

        } while (smsInboxCursor.moveToNext());
    }




}
