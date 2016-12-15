package com.brainmatics.bpjs.bpjskesehatan.service;


import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.brainmatics.bpjs.bpjskesehatan.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class TerimaFirebaseMessageService extends FirebaseMessagingService {

    private static final String TAG = "FCMMessage";

    @Override
    public void onMessageReceived(RemoteMessage msg) {
        Log.d(TAG, "FCM Message From : "+msg.getFrom());
        Log.d(TAG, "FCM Message To : "+msg.getTo());
        Log.d(TAG, "FCM Message Content : "+msg.getData());

        Map<String, String> data = msg.getData();

        // tampilkan notifikasi
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo_only)
                        .setContentTitle("Tagihan Baru")
                        .setContentText("Ada tagihan baru untuk peserta "
                        +data.get("nama"));

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 100;
        mNotificationManager.notify(notificationId, mBuilder.build());
    }
}
