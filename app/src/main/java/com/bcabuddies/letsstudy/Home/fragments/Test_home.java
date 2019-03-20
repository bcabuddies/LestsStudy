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
public class Test_home extends Fragment {


    public Test_home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_home, container, false);
    }

    public static Fragment newInstance() {
        Test_home fragment = new Test_home();
        return fragment;
    }
}
