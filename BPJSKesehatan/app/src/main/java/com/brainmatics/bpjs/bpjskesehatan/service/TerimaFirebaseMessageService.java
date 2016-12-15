package com.brainmatics.bpjs.bpjskesehatan.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class TerimaFirebaseMessageService extends FirebaseMessagingService {

    private static final String TAG = "FCMMessage";

    @Override
    public void onMessageReceived(RemoteMessage msg) {
        Log.d(TAG, "FCM Message From : "+msg.getFrom());
        Log.d(TAG, "FCM Message To : "+msg.getTo());
        Log.d(TAG, "FCM Message Content : "+msg.getData());
    }
}
