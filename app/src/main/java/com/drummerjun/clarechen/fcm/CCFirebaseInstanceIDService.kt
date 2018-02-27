package com.drummerjun.clarechen.fcm

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * Scribbled by drummerjun on 1/13/2018.
 */

class CCFirebaseInstanceIDService : FirebaseInstanceIdService() {
    val TAG = "CCFCM"
    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val token = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "fcm token=" + token)
        registerToken()
    }

    private fun registerToken() {

    }
}
