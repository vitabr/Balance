package com.app4each.balance.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app4each.balance.R;
import com.app4each.balance.view.adapters.RecyclerViewAdapter;

import java.util.ArrayList;


/**
 * Created by vito on 30/10/2017.
 */

public class MainFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerAdapter;
    private GridLayoutManager mManager;
    private ArrayList<String> mList = new ArrayList<>();

    public MainFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mList.add("DollarCoin");
        mList.add("BitCoin");
        mList.add("HuinyaCoin");
        mList.add("Etherium");

        /*setRetainInstance(true);*/
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerView);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


        refreshRecycler();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    private void refreshRecycler() {

        mRecyclerView.setLayoutManager(null);
        mManager = new GridLayoutManager(getContext(),  1);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerAdapter = new RecyclerViewAdapter(mList);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerAdapter.notifyDataSetChanged();

    }

}
