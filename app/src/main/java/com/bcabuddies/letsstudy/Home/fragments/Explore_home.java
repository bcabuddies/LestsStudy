package com.bcabuddies.letsstudy.Home.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bcabuddies.letsstudy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Explore_home extends Fragment {


    public Explore_home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore_home, container, false);
    }

}
