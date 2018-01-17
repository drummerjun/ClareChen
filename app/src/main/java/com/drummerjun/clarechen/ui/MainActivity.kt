package com.drummerjun.clarechen.ui

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.drummerjun.clarechen.CCApp
import com.drummerjun.clarechen.Constants
import com.drummerjun.clarechen.R
import com.drummerjun.clarechen.obj.Product
import com.yalantis.guillotine.animation.GuillotineAnimation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.guillotine.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
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
            retrieveData()
        }

        group_girls.setOnClickListener {
            PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putInt(Constants.KEY_ACTIVE_CATE, 1).apply()
            guillotineAnimation.close()
            retrieveData()
        }

        group_boys.setOnClickListener {
            PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putInt(Constants.KEY_ACTIVE_CATE, 2).apply()
            guillotineAnimation.close()
            retrieveData()
        }

        staggeredLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        productlistview.layoutManager = staggeredLayoutManager

        pull_to_refresh.setOnRefreshListener({
            pull_to_refresh.setRefreshing(true)
            retrieveData()
            pull_to_refresh.postDelayed({
                pull_to_refresh.setRefreshing(false)
            }, TimeUnit.SECONDS.toMillis(3))
        })
    }

    override fun onResume() {
        super.onResume()
        retrieveData()
    }

    private fun retrieveData() {
        val cate = PreferenceManager.getDefaultSharedPreferences(applicationContext).getInt(Constants.KEY_ACTIVE_CATE, 0)
        pull_to_refresh.setRefreshing(true)

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
                        pull_to_refresh.setRefreshing(false)
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
                        pull_to_refresh.setRefreshing(false)

                    }
        }
    }
}

