package com.drummerjun.clarechen

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import android.graphics.Typeface

/**
 * Created by drummerjun on 1/13/2018.
 */
class CCApp : Application() {
    val TAG = CCApp::class.simpleName
    private var storageRef: StorageReference? = null
    private var db: FirebaseFirestore? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        storageRef = FirebaseStorage.getInstance().reference
        db = FirebaseFirestore.getInstance()
        initTypeFace()
    }

    companion object {
        lateinit var font: Typeface
        lateinit var instance: CCApp
            private set
    }

    fun initTypeFace() {
        font = Typeface.createFromAsset(assets, "fonts/cc_font.otf")
    }

    fun getStorage() = storageRef

    fun getDb(): FirebaseFirestore {
        if(db == null) {
            db = FirebaseFirestore.getInstance()
        }
        return db as FirebaseFirestore
    }
}