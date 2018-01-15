package com.drummerjun.clarechen.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import com.drummerjun.clarechen.CCApp
import com.drummerjun.clarechen.Constants
import com.drummerjun.clarechen.R
import com.drummerjun.clarechen.adapters.ProductAdapter
import com.drummerjun.clarechen.obj.Product
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.simpleName
    private lateinit var gridviewLayoutManager: GridLayoutManager
    private val ccapp = CCApp.instance
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridviewLayoutManager = GridLayoutManager(this, 2)
        productlistview.layoutManager = gridviewLayoutManager

        ccapp.getDb()?.collection(Constants.COLLECTION_PRODUCT)?.get()
                ?.addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        Log.d(TAG, "success! task=" + task.result)
                        var products: ArrayList<Product>
                        task.result.mapTo() {

                        }
                        for (document in task.result) {
                        }
                        productlistview.adapter = adapter
                    } else {
                        Log.e(TAG, "db fail!!!!")
                    }
                }
    }
}
