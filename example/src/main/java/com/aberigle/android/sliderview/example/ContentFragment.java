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
        else if (position == 2)
            view = setUpScrollView(inflater.inflate(R.layout.scrollview, container, false));
        else
            view = inflater.inflate(R.layout.example_page, container, false);

        return view;
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

    public View setUpPlayGround(final View playground) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playGroundListener.onPlayGroundItemClick(playground, view);
            }
        };
        playground.findViewById(R.id.customTabView).setOnClickListener(listener);
        playground.findViewById(R.id.defaultTabView).setOnClickListener(listener);
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
        void onPlayGroundItemClick(View playground, View clickedView);
    }

}