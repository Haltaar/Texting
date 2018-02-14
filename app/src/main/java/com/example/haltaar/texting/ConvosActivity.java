package com.example.haltaar.texting;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

        convos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent treadIntent = new Intent(getBaseContext(), ThreadActivity.class);
                treadIntent.putExtra("EXTRA_THREAD_ID", threads.get(position));

                startActivity(treadIntent);
            }
        });

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
                String str = getContactName(this, smsInboxCursor.getString(indexAddress)) +
                        "\n Thread ID:" + smsInboxCursor.getString(indexThreadID) + "\n";
                convoAdapter.add(str);

            }

        } while (smsInboxCursor.moveToNext());
    }





    public static String getContactName(Context context, String phoneNo) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNo));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return phoneNo;
        }
        String Name = phoneNo;
        if (cursor.moveToFirst()) {
            Name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return Name;
    }
}
