package com.example.haltaar.texting;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by Haltaar on 30/01/2018.
 */

public class SMSBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "SMSBroadcastReceiver";
    private int notificationID = 0;


    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();
        String notiString = "New Message!";

        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get("pdus");
            notiString = "";
            for (int i = 0; i < sms.length; ++i) {
                String format = intentExtras.getString("format");
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i], format);
                String number = smsMessage.getOriginatingAddress();
                notiString += "New message from: " + number + "\n";
            }
        }
//        Toast.makeText(context, "Message Received!", Toast.LENGTH_SHORT).show();


        String channel_ID = "CipherText";

        Intent threadIntent = new Intent(context, MainActivity.class);
        threadIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, threadIntent, 0);

        NotificationCompat.Builder newMesageNoti = new NotificationCompat.Builder(context, channel_ID)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_round))
                .setContentTitle("CipherText")
                .setContentText(notiString)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "CipherText";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channel_ID, name, importance);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(notificationID, newMesageNoti.build());
        notificationID++;


        if (MainActivity.active) {
                MainActivity inst = MainActivity.instance();
                inst.refreshConvos();
            } else if (ThreadActivity.active) {
                ThreadActivity inst = ThreadActivity.instance();
                inst.refreshThread();
            }
        }
    }
