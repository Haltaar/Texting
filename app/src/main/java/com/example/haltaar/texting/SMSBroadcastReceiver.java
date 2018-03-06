package com.example.haltaar.texting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Haltaar on 30/01/2018.
 */

public class SMSBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "SMSBroadcastReceiver";


    public static final String SMS_BUNDLE = "pdus";

    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();

        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            for (int i = 0; i < sms.length; ++i) {
                String format = intentExtras.getString("format");
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i], format);

                String smsBody = smsMessage.getMessageBody().toString();
                String address = smsMessage.getOriginatingAddress();

                smsMessageStr += "SMS From: " + address + "\n";
                smsMessageStr += smsBody + "\n";
            }

            Toast.makeText(context, "Message Received!", Toast.LENGTH_SHORT).show();

            if (MainActivity.active) {
                MainActivity inst = MainActivity.instance();
                inst.refreshConvos();
            } else if (ThreadActivity.active) {
                ThreadActivity inst = ThreadActivity.instance();
                inst.refreshThread();
            }
        }
    }
}
