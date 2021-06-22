package com.coen268.moviemate;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

public class GetDeviceToken extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        String device_token = FirebaseInstanceId.getInstance().getToken();
        Log.d("Device Token: ", device_token);
    }
}
