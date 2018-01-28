package com.drummerjun.clarechen.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.transition.Slide
import android.util.Log
import android.view.MenuItem
import com.drummerjun.clarechen.Constants
import com.drummerjun.clarechen.GlideApp
import com.drummerjun.clarechen.R
import com.drummerjun.clarechen.obj.Product
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment
import com.yalantis.contextmenu.lib.MenuParams
import kotlinx.android.synthetic.main.activity_layer1.*
import com.yalantis.contextmenu.lib.MenuObject
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.Bitmap
import android.view.Menu
import android.view.View
import android.widget.Toast
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener
import android.view.MenuInflater




/**
 * Created by drummerjun on 20/01/2018.
 */
class ProductLayer1Activity : AppCompatActivity(), OnMenuItemClickListener, OnMenuItemLongClickListener {
    private val TAG = ProductLayer1Activity::class.simpleName
    private lateinit var product: Product
    private lateinit var menuDialogFragment: ContextMenuDialogFragment
    private val cateResId = arrayOf(R.color.colorPrimaryDark, R.color.colorAccent, R.color.colorPrimary)

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val transition = Slide()
            transition.excludeTarget(android.R.id.statusBarBackground, true)
            window.enterTransition = transition
            window.returnTransition = transition
        }
        setContentView(R.layout.activity_layer1)

        ViewCompat.setTransitionName(appbar, Constants.EXTRA_IMAGE)
        supportPostponeEnterTransition()

        setSupportActionBar(toolbar1)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

        initMenuFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.layer1_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.context_menu -> {
                if(supportFragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    menuDialogFragment.show(supportFragmentManager, ContextMenuDialogFragment.TAG)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initMenuFragment() {
        val menuParams = MenuParams()
        menuParams.actionBarSize = resources.getDimension(R.dimen.fab_size).toInt()
        Log.d(TAG, "menuParams.actionBarSize=" + menuParams.actionBarSize)

        menuParams.menuObjects = getMenuObjects()
        menuParams.isClosableOutside = false

        menuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams)
        menuDialogFragment.setItemClickListener(this)
        menuDialogFragment.setItemLongClickListener(this)
    }

    private fun getMenuObjects(): List<MenuObject> {
        val menuObjects = ArrayList<MenuObject>()

        val close = MenuObject()
        close.resource = R.drawable.icn_close

        val send = MenuObject("Send message")
        send.resource = R.drawable.icn_1

        val like = MenuObject("Like profile")
        val b = BitmapFactory.decodeResource(resources, R.drawable.icn_2)
        like.bitmap = b

        val addFr = MenuObject("Add to friends")
        val bd = BitmapDrawable(resources,
                BitmapFactory.decodeResource(resources, R.drawable.icn_3))
        addFr.drawable = bd

        val addFav = MenuObject("Add to favorites")
        addFav.resource = R.drawable.icn_4

        val block = MenuObject("Block user")
        block.resource = R.drawable.icn_5

        menuObjects.add(close)
        menuObjects.add(send)
        menuObjects.add(like)
        menuObjects.add(addFr)
        menuObjects.add(addFav)
        menuObjects.add(block)
        return menuObjects
    }

    override fun onMenuItemClick(clickedView: View?, position: Int) {
        Toast.makeText(this, "Clicked on position: " + position, Toast.LENGTH_SHORT).show();
    }

    override fun onMenuItemLongClick(clickedView: View?, position: Int) {
        Toast.makeText(this, "Long clicked on position: " + position, Toast.LENGTH_SHORT).show();
    }
}