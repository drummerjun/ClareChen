package com.drummerjun.clarechen.ui

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.drummerjun.clarechen.Constants
import com.drummerjun.clarechen.R
import com.drummerjun.clarechen.activities.WebAcivity
import com.drummerjun.clarechen.obj.Product
import kotlinx.android.synthetic.main.item_product.view.*

/**
 * Created by drummerjun on 1/13/2018.
 */
class ProductAdapter (private val products: ArrayList<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        var inflated = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(inflated)
    }

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bindItems(products[position])
    }

    class ProductViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var product: Product? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val context = itemView.context
            val showPhotoIntent = Intent(context, WebAcivity::class.java)
            showPhotoIntent.putExtra(Constants.KEY_PRODUCT_LINK, product!!.image[0])
            context.startActivity(showPhotoIntent)
        }

        fun bindItems(product: Product) {
            Glide.with(itemView.context).load(product.image[0]).into(itemView.product_image)
            itemView.product_name.text = product.name[0]
            itemView.product_price.text = product.price[0]
        }
    }
}

