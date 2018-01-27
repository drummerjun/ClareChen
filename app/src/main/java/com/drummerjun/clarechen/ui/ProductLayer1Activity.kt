package com.drummerjun.clarechen.ui

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Build
import android.support.v4.view.ViewCompat
import android.support.v7.app.ActionBar

import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import com.drummerjun.clarechen.R
import android.transition.Slide
import android.util.Log
import com.drummerjun.clarechen.Constants
import com.drummerjun.clarechen.GlideApp
import com.drummerjun.clarechen.obj.Product
import kotlinx.android.synthetic.main.activity_layer1.*
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.target.Target


/**
 * Created by ehs_app_1 on 20/01/2018.
 */
class ProductLayer1Activity : AppCompatActivity() {
    private val TAG = ProductLayer1Activity::class.simpleName
    private lateinit var product: Product
    private val cateResId = arrayOf(R.color.colorPrimaryDark, R.color.colorAccent, R.color.colorPrimary)

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            var transition = Slide()
            transition.excludeTarget(android.R.id.statusBarBackground, true)
            window.enterTransition = transition
            window.returnTransition = transition
        }
        setContentView(R.layout.activity_layer1)

        ViewCompat.setTransitionName(appbar, Constants.EXTRA_IMAGE)
        supportPostponeEnterTransition()

        setSupportActionBar(toolbar1)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        if(intent.hasExtra("EXTRA_OBJ")) {
            product = intent.getParcelableExtra("EXTRA_OBJ")
            Log.d(TAG, "product=" + product.toString())

            collapsing_bar.title = product.name[1]
            collapsing_bar.setExpandedTitleColor(resources.getColor(cateResId[product.cate]))
            collapsing_bar.setContentScrimColor(resources.getColor(cateResId[product.cate]))
            collapsing_bar.setStatusBarScrimColor(resources.getColor(cateResId[product.cate]))
            GlideApp.with(this)
                    .load(product.image[0])
//                    .listener(object : RequestListener<Drawable> {
//                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
//                            Log.e(TAG, "Product Image load fail:::" + product.image[1])
//                            return true
//                        }
//
//                        override fun onResourceReady(resource: Drawable?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
//                            val bitmap = (resource as BitmapDrawable).bitmap
//                            Palette.from(bitmap).generate { palette ->
//                                collapsing_bar.setContentScrimColor(palette.getMutedColor(resources.getColor(R.color.colorPrimary)))
//                                collapsing_bar.setStatusBarScrimColor(palette.getDarkMutedColor(resources.getColor(R.color.colorPrimary)))
//
//                                fab1.rippleColor = palette.getLightVibrantColor(resources.getColor(android.R.color.white))
//                                fab1.backgroundTintList = ColorStateList.valueOf(palette.getVibrantColor(resources.getColor(R.color.colorAccent)))
//
//                                supportStartPostponedEnterTransition()
//                            }
//                            return true
//                        }
//                    })
                    .into(sqimage)

            title1.text = product.description
            details1.text = product.price[0]
        }
    }


}