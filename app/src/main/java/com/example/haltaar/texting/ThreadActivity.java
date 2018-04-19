package com.example.haltaar.texting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.ArrayList;

public class ThreadActivity extends AppCompatActivity {

    private static final String TAG = "ThreadActivity";

    private static ThreadActivity inst;
    public static boolean active = false;

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
        registerForContextMenu(messages);
        input = (EditText) findViewById(R.id.input);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, smsMessagesList);
        messages.setAdapter(arrayAdapter);

        sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);
        ContactNumber = getIntent().getStringExtra("EXTRA_NUMBER");
        ThreadID = getIntent().getIntExtra("EXTRA_THREAD_ID", 0);
        String Title = "";
        Title = getIntent().getStringExtra("EXTRA_NAME");
        if (Title.equals(""))
            Title = "New Message";
        setTitle(Title + ": " + ContactNumber);

        refreshThread();
    }

    public void onStart() {
        super.onStart();
        inst = this;
    }

    protected void onResume() {
        super.onResume();

        active = true;

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

        active = false;

        unregisterReceiver(smsDeliveredReceiver);
        unregisterReceiver(smsSentReceiver);
    }

    public static ThreadActivity instance() {
        return inst;
    }

    public void refreshThread() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexThreadID = smsInboxCursor.getColumnIndex("thread_id");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        int indexType = smsInboxCursor.getColumnIndex("type");
        String messageLine = "";
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        arrayAdapter.clear();
        do {
            if (ThreadID == 0) //if message is new, there is no thread id until first message is sent
                if (smsInboxCursor.getString(indexAddress).equals(ContactNumber))
                    ThreadID = smsInboxCursor.getInt(indexThreadID);

            if (smsInboxCursor.getInt(indexThreadID) == ThreadID) {
                if (smsInboxCursor.getInt(indexType) == 1){ //checking type for sent or received
                    messageLine = getIntent().getStringExtra("EXTRA_NAME") + ": " + smsInboxCursor.getString(indexBody);
                    messageLine = smsInboxCursor.getString(indexBody);

                }
                else if (smsInboxCursor.getInt(indexType) == 2){
                    messageLine = "Me: " + smsInboxCursor.getString(indexBody);
                    messageLine = smsInboxCursor.getString(indexBody);

                }
                arrayAdapter.insert(messageLine, 0);

            }
        } while (smsInboxCursor.moveToNext());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_crypt_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getTitle().toString()) {

            case "Caeser":
                final AdapterView.AdapterContextMenuInfo infoCaeser = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                AlertDialog.Builder alertBuilderCaeser = new AlertDialog.Builder(this);
                View alertViewCaeser = getLayoutInflater().inflate(R.layout.dialog_caeser, null);

                final NumberPicker alertNumberPicker = alertViewCaeser.findViewById(R.id.numberPicker);
                alertNumberPicker.setMinValue(1);
                alertNumberPicker.setMaxValue(26);
                alertNumberPicker.setValue(13);
                Button caeserButtonOkay = alertViewCaeser.findViewById(R.id.buttonOkay);
                Button caeserButtonCancel = alertViewCaeser.findViewById(R.id.buttonCancel);

                alertBuilderCaeser.setView(alertViewCaeser);
                final AlertDialog dialogCaeser = alertBuilderCaeser.create();

                caeserButtonOkay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int shiftCaeser = alertNumberPicker.getValue();
                        smsMessagesList.set(infoCaeser.position, Ciphers.caesar(smsMessagesList.get(infoCaeser.position), shiftCaeser));
                        arrayAdapter.notifyDataSetChanged();
                        dialogCaeser.dismiss();
                    }
                });

                caeserButtonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogCaeser.dismiss();
                    }
                });
                dialogCaeser.show();
                return true;

            case "AES":
                final AdapterView.AdapterContextMenuInfo infoAES = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                AlertDialog.Builder alertBuilderAES = new AlertDialog.Builder(this);
                View alertViewAES = getLayoutInflater().inflate(R.layout.dialog_aes, null);
                final EditText aesPassword = alertViewAES.findViewById(R.id.editTextPassword);
                Button aesButtonOkay = alertViewAES.findViewById(R.id.buttonOkay);
                Button aesButtonCancel = alertViewAES.findViewById(R.id.buttonCancel);

                alertBuilderAES.setView(alertViewAES);
                final AlertDialog dialogAES = alertBuilderAES.create();

                aesButtonOkay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String passwordAES = aesPassword.getText().toString();
                        try {
                            smsMessagesList.set(infoAES.position, Ciphers.AESDecrypt(smsMessagesList.get(infoAES.position), passwordAES));
                        } catch (Exception e) {
                            Toast.makeText(ThreadActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        arrayAdapter.notifyDataSetChanged();
                        dialogAES.dismiss();
                    }
                });

                aesButtonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogAES.dismiss();
                    }
                });
                dialogAES.show();
                return true;

            default:
                return false;
        }
    }

    public void onSendClick(View view) {
        Toast.makeText(getApplicationContext(), "Sending message...", Toast.LENGTH_SHORT).show();
        String message = input.getText().toString();
        smsManager.sendTextMessage(ContactNumber, null, message, sentPI  , deliveredPI);

        if (ThreadID == 0) {
            ContentResolver contentResolver = getContentResolver();
            Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/sent"), null, null, null, "date DESC");
            int indexThreadID = smsInboxCursor.getColumnIndex("thread_id");

            int indexAddress = smsInboxCursor.getColumnIndex("address");

            smsInboxCursor.moveToFirst();
            Log.d(TAG, "onSendClick: threadid from cursor: " + smsInboxCursor.getInt(indexThreadID));
            Log.d(TAG, "onSendClick: address from cursor:  " + smsInboxCursor.getString(indexAddress));
            if (ContactNumber.equals(smsInboxCursor.getString(indexAddress))) {
                Log.d(TAG, "onSendClick: assigning new threadID");
                ThreadID = smsInboxCursor.getInt(indexThreadID);
                Log.d(TAG, "onSendClick: ThreadID: " + ThreadID);
            }
        }
        refreshThread();
    }

    public void onCeasarClick(View view){

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        View alertView = getLayoutInflater().inflate(R.layout.dialog_caeser, null);

        final NumberPicker alertNumberPicker = (NumberPicker) alertView.findViewById(R.id.numberPicker);
        alertNumberPicker.setMinValue(1);
        alertNumberPicker.setMaxValue(26);
        alertNumberPicker.setValue(13);
        Button alertButtonOkay = (Button) alertView.findViewById(R.id.buttonOkay);
        Button alertButtonCancel = (Button) alertView.findViewById(R.id.buttonCancel);

        alertBuilder.setView(alertView);
        final AlertDialog dialog = alertBuilder.create();

        alertButtonOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int shift = (int) alertNumberPicker.getValue();
                String msg = input.getText().toString();
                String msgCrypt = Ciphers.caesar(msg, shift);
                input.setText(msgCrypt);
                dialog.dismiss();
            }
        });

        alertButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();


    }

    public void onAESClick(View view){

        AlertDialog.Builder alertBuilderAES = new AlertDialog.Builder(this);
        View alertViewAES = getLayoutInflater().inflate(R.layout.dialog_aes, null);

        final EditText aesPassword = alertViewAES.findViewById(R.id.editTextPassword);
        Button aesButtonOkay = alertViewAES.findViewById(R.id.buttonOkay);
        Button aesButtonCancel = alertViewAES.findViewById(R.id.buttonCancel);

        alertBuilderAES.setView(alertViewAES);
        final AlertDialog dialogAES = alertBuilderAES.create();

        aesButtonOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwordAES = aesPassword.getText().toString();
                String msg = input.getText().toString();
                String msgCrypt = "";


                try {
                    msgCrypt = Ciphers.AESEncrypt(msg, passwordAES);
                } catch (Exception e) {
                    Toast.makeText(ThreadActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                if (!msgCrypt.equals("")){
                    input.setText(msgCrypt);
                }
                dialogAES.dismiss();
            }
        });
        aesButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAES.dismiss();
            }
        });
        dialogAES.show();
    }
}
