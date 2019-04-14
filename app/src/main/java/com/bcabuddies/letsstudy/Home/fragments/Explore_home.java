package com.bcabuddies.letsstudy.Home.fragments;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bcabuddies.letsstudy.Adapter.SearchRecyclerAdapter;
import com.bcabuddies.letsstudy.Home.Presenter.Explore_homePresenter;
import com.bcabuddies.letsstudy.Home.Presenter.Explore_homePresenterImpl;
import com.bcabuddies.letsstudy.Home.view.Explore_homeView;
import com.bcabuddies.letsstudy.Model.PostData;
import com.bcabuddies.letsstudy.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class Explore_home extends Fragment implements Explore_homeView {

    @BindView(R.id.search_text_layout)
    TextInputLayout searchTextLayout;
    @BindView(R.id.search_btn)
    ImageView searchBtn;
    @BindView(R.id.search_recycler)
    RecyclerView searchRecycler;

    private Explore_homePresenter presenter;
    private final static String TAG = "Explore_home.java";
    private ArrayList<PostData> list;

    public Explore_home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore_home, container, false);
        ButterKnife.bind(this, view);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        presenter = new Explore_homePresenterImpl(firestore);
        presenter.attachView(this);

        list = new ArrayList<>();

        Objects.requireNonNull(searchTextLayout.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //search on every keystore
                Log.e(TAG, "onTextChanged: typed " + s);
                if (count > 0) {
                    presenter.search(String.valueOf(s));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }

    public static Fragment newInstance() {
        return new Explore_home();
    }

    @Override
    public void getData(ArrayList<PostData> postList) {
        list = postList;
        Log.e(TAG, "getData: postList size " + postList.size());
        Log.e(TAG, "getData: list size " + list.size());
        Log.e(TAG, "recyclerInit: recycler initialized ");
        SearchRecyclerAdapter adapter = new SearchRecyclerAdapter(list);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        searchRecycler.setLayoutManager(manager);
        searchRecycler.setAdapter(adapter);
    }
}
