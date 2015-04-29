package com.aberigle.android.sliderview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

/**
 * Created by aberigle on 06/04/15.
 */
public class SlidingTabLayout extends HorizontalScrollView{

    private static final float TAB_VIEW_PADDING_DIPS = 16;

    private final SlidingTabStrip strip;

    private int displayWidth;
    private int displayHeight;

    private int selectedOffset;

    private int backgroundColor;
    private final int selectedTextColor;
    private int textColor;

    private ViewPager viewpager;


    public SlidingTabLayout(Context context) { this(context, null); }

    public SlidingTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalScrollBarEnabled(false);
        setFillViewport(true);

        backgroundColor = getBackgroundColor();
        textColor = getContext().getResources().getColor(android.R.color.secondary_text_dark);
        selectedTextColor = getContext().getResources().getColor(android.R.color.primary_text_dark);
        setBackgroundColor(backgroundColor);

        strip = new SlidingTabStrip(getContext(), Color.WHITE);

        addView(strip);

    }

    private void populateFromPager() {
        PagerAdapter adapter = viewpager.getAdapter();
        int padding = (int) (TAB_VIEW_PADDING_DIPS * getResources().getDisplayMetrics().density);

        TextView tab;

        for (int i = 0; i < adapter.getCount(); i++) {
            tab = new TextView(getContext());
            final int index = i;
            tab.setGravity(Gravity.CENTER);
            tab.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            tab.setTypeface(Typeface.DEFAULT_BOLD);
            tab.setTextColor(textColor);
            tab.setAllCaps(true);
            tab.setText(adapter.getPageTitle(i));
            tab.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewpager.setCurrentItem(index);
                }
            });
            tab.setPadding(padding,padding,padding,padding);
            strip.addView(tab);
        }

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (viewpager != null)
            scrollToTab(viewpager.getCurrentItem(), 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        displayWidth = MeasureSpec.getSize(widthMeasureSpec);
        displayHeight = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int resolveAttr(int attrId) {
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(attrId, outValue, true);
        return outValue.data;
    }

    private int getTextColor() {
        return resolveAttr(android.R.attr.textColorPrimaryInverse);
    }

    private void scrollToTab(int index, float positionOffset) {
        final int childCount = strip.getChildCount();
        if (childCount == 0 || index < 0 || index >= childCount) {
            return;
        }

        View selectedChild = strip.getChildAt(index);
        if (selectedChild != null) {
            int left = selectedChild.getLeft();

            if (index < (childCount - 1)) {
                View nextTitle = strip.getChildAt(index + 1);
                left = (int) (positionOffset * nextTitle.getLeft() + (1.0f - positionOffset) * left);
            }

            int targetScrollX = left + (selectedChild.getWidth() / 2) - (getWidth() / 2);
            scrollTo(targetScrollX, 0);
        }
    }

    private int getBackgroundColor() {
        int attrId;
        if (Build.VERSION.SDK_INT >= 21)
            attrId = android.R.attr.colorPrimary;
        else
            attrId = android.R.attr.colorBackground;
        return resolveAttr(attrId);
    }

    public void setViewpager(ViewPager pager) {
        viewpager = pager;

        if (pager != null) {
            pager.setOnPageChangeListener(new PagerChangeListener());
        }
        populateFromPager();
    }

    private class PagerChangeListener implements ViewPager.OnPageChangeListener {

        private TextView oldPos;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            strip.onViewPagerChanged(position, positionOffset);
            scrollToTab(position, positionOffset);
        }

        @Override
        public void onPageSelected(int position) {
            TextView selected = (TextView) strip.getChildAt(position);
            if (oldPos != null) if (!oldPos.equals(selected)) oldPos.setTextColor(textColor);
            oldPos = selected;
            selected.setTextColor(selectedTextColor);
//            scrollToTab(position, 0);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
