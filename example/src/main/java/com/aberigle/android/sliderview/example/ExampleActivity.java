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

import com.aberigle.android.sliderview.SlidingTabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ExampleActivity extends AppCompatActivity implements ContentFragment.OnPlayGroundItemClickListener {

    private ActionBar          bar;
    private SlidingTabLayout   slidingHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_example);

        bar = getSupportActionBar();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        SimplePagerAdapter pagerAdapter = new SimplePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        slidingHeader = (SlidingTabLayout) findViewById(R.id.slidingtab);
        slidingHeader.setViewpager(viewPager);
        slidingHeader.setElevation(16);
        slidingHeader.attachToActionBar(bar);

        slidingHeader.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public int getRandom(int min, int max) {
                return new Random().nextInt(max - min) + min;
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                int color = Color.rgb(
                        getRandom(100, 200),
                        getRandom(100, 200),
                        getRandom(100, 200)
                );
                slidingHeader.setBackgroundColor(color);
                slidingHeader.setBorderIndicatorThicknessDPS(
                        getRandom(position + 1, (position + 1) * 3)
                );
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
    public void onPlayGroundItemClick(View playground, View clickedView) {
        switch (clickedView.getId()) {
            case R.id.customTabView:
                setCustomTabView();
                break;
            case R.id.defaultTabView:
                setDefaultTabView();
                break;
        }
    }

    private void setDefaultTabView() {
        slidingHeader.setDefaultTabView();
        slidingHeader.refreshViews();
    }

    private void setCustomTabView() {
        slidingHeader.setCustomTabView(R.layout.custom_tab, R.id.text);
        slidingHeader.refreshViews();
    }

    public class SimplePagerAdapter extends FragmentPagerAdapter {

        public List<String> pages;

        public SimplePagerAdapter(FragmentManager fm) {
            super(fm);
            pages = new ArrayList<>();
            Collections.addAll(pages, "playground list scrollview one two three four five".split(" "));
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
