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
public class Prep_home extends Fragment {


    public Prep_home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_prep_home, container, false);
    }

    public static Fragment newInstance() {
        Prep_home fragment = new Prep_home();
        return fragment;
    }
}
