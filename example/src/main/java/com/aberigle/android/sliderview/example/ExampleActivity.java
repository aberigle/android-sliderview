package com.aberigle.android.sliderview.example;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aberigle.android.sliderview.SlidingTabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ExampleActivity extends ActionBarActivity {

    private ViewPager viewPager;
    private SimplePagerAdapter pagerAdapter;
    private ActionBar bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        bar = getSupportActionBar();

        bar.setElevation(0);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        pagerAdapter = new SimplePagerAdapter();
        viewPager.setAdapter(pagerAdapter);

        SlidingTabLayout slidingTab = (SlidingTabLayout) findViewById(R.id.slidingtab);
        slidingTab.setViewpager(viewPager);
        slidingTab.setElevation(8);
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
            bar.hide();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SimplePagerAdapter extends PagerAdapter {

        public List<String> pages;

        public SimplePagerAdapter() {
            pages = new ArrayList<>();
            Collections.addAll(pages, "1 2 3 4 5 6 7 8 9 10 11".split(" "));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = getLayoutInflater().inflate(R.layout.example_page, container, false);
            container.addView(view);
            TextView text = (TextView) view.findViewById(R.id.text);
            text.setText("page: " + pages.get(position));
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "PAGE " + pages.get(position);
        }

        @Override
        public int getCount() {
            return pages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }
    }
}
