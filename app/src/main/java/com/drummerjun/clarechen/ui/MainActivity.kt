package com.drummerjun.clarechen.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.drummerjun.clarechen.CCApp
import com.drummerjun.clarechen.Constants
import com.drummerjun.clarechen.R
import com.drummerjun.clarechen.obj.Product
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.guillotine.*
import com.yalantis.guillotine.animation.GuillotineAnimation

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.simpleName
    private lateinit var staggeredLayoutManager: StaggeredGridLayoutManager
    private val ccapp = CCApp.instance
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(toolbar != null) {
            setSupportActionBar(toolbar)
            supportActionBar?.title = null
        }

        val guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null)
        root.addView(guillotineMenu)

        GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), content_hamburger)
                .setStartDelay(250)
                .setActionBarViewForAnimation(toolbar as View)
                .setClosedOnStart(true)
                .build()

        group_all.setOnClickListener { Toast.makeText(this, "all", Toast.LENGTH_SHORT).show() }
        group_boys.setOnClickListener { Toast.makeText(this, "boys", Toast.LENGTH_SHORT).show() }
        group_girls.setOnClickListener { Toast.makeText(this, "girls", Toast.LENGTH_SHORT).show() }

        staggeredLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        productlistview.layoutManager = staggeredLayoutManager

        ccapp.getDb().collection(Constants.COLLECTION_PRODUCT).get()
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        Log.d(TAG, "success! task=" + task.result)
                        val products = ArrayList<Product>()
                        task.result.mapTo(products) { it.toObject(Product::class.java) }
                        adapter = ProductAdapter(products)
                        productlistview.adapter = adapter
                    } else {
                        Log.e(TAG, "db fail!!!!")
                    }
                }
    }
}
