package com.aberigle.android.sliderview.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.aberigle.android.sliderview.SlidingTabLayout;

public class ContentFragment extends Fragment {

    private static final String LAYOUT = "layout";
    private int              position;
    private SlidingTabLayout header;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        header = ((ExampleActivity) getActivity()).getSlidingHeader();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        position = getArguments().getInt(LAYOUT);
        if (position == 0)
            return setUpPlayGround(inflater.inflate(R.layout.playground, container, false));
        if (position == 1)
            return setUpList(inflater.inflate(R.layout.list, container, false));
        if (position == 2)
            return setUpScrollView(inflater.inflate(R.layout.scrollview, container, false));

        return inflater.inflate(R.layout.example_page, container, false);
    }

    private View setUpScrollView(View view) {
        return view;
    }

    private View setUpList(View view) {
        ListView listView = (ListView) view;
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);

        adapter.addAll(
                "one two three four five six seven eight nine one two three four five six seven eight nine one two three four five six seven eight nine".split(" ")
        );

        listView.setAdapter(adapter);
        return listView;
    }

    public View setUpPlayGround(View view) {
        return view;
    }

    public static Fragment newInstance(int position) {
        ContentFragment fragment = new ContentFragment();
        Bundle          bundle   = new Bundle();
        bundle.putInt(LAYOUT, position);
        fragment.setArguments(bundle);
        return fragment;
    }

}