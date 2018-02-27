package com.drummerjun.clarechen.ui

import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.drummerjun.clarechen.CCApp
import com.drummerjun.clarechen.Constants
import com.drummerjun.clarechen.R
import com.drummerjun.clarechen.model.Banner
import com.drummerjun.clarechen.model.Product
import com.yalantis.guillotine.animation.GuillotineAnimation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.guillotine.*
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : LocalizationActivity() {
    private val TAG = MainActivity::class.simpleName
    private val ccapp = CCApp.instance
    private lateinit var staggeredLayoutManager: StaggeredGridLayoutManager
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
            val currentTag = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                resources.configuration.locales[0] //Locale.getDefault().toLanguageTag()
            } else {
                resources.configuration.locale//.toLanguageTag()
            }
            val editor = PreferenceManager.getDefaultSharedPreferences(applicationContext).edit()
            Log.d(TAG, "current language=" + currentTag)
            when (currentTag) {
                Locale.TRADITIONAL_CHINESE -> {
//                Constants.LANG_TW_TAG, Constants.LANG_HK_TAG -> {
                    tw_button.isActivated = true
                    editor.putInt(Constants.KEY_ACTIVE_LANG, Constants.LANG_TW).apply()
                }
                Locale.SIMPLIFIED_CHINESE -> {
//                Constants.LANG_CN_TAG, Constants.LANG_SG_TAG -> {
                    cn_button.isActivated = true
                    editor.putInt(Constants.KEY_ACTIVE_LANG, Constants.LANG_CN).apply()
                }
                else ->  {
                    en_button.isActivated = true
                    editor.putInt(Constants.KEY_ACTIVE_LANG, Constants.LANG_EN).apply()
                }
            }
        } else {
            activateLang(currentLang)
        }

        guillotineAnimation = GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), content_hamburger)
                .setStartDelay(250)
                .setActionBarViewForAnimation(toolbar as View)
                .setClosedOnStart(true)
                .build()

        activateCate(PreferenceManager.getDefaultSharedPreferences(applicationContext).getInt(Constants.KEY_ACTIVE_CATE, Constants.CATE_ALL))

        group_all.setOnClickListener {
            activateCate(Constants.CATE_ALL)
            PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putInt(Constants.KEY_ACTIVE_CATE, 0).apply()
            guillotineAnimation.close()
            retrieveData()
        }

        group_girls.setOnClickListener {
            activateCate(Constants.CATE_GIRLS)
            PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putInt(Constants.KEY_ACTIVE_CATE, 1).apply()
            guillotineAnimation.close()
            retrieveData()
        }

        group_boys.setOnClickListener {
            activateCate(Constants.CATE_BOYS)
            PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putInt(Constants.KEY_ACTIVE_CATE, 2).apply()
            guillotineAnimation.close()
            retrieveData()
        }

        en_button.setOnClickListener {
            PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putInt(Constants.KEY_ACTIVE_LANG, Constants.LANG_EN).apply()
            setDefaultLanguage(Locale.ENGLISH)
            guillotineAnimation.close()
            activateLang(Constants.LANG_EN)
            productlistview.adapter?.notifyDataSetChanged()
            setLanguage(Locale.ENGLISH)
        }

        tw_button.setOnClickListener {
            PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putInt(Constants.KEY_ACTIVE_LANG, Constants.LANG_TW).apply()
            setDefaultLanguage(Locale.TRADITIONAL_CHINESE)
            guillotineAnimation.close()
            activateLang(Constants.LANG_TW)
            productlistview.adapter?.notifyDataSetChanged()
            setLanguage(Locale.TRADITIONAL_CHINESE)
        }

        cn_button.setOnClickListener {
            PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putInt(Constants.KEY_ACTIVE_LANG, Constants.LANG_CN).apply()
            setDefaultLanguage(Locale.SIMPLIFIED_CHINESE)
            guillotineAnimation.close()
            activateLang(Constants.LANG_CN)
            productlistview.adapter?.notifyDataSetChanged()
            setLanguage(Locale.SIMPLIFIED_CHINESE)
        }

        staggeredLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        productlistview.layoutManager = staggeredLayoutManager
        retrieveBanner()

        pull_to_refresh.setOnRefreshListener({
            pull_to_refresh.setRefreshing(true)
//            banner_container.visibility = View.GONE
            retrieveBanner()
//            retrieveData()
            pull_to_refresh.postDelayed({
                pull_to_refresh.setRefreshing(false)
//                banner_container.visibility = View.VISIBLE
            }, TimeUnit.SECONDS.toMillis(3))
        })
    }

    override fun onResume() {
        super.onResume()
        retrieveData()
    }

    private fun retrieveBanner() {
        ccapp.getDb().collection(Constants.COLLECTION_BANNER).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val banners = ArrayList<Banner>()
                        task.result.mapTo(banners) { it.toObject(Banner::class.java) }
                        Log.d(TAG, "banners=" + banners)
                        val bannerAdapter = BannerAdapter(main_banner, banners[0].imageUrl, this@MainActivity)
                        main_banner.setAdapter(bannerAdapter)
                        banner_title.text = banners[0].title
                        banner_sub.text = banners[0].subscript
                    } else {
                        Snackbar.make(root, R.string.db_fail, Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.refresh, { retrieveBanner() })
                                .show()
                        Log.e(TAG, "banner fail!!!!")
                    }
                }
                .addOnFailureListener {
                    Log.e(TAG, "banner addOnFailureListener...........")
                }
    }

    private fun retrieveData() {
        val cate = PreferenceManager.getDefaultSharedPreferences(applicationContext).getInt(Constants.KEY_ACTIVE_CATE, Constants.CATE_ALL)
        pull_to_refresh.setRefreshing(true)
//        banner_container.visibility = View.GONE

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
                    }
        }
    }

    private fun activateCate(selected: Int) {
        val cates = listOf(group_all, group_girls, group_boys)
        for(c in cates) c.isActivated = false
        cates[selected].isActivated = true
    }

    private fun activateLang(selected: Int) {
        val languages = listOf(en_button, tw_button, cn_button)
        for(l in languages) l.isActivated = false
        languages[selected].isActivated = true
    }
}
