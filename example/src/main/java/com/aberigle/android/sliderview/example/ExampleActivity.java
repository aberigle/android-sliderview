package com.aberigle.android.sliderview.example;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.SeekBar;

import com.aberigle.android.sliderview.SlidingTabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ExampleActivity extends AppCompatActivity implements ContentFragment.OnPlayGroundItemClickListener, AbsListView.OnScrollListener {

    private ActionBar        bar;
    private SlidingTabLayout slidingHeader;
    private boolean          hideBarOnScroll;
    private int              lastScrollY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_example);

        bar = getSupportActionBar();

        ViewPager          viewPager    = (ViewPager) findViewById(R.id.viewpager);
        SimplePagerAdapter pagerAdapter = new SimplePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        slidingHeader = (SlidingTabLayout) findViewById(R.id.slidingtab);
        slidingHeader.setViewpager(viewPager);
        slidingHeader.setElevation(16);
        slidingHeader.attachToActionBar(bar);

        hideBarOnScroll = false;

        slidingHeader.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_example, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public SlidingTabLayout getSlidingHeader() {
        return slidingHeader;
    }

    @Override
    public void onPlayGroundItemInteract(View playground, View clickedView) {
        int red     = -1,
            green   = -1,
            blue    = -1;
        switch (clickedView.getId()) {
            case R.id.customTabView:
                setCustomTabView();
                break;
            case R.id.defaultTabView:
                setDefaultTabView();
                break;
            case R.id.hideBarOnScroll:
                CheckBox checkBox = (CheckBox) clickedView;
                hideBarOnScroll = checkBox.isChecked();
                break;
            case R.id.randomColor:
                Random random = new Random();
                red     = random.nextInt(200 - 100) + 100;
                green   = random.nextInt(200 - 100) + 100;
                blue    = random.nextInt(200 - 100) + 100;
            case R.id.redBar:
            case R.id.greenBar:
            case R.id.blueBar:
                SeekBar redbar      = (SeekBar) playground.findViewById(R.id.redBar);
                SeekBar greenbar    = (SeekBar) playground.findViewById(R.id.greenBar);
                SeekBar bluebar     = (SeekBar) playground.findViewById(R.id.blueBar);

                if (red   == -1) red    = redbar.getProgress();
                if (green == -1) green  = greenbar.getProgress();
                if (blue  == -1) blue   = bluebar.getProgress();

                redbar.setProgress(red);
                greenbar.setProgress(green);
                bluebar.setProgress(blue);
                updateHeaderColor(
                        red,
                        green,
                        blue);
                break;
            case R.id.stripSizeBar:
                updateBorderIndicatorThickness(((SeekBar) clickedView).getProgress());
                break;
        }
    }

    private void updateBorderIndicatorThickness(int amount) {
        slidingHeader.setBorderIndicatorThicknessDPS(amount / 10);
    }

    private void updateHeaderColor(int red, int green, int blue) {
        int color = Color.rgb(red, green, blue);
        slidingHeader.setBackgroundColor(color);
    }

    private void setDefaultTabView() {
        slidingHeader.setDefaultTabView();
        slidingHeader.refreshViews();
    }

    private void setCustomTabView() {
        slidingHeader.setCustomTabView(R.layout.custom_tab, R.id.text);
        slidingHeader.refreshViews();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (hideBarOnScroll) {
            View firstChild = view.getChildAt(0);
            if (firstChild != null) {
                int scrollY = -firstChild.getTop() + firstVisibleItem * firstChild.getHeight();

                int diff = Math.abs(lastScrollY - scrollY);

                if (diff > bar.getHeight() / 2) {
                    if (scrollY > lastScrollY && bar.isShowing()) bar.hide();
                    else if (scrollY < lastScrollY && !bar.isShowing()) bar.show();
                    lastScrollY = scrollY;
                }

            }
        }
    }

    public class SimplePagerAdapter extends FragmentPagerAdapter {

        public List<String> pages;

        public SimplePagerAdapter(FragmentManager fm) {
            super(fm);
            pages = new ArrayList<>();
            Collections.addAll(pages, "playground list one two three four five six".split(" "));
        }

        @Override
        public Fragment getItem(int position) {
            return ContentFragment.newInstance(position);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pages.get(position);
        }

        @Override
        public int getCount() {
            return pages.size();
        }

    }
}
