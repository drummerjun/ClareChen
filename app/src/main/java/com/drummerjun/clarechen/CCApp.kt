package com.drummerjun.clarechen

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.graphics.Typeface
import android.util.Log
import com.akexorcist.localizationactivity.core.LocalizationApplicationDelegate
import com.bumptech.glide.request.target.ViewTarget
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CCApp : Application() {
    val TAG = CCApp::class.simpleName
    private var storageRef: StorageReference? = null
    private var db: FirebaseFirestore? = null
    private val localizationDelegate = LocalizationApplicationDelegate(this)

//    private val reactNativeHost = object : ReactNativeHost(this) {
//        override fun getUseDeveloperSupport(): Boolean {
//            return BuildConfig.DEBUG
//        }
//
//        override fun getPackages(): List<ReactPackage> {
//            return Arrays.asList(
//                    MainReactPackage(),
//                    RNI18nPackage()
//            )
//        }
//    }
//
//    override fun getReactNativeHost(): ReactNativeHost {
//        return reactNativeHost
//    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        storageRef = FirebaseStorage.getInstance().reference
        db = FirebaseFirestore.getInstance()
        initTypeFace()
        ViewTarget.setTagId(R.id.glide_tag)

        ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { isConnectedToInternet ->
                    if(!isConnectedToInternet) {
                        // do something with isConnectedToInternet value
                    } else {
                        Log.e(TAG, "no internet no internet no internet no internet")
                    }
                }
    }

    override fun getApplicationContext(): Context {
        return localizationDelegate.getApplicationContext(super.getApplicationContext())
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(localizationDelegate.attachBaseContext(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        localizationDelegate.onConfigurationChanged(this)
    }

    companion object {
        lateinit var font: Typeface
        lateinit var instance: CCApp
            private set
    }

    private fun initTypeFace() {
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