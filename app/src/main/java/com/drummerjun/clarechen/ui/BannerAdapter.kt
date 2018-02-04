package com.drummerjun.clarechen.ui

import android.content.Context
import android.view.View
import com.jude.rollviewpager.adapter.LoopPagerAdapter
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.drummerjun.clarechen.GlideApp
import com.drummerjun.clarechen.obj.Banner
import com.jude.rollviewpager.RollPagerView

/**
 * Created by drummerjun on 2/4/2018.
 */
class BannerAdapter(viewPager: RollPagerView, private val images: List<String>, val context: Context) : LoopPagerAdapter(viewPager) {
    override fun getView(container: ViewGroup, position: Int): View {
        val view = ImageView(container.context)
        view.scaleType = ImageView.ScaleType.CENTER_CROP
//        view.adjustViewBounds = true
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        GlideApp.with(context).load(images[position])
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(view)
//        view.set(images.get(position))
        return view
    }

    override fun getRealCount(): Int {
        return images.size
    }
}