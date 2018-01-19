package com.drummerjun.clarechen.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.OnScrollListener
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.drummerjun.clarechen.GlideApp
import com.drummerjun.clarechen.R
import com.drummerjun.clarechen.obj.Product
import com.drummerjun.clarechen.ui.widget.BaselineGridTextView
import com.drummerjun.clarechen.ui.widget.ElasticDragDismissFrameLayout
import com.drummerjun.clarechen.ui.widget.FabOverlapTextView
import com.drummerjun.clarechen.ui.widget.FourThreeView
import kotlinx.android.synthetic.main.product_description.*
import kotlinx.android.synthetic.main.product_title.*
import kotlinx.android.synthetic.main.activity_product_detail.*

/**
 * Created by drummerjun on 17/01/2018.
 */
class ProductDetailActivity: AppCompatActivity() {
    private val TAG = ProductDetailActivity::class.simpleName
    private lateinit var description: View
    private lateinit var chromeFader: ElasticDragDismissFrameLayout.SystemChromeFader
    private var product: Product? = null
    private var fabOffset = 0
    private lateinit var shotTitle: View
    private lateinit var shotSpacer: FourThreeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        description = layoutInflater.inflate(R.layout.product_description, dribbble_comments, false)
        back.setOnClickListener({setResultAndFinish()})
        chromeFader = object : ElasticDragDismissFrameLayout.SystemChromeFader(this) {
            override fun onDragDismissed() {
                setResultAndFinish()
            }
        }

        if(intent.hasExtra("EXTRA_OBJ")) {
            product = intent.getParcelableExtra("EXTRA_OBJ")
            Log.d(TAG, "product=" + product.toString())
            shotTitle = description.findViewById(R.id.shot_title)
//            shotTitle.text = product!!.name[0]
            Log.d(TAG, "shotTitle=" + shotTitle)
            Log.d(TAG, "shot_title=" + shot_title)
            shotSpacer = description.findViewById(R.id.shot_spacer)
            bindProduct(true)
        }
    }

    override fun onResume() {
        super.onResume()
        draggable_frame.addListener(chromeFader)
    }

    override fun onPause() {
        draggable_frame.removeListener(chromeFader)
        super.onPause()
    }

    override fun onBackPressed() {
        setResultAndFinish()
    }

    private fun setResultAndFinish() {
        var resultData = Intent()
        resultData.putExtra("RESULT_EXTRA_SHOT_ID", resultData)
        setResult(Activity.RESULT_OK)
        finishAfterTransition()
    }

    private fun bindProduct(postponeEntry: Boolean) {
        GlideApp.with(this)
                .load(product!!.image[0])
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .priority(Priority.IMMEDIATE)
                .transition(withCrossFade())
                .into(shot)

        val bgResId = intArrayOf(R.color.colorPrimaryDark, R.color.colorAccent, R.color.colorPrimary)
        shot.setBackgroundResource(bgResId[product!!.cate])
        Log.d(TAG, "product name=" + product!!.name[0])
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (shotTitle as FabOverlapTextView).setText(product!!.name[0])
        } else {
            (shotTitle as TextView).text = product!!.name[0]
        }

        if(postponeEntry) postponeEnterTransition()
        shot.viewTreeObserver.addOnPreDrawListener(
                object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        shot.viewTreeObserver.removeOnPreDrawListener(this)
                        calculateFabPosition()
                        if (postponeEntry) startPostponedEnterTransition()
                        return true
                    }
                })
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            val scrollY = shot_description.top
            shot.offset = scrollY
            fab_heart.setOffset(fabOffset + scrollY)
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            // as we animate the main image's elevation change when it 'pins' at it's min height
            // a fling can cause the title to go over the image before the animation has a chance to
            // run. In this case we short circuit the animation and just jump to state.
            shot.isImmediatePin = newState == RecyclerView.SCROLL_STATE_SETTLING
        }
    }

    private val flingListener = object : RecyclerView.OnFlingListener() {
        override fun onFling(velocityX: Int, velocityY: Int): Boolean {
            shot.isImmediatePin = true
            return false
        }
    }

    internal fun calculateFabPosition() {
        // calculate 'natural' position i.e. with full height image. Store it for use when scrolling
        fabOffset = shot.height + shotTitle.height - fab_heart.height / 2
        fab_heart.setOffset(fabOffset)

        // calculate min position i.e. pinned to the collapsed image when scrolled
        fab_heart.setMinOffset(shot.minimumHeight - fab_heart.height / 2)
    }
}