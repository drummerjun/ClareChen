package com.drummerjun.clarechen.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

/**
 * Scribbled by drummerjun on 20/01/2018.
 */
class SquareImageView : ImageView {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec)
    }
}