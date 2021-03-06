package com.drummerjun.clarechen.ui

import android.content.Intent
import android.preference.PreferenceManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.drummerjun.clarechen.Constants
import com.drummerjun.clarechen.GlideApp
import com.drummerjun.clarechen.R
import com.drummerjun.clarechen.model.Product
import kotlinx.android.synthetic.main.item_product.view.*

class ProductAdapter (private val products: ArrayList<Product>) :
        RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    val TAG = ProductAdapter::class.simpleName
    var filtered: ArrayList<Product> = products.clone() as ArrayList<Product>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflated = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(inflated).listen { position, _ ->
            val selected = products[position]
//            val intent1 = Intent(parent.context, ProductDetailActivity::class.java)
            val intent1 = Intent(parent.context, ProductLayer1Activity::class.java)
            Log.d("ProductAdapter", "parcelable product=" + selected)
            intent1.putExtra("EXTRA_OBJ", selected)
            parent.context.startActivity(intent1)
        }
    }

    override fun getItemCount() = filtered.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(filtered[position])
    }

    fun overrideData(data: ArrayList<Product>) {
        products.clear()
        products.addAll(data)
        filtered.clear()
        filtered.addAll(data)
    }

    class ProductViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        fun bind(product: Product) {
            val activeLang = PreferenceManager
                    .getDefaultSharedPreferences(itemView.context.applicationContext)
                    .getInt(Constants.KEY_ACTIVE_LANG, Constants.LANG_EN)
            GlideApp.with(itemView.context).load(product.image[0]).into(itemView.product_image)
            itemView.product_name.text = product.name[activeLang]
            itemView.product_price.text = product.price[0]
        }
    }

    private fun <T: RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
        itemView.setOnClickListener{
            event.invoke(adapterPosition, itemViewType)
        }
        return this
    }
}

