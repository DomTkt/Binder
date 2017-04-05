package com.example.lp.binder.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lp.binder.R;
import com.example.lp.binder.adapter.HomeGridAdapter;
import com.example.lp.binder.adapter.HomeGridAdapterListener;

/**
 * Created by romai on 05/04/2017.
 */

public class HomeFragment extends Fragment implements HomeGridAdapterListener {

    private RecyclerView gridView;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (RecyclerView) view.findViewById(R.id.fragment_main_recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_main_swiperefreshlayout);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        gridView.setLayoutManager(layoutManager);
        HomeGridAdapter adapter = new HomeGridAdapter(this);
        gridView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO refresh list
            }
        });

        return view;
    }
}
