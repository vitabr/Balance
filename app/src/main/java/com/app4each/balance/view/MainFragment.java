package com.app4each.balance.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app4each.balance.R;
import com.app4each.balance.model.Token;
import com.app4each.balance.view.adapters.RecyclerViewAdapter;

import java.util.ArrayList;

import io.realm.Realm;


/**
 * Created by vito on 30/10/2017.
 */

public class MainFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerAdapter;
    private LinearLayoutManager mManager;
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

        mRecyclerView = initRecyclerView(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    private RecyclerView initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(null);
        mManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mManager);
        mRecyclerAdapter = new RecyclerViewAdapter(Realm.getDefaultInstance().where(Token.class).findAll());
        recyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerAdapter.notifyDataSetChanged();
        return recyclerView;
    }

}
