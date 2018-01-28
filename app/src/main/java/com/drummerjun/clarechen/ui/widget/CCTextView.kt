package com.drummerjun.clarechen.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.drummerjun.clarechen.CCApp

/**
 * Created by drummerjun on 16/01/2018.
 */
class CCTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : TextView(context, attrs, defStyleAttr) {
    init {
        typeface = CCApp.font
    }

}