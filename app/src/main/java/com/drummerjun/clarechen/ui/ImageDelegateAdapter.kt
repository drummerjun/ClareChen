package com.drummerjun.clarechen.ui

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.drummerjun.clarechen.R
import com.drummerjun.clarechen.model.Product
import kotlinx.android.synthetic.main.image_content.*

class ImageDelegateAdapter: ViewTypeDelegateAdapter {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = ImageViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as ImageViewHolder
        holder.bind(item as Product)
    }

    inner class ImageViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
            View.inflate(parent.context, R.layout.image_content, parent)) {
        fun bind(item: Product) {
        }
    }
}