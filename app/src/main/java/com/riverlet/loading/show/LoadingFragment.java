package com.riverlet.loading.show;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class LoadingFragment extends Fragment {

    private int layoutId;
    public static LoadingFragment newInstance( int layoutId) {
        LoadingFragment fragment = new LoadingFragment();
        fragment.layoutId = layoutId;
        return fragment;
    }
    public LoadingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(layoutId, container, false);
    }
}
