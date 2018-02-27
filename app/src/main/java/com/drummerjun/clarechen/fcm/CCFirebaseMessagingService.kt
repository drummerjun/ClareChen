package com.drummerjun.clarechen.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Scribbled by drummerjun on 1/13/2018.
 */
class CCFirebaseMessagingService : FirebaseMessagingService() {
    val TAG = "CCFCM";
    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
        Log.d(TAG, "fcm received message=" + p0)
    }
}