package com.aberigle.android.sliderview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

/**
 * Created by aberigle on 06/04/15.
 */
public class SlidingTabLayout extends HorizontalScrollView {

    private static final float DEFAULT_TAB_VIEW_PADDING_DIPS = 16;

    private final SlidingTabStrip strip;

    private final int selectedTextColor;
    private final int textColor;

    private ViewPager                      viewpager;
    private PagerChangeListener            pagerListener;
    private ViewPager.OnPageChangeListener onPageChangeListener;
    private ActionBar                      bar;
    private ColorDrawable                  barBackground;

    private int customTabViewId;
    private int customTabViewTextViewId;

    public SlidingTabLayout(Context context) { this(context, null); }

    public SlidingTabLayout(Context context, AttributeSet attributes) {
        super(context, attributes);
        setHorizontalScrollBarEnabled(false);
        setFillViewport(true);

        selectedTextColor = context.getResources().getColor(R.color.white);
        textColor = context.getResources().getColor(R.color.bright_foreground_dark_disabled);

        strip = new SlidingTabStrip(context, Color.WHITE);
        addView(strip);

    }

    public void refreshViews() {
        populateFromPager();
    }

    public void setBorderIndicatorThicknessDPS(int dps) {
        strip.setBorderThicknessDPS(dps);
    }

    @Override
    public void setElevation(float elevation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) super.setElevation(elevation);
        else ViewCompat.setElevation(this, elevation);
    }

    private void populateFromPager() {
        PagerAdapter adapter = viewpager.getAdapter();
        View tab;
        strip.removeAllViews();
        if (adapter != null) for (int i = 0; i < adapter.getCount(); i++) {
            final int index = i;
            tab = getTabView(adapter.getPageTitle(i));
            tab.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewpager.setCurrentItem(index);
                }
            });
            strip.addView(tab);
        }

    }

    public View getTitleView(int position) {
        return strip.getChildAt(position);
    }
    
    public void setCustomTabView(int tabViewId, int textViewId) {
        this.customTabViewId = tabViewId;
        this.customTabViewTextViewId = textViewId;
    }

    private View getTabView(CharSequence title) {
        if (customTabViewId == 0) return getDefaultTabView(title);
        else return getCustomTabView(title);
    }

    private View getCustomTabView(CharSequence title) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View tabView = inflater.inflate(customTabViewId, strip, false);
        TextView text = (TextView) tabView.findViewById(customTabViewTextViewId);
        if (text != null) text.setText(title);
        return tabView;
    }

    private View getDefaultTabView(CharSequence title) {
        int padding = (int) (DEFAULT_TAB_VIEW_PADDING_DIPS * getResources().getDisplayMetrics().density);
        TextView tab = new TextView(getContext());
        tab.setGravity(Gravity.CENTER);
        tab.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        tab.setTypeface(Typeface.DEFAULT_BOLD);
        tab.setTextColor(textColor);
        tab.setAllCaps(true);
        tab.setText(title);
        tab.setPadding(padding, padding, padding, padding);
        return tab;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (viewpager != null) {
            pagerListener.onPageSelected(viewpager.getCurrentItem());
            scrollToTab(viewpager.getCurrentItem(), 0);
        }
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        if (bar != null) ((ColorDrawable) barBackground.mutate()).setColor(color);
    }

    private void scrollToTab(int index, float positionOffset) {
        final int childCount = strip.getChildCount();
        if (childCount == 0 || index < 0 || index >= childCount) return;

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

    public void setViewpager(ViewPager pager) {
        if (pager != null) {
            viewpager = pager;
            pager.setOnPageChangeListener(pagerListener = new PagerChangeListener());
            populateFromPager();
        }
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    public void attachToActionBar(ActionBar bar) {
        this.bar = bar;
        if (bar != null) {
            bar.setElevation(0);
            barBackground = new ColorDrawable(((ColorDrawable) getBackground()).getColor());
            bar.setBackgroundDrawable(barBackground);
        }
    }

    private class PagerChangeListener implements ViewPager.OnPageChangeListener {

        private TextView oldPos;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            strip.onViewPagerChanged(position, positionOffset);
            scrollToTab(position, positionOffset);
            if (onPageChangeListener != null)
                onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            TextView selected;
            View view = strip.getChildAt(position);
            if (customTabViewId == 0)
                selected = (TextView) view;
            else selected = (TextView) view.findViewById(customTabViewTextViewId);
            if (oldPos != null) if (!oldPos.equals(selected)) oldPos.setTextColor(textColor);
            oldPos = selected;
            selected.setTextColor(selectedTextColor);
            if (onPageChangeListener != null) onPageChangeListener.onPageSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (onPageChangeListener != null) onPageChangeListener.onPageScrollStateChanged(state);
        }
    }

}
