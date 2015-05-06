package com.aberigle.android.sliderview.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.aberigle.android.sliderview.SlidingTabLayout;

import java.util.ArrayList;

public class ContentFragment extends Fragment {

    private static final String LAYOUT = "layout";
    private int              position;
    private OnPlayGroundItemClickListener playGroundListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playGroundListener  = (ExampleActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        position = getArguments().getInt(LAYOUT);
        View view;
        if (position == 0)
            view = setUpPlayGround(inflater.inflate(R.layout.playground, container, false));
        else if (position == 1)
            view = setUpList(inflater.inflate(R.layout.list, container, false));
        else
            view = setUpExamplePage(inflater.inflate(R.layout.example_page, container, false), position - 1);

        return view;
    }

    public View setUpExamplePage(View view, int title) {
        ((TextView) view.findViewById(R.id.text)).setText(String.valueOf(title));
        return view;
    }
    private View setUpList(View view) {
        ListView listView = (ListView) view;
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);

        listView.setOnScrollListener((ExampleActivity) getActivity());

        adapter.addAll(
                "one two three four five six seven eight nine one two three four five six seven eight nine one two three four five six seven eight nine".split(" ")
        );

        listView.setAdapter(adapter);
        return listView;
    }

    public View setUpPlayGround(final View playground) {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playGroundListener.onPlayGroundItemInteract(playground, view);
            }
        };

        SeekBar.OnSeekBarChangeListener seekBarListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                playGroundListener.onPlayGroundItemInteract(playground, seekBar);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };

        int[] ids = {
                R.id.randomColor,
                R.id.hideBarOnScroll,
                R.id.customTabView,
                R.id.defaultTabView
        };

        for (int id : ids) playground.findViewById(id).setOnClickListener(clickListener);

        int[] colorIds = {
                R.id.redBar,
                R.id.blueBar,
                R.id.greenBar
        };
        for (int id : colorIds)
            ((SeekBar) playground.findViewById(id)).setOnSeekBarChangeListener(seekBarListener);

        return playground;
    }

    public static Fragment newInstance(int position) {
        ContentFragment fragment = new ContentFragment();
        Bundle          bundle   = new Bundle();
        bundle.putInt(LAYOUT, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    public interface OnPlayGroundItemClickListener {
        void onPlayGroundItemInteract(View playground, View clickedView);
    }

}