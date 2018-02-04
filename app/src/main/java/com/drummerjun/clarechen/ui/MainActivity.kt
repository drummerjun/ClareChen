package com.drummerjun.clarechen.ui

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.drummerjun.clarechen.CCApp
import com.drummerjun.clarechen.Constants
import com.drummerjun.clarechen.R
import com.drummerjun.clarechen.obj.Banner
import com.drummerjun.clarechen.obj.Product
import com.yalantis.guillotine.animation.GuillotineAnimation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.guillotine.*
import java.util.*
import java.util.concurrent.TimeUnit
import com.drummerjun.clarechen.R.id.main_banner



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

        val currentLang = PreferenceManager.getDefaultSharedPreferences(applicationContext).getInt(Constants.KEY_ACTIVE_LANG, -1)
        if(currentLang == -1) {
            val currentTag = Locale.getDefault().toLanguageTag()
            val editor = PreferenceManager.getDefaultSharedPreferences(applicationContext).edit()
            Log.d(TAG, "current language=" + currentTag)
            when (currentTag) {
                Constants.LANG_TW_TAG, Constants.LANG_HK_TAG -> {
                    tw_button.isActivated = true
                    editor.putInt(Constants.KEY_ACTIVE_LANG, Constants.LANG_TW).apply()
                }
                Constants.LANG_CN_TAG, Constants.LANG_SG_TAG -> {
                    cn_button.isActivated = true
                    editor.putInt(Constants.KEY_ACTIVE_LANG, Constants.LANG_CN).apply()
                }
                else ->  {
                    en_button.isActivated = true
                    editor.putInt(Constants.KEY_ACTIVE_LANG, Constants.LANG_EN).apply()
                }
            }
        }

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

        en_button.setOnClickListener {
            PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putInt(Constants.KEY_ACTIVE_LANG, Constants.LANG_EN).apply()
            guillotineAnimation.close()
            en_button.isActivated = true
            tw_button.isActivated = false
            cn_button.isActivated = false
            productlistview.adapter?.notifyDataSetChanged()
        }

        tw_button.setOnClickListener {
            PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putInt(Constants.KEY_ACTIVE_LANG, Constants.LANG_TW).apply()
            guillotineAnimation.close()
            en_button.isActivated = false
            tw_button.isActivated = true
            cn_button.isActivated = false
            productlistview.adapter?.notifyDataSetChanged()
        }

        cn_button.setOnClickListener {
            PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putInt(Constants.KEY_ACTIVE_LANG, Constants.LANG_CN).apply()
            guillotineAnimation.close()
            en_button.isActivated = false
            tw_button.isActivated = false
            cn_button.isActivated = true
            productlistview.adapter?.notifyDataSetChanged()
        }

        staggeredLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        productlistview.layoutManager = staggeredLayoutManager
        retrieveBanner()

        pull_to_refresh.setOnRefreshListener({
            pull_to_refresh.setRefreshing(true)
            banner_container.visibility = View.GONE
            retrieveBanner()
//            retrieveData()
            pull_to_refresh.postDelayed({
                pull_to_refresh.setRefreshing(false)
                banner_container.visibility = View.VISIBLE
            }, TimeUnit.SECONDS.toMillis(3))
        })
    }

    override fun onResume() {
        super.onResume()
        retrieveData()
    }

    private fun retrieveBanner() {
//        banner_container.visibility = View.GONE
        ccapp.getDb().collection(Constants.COLLECTION_BANNER).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val banners = ArrayList<Banner>()
                        task.result.mapTo(banners) { it.toObject(Banner::class.java) }
                        Log.d(TAG, "banners=" + banners)
                        val bannerAdapter = BannerAdapter(main_banner, banners[0].imageUrl, this@MainActivity)
                        main_banner.setAdapter(bannerAdapter)
//                        banner_container.visibility = View.VISIBLE
                        banner_title.text = banners[0].title
                        banner_sub.text = banners[0].subscript
                    } else {
                        Snackbar.make(root, R.string.db_fail, Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.refresh, { retrieveBanner() })
                                .show()
                        Log.e(TAG, "banner fail!!!!")
                    }
//                    retrieveData()
                }
                .addOnFailureListener {
                    Log.e(TAG, "banner addOnFailureListener...........")
                }
    }

    private fun retrieveData() {
        val cate = PreferenceManager.getDefaultSharedPreferences(applicationContext).getInt(Constants.KEY_ACTIVE_CATE, 0)
        pull_to_refresh.setRefreshing(true)
        banner_container.visibility = View.GONE

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
                            Snackbar.make(root, R.string.db_fail, Snackbar.LENGTH_INDEFINITE)
                                    .setAction(R.string.refresh, { retrieveData() })
                                    .show()
                            Log.e(TAG, "db fail!!!!")
                        }
                        pull_to_refresh.setRefreshing(false)
                        banner_container.visibility = View.VISIBLE
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
                            Snackbar.make(root, R.string.db_fail, Snackbar.LENGTH_INDEFINITE)
                                    .setAction(R.string.refresh, { retrieveData() })
                                    .show()
                            Log.e(TAG, "db fail!!!!")
                        }
                        pull_to_refresh.setRefreshing(false)
                        banner_container.visibility = View.VISIBLE
                    }
        }
    }
}
