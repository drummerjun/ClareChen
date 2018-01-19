package com.drummerjun.clarechen.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ehs_app_1 on 18/01/2018.
 */

public class FourThreeView extends View {

    public FourThreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int fourThreeHeight = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthSpec) * 3 / 4,
                MeasureSpec.EXACTLY);
        super.onMeasure(widthSpec, fourThreeHeight);
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }
}
