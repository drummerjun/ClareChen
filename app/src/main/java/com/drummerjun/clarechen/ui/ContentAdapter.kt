package com.drummerjun.clarechen.ui

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.drummerjun.clarechen.Constants
import com.drummerjun.clarechen.model.Product

class ContentAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: ArrayList<ViewType>
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()

    init {
        delegateAdapters.put(Constants.VIEWTYPE_IMAGE, ImageDelegateAdapter())
        items = ArrayList()
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = delegateAdapters.get(viewType).onCreateViewHolder(parent)

    override fun getItemViewType(position: Int) = items[position].getViewType()

    fun addContent(product: Product) {

    }
}