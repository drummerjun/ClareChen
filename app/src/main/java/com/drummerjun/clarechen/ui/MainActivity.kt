package com.drummerjun.clarechen.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.widget.SwipeRefreshLayout
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

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private val TAG = MainActivity::class.simpleName
    private lateinit var staggeredLayoutManager: StaggeredGridLayoutManager
    private val ccapp = CCApp.instance
    private lateinit var adapter: ProductAdapter
    private lateinit var guillotineAnimation: GuillotineAnimation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(toolbar != null) {
            setSupportActionBar(toolbar)
            supportActionBar?.title = null
        }

        val guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null)
        root.addView(guillotineMenu)

        guillotineAnimation = GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), content_hamburger)
                .setStartDelay(250)
                .setActionBarViewForAnimation(toolbar as View)
                .setClosedOnStart(true)
                .build()

        group_all.setOnClickListener {
            PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putInt(Constants.KEY_ACTIVE_CATE, 0).apply()
            guillotineAnimation.close()
//            adapter.filterByCategory(0)
//            productlistview.adapter.notifyDataSetChanged()
            retrieveData()
        }

        group_girls.setOnClickListener {
            PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putInt(Constants.KEY_ACTIVE_CATE, 1).apply()
            guillotineAnimation.close()
//            adapter.filterByCategory(1)
//            productlistview.adapter.notifyDataSetChanged()
            retrieveData()
        }

        group_boys.setOnClickListener {
            PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putInt(Constants.KEY_ACTIVE_CATE, 2).apply()
            guillotineAnimation.close()
//            adapter.filterByCategory(2)
//            productlistview.adapter.notifyDataSetChanged()
            retrieveData()
        }

        staggeredLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        productlistview.layoutManager = staggeredLayoutManager

        swipe_container.setOnRefreshListener(this)
        swipe_container.setColorSchemeResources(
                R.color.colorPrimary,
                android.R.color.white,
                R.color.colorAccent,
                R.color.colorPrimaryDark
        )
        swipe_container.post({
            retrieveData()
        })
    }

    override fun onResume() {
        super.onResume()
//        var currentCate = PreferenceManager.getDefaultSharedPreferences(applicationContext).getInt(Constants.KEY_ACTIVE_CATE, 0)
        retrieveData()
    }

    override fun onRefresh() {
        retrieveData()
    }

    private fun retrieveData() {
        val cate = PreferenceManager.getDefaultSharedPreferences(applicationContext).getInt(Constants.KEY_ACTIVE_CATE, 0)
        swipe_container?.isRefreshing = true

        if(cate == 0) {
            ccapp.getDb().collection(Constants.COLLECTION_PRODUCT).get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "success! task=" + task.result)
                            val products = ArrayList<Product>()
                            task.result.mapTo(products) { it.toObject(Product::class.java) }
                            if (productlistview.adapter == null) {
                                adapter = ProductAdapter(products)
                                productlistview.adapter = adapter
                            } else {
                                adapter.overrideData(products)
                                productlistview.adapter.notifyDataSetChanged()
                            }
                        } else {
                            Log.e(TAG, "db fail!!!!")
                        }
                        swipe_container?.isRefreshing = false
                    }
        } else {
            ccapp.getDb().collection(Constants.COLLECTION_PRODUCT)
                    .whereEqualTo("cate", cate)
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "success! task=" + task.result)
                            val products = ArrayList<Product>()
                            task.result.mapTo(products) { it.toObject(Product::class.java) }
                            if (productlistview.adapter == null) {
                                adapter = ProductAdapter(products)
                                productlistview.adapter = adapter
                            } else {
                                adapter.overrideData(products)
                                productlistview.adapter.notifyDataSetChanged()
                            }
                        } else {
                            Log.e(TAG, "db fail!!!!")
                        }
                        swipe_container?.isRefreshing = false
                    }
        }
    }
}