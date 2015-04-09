package com.aberigle.android.sliderview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by aberigle on 06/04/15.
 */
public class SlidingTabStrip extends LinearLayout {
    private static final float SELECTED_INDICATOR_THICKNESS_DIPS = 1;

    private final Paint paint;
    private final int borderThickness;

    private int currentPosition;
    private float positionOffset;


    public SlidingTabStrip(Context context, int borderColor) {
        super(context);
        setWillNotDraw(false);

        paint = new Paint();
        paint.setColor(borderColor);

        float density = getResources().getDisplayMetrics().density;
        borderThickness = (int) (SELECTED_INDICATOR_THICKNESS_DIPS * density);

        setOrientation(HORIZONTAL);
    }

    public void onViewPagerChanged(int position, float offset) {
        currentPosition = position;
        positionOffset = offset;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height     = getHeight();
        int childCount = getChildCount();

        // draw an animated line under the items
        if (childCount > 0) {
            View selected = getChildAt(currentPosition);

            int left  = selected.getLeft();
            int right = selected.getRight();

            // the line is betweet two tabs
            if (positionOffset > 0 && currentPosition < (childCount - 1)) {
                View nextTitle = getChildAt(currentPosition + 1);
                left  = (int) (positionOffset * nextTitle.getLeft()  + (1 - positionOffset) * left);
                right = (int) (positionOffset * nextTitle.getRight() + (1 - positionOffset) * right);
            }

            canvas.drawRect(left, height - borderThickness, right, height, paint);
        }
    }
}
