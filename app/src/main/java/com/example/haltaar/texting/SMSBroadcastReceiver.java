package com.example.haltaar.texting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by Haltaar on 30/01/2018.
 */

public class SMSBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "SMSBroadcastReceiver";

    public void onReceive(Context context, Intent intent) {
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
