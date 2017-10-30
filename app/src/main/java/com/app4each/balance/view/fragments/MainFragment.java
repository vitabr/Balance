package com.app4each.balance.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app4each.balance.R;
import com.app4each.balance.model.Balance;
import com.app4each.balance.model.Tick;
import com.app4each.balance.model.Token;
import com.app4each.balance.view.SearchActivity;
import com.app4each.balance.view.adapters.RecyclerViewAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;


/**
 * Created by vito on 30/10/2017.
 */

public class MainFragment extends Fragment implements RealmChangeListener {

    private LineChart mChart;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerAdapter;
    private LinearLayoutManager mManager;
    private ArrayList<String> mList = new ArrayList<>();
    private RelativeLayout mBottomSheet;
    private ConstraintLayout mHeaderInfo;
    private ImageView mIvHeaderDirection;
    private TextView mHeaderBalance, mHeaderChange;
    private BottomSheetBehavior mBottomSheetBehavior;
    private TextView mTvBalance;
    private TextView mTvChange;
    private ImageView mIvIconDirection;

    public MainFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        mHeaderInfo = view.findViewById(R.id.header_info);
        mHeaderBalance = view.findViewById(R.id.tvHeaderBalance);
        mHeaderChange = view.findViewById(R.id.tvHeaderChange);
        mIvHeaderDirection = view.findViewById(R.id.ivHeaderDirection);
        mIvHeaderDirection.setVisibility(View.INVISIBLE);
        mHeaderChange.setVisibility(View.INVISIBLE);
        mHeaderBalance.setVisibility(View.INVISIBLE);
        mChart = view.findViewById(R.id.chart);
        mTvBalance = view.findViewById(R.id.tvBalance);
        mTvChange = view.findViewById(R.id.tvChange);
        mIvIconDirection = view.findViewById(R.id.ivIconDirection);

        mBottomSheet = view.findViewById(R.id.mainSheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);

        mRecyclerView = initRecyclerView(view);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {


            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                if (slideOffset > 0.98 || slideOffset == 1) {
                    mHeaderChange.setVisibility(View.VISIBLE);
                    mHeaderBalance.setVisibility(View.VISIBLE);
                    mIvHeaderDirection.setVisibility(View.VISIBLE);
                } else {
                    mHeaderChange.setVisibility(View.INVISIBLE);
                    mHeaderBalance.setVisibility(View.INVISIBLE);
                    mIvHeaderDirection.setVisibility(View.INVISIBLE);
                }
                mHeaderInfo.setAlpha(Math.abs(slideOffset - 1));
                mChart.setAlpha(Math.abs(slideOffset - 1));
            }
        });
        initChart();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add watcher for DB changes
        Realm.getDefaultInstance().addChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Remove watcher for DB changes
        Realm.getDefaultInstance().removeChangeListener(this);
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

    //*************************************/
    // private methods
    //*************************************/

    private void initChart() {

        mChart.setDrawGridBackground(false);
        mChart.setBackgroundTintMode(PorterDuff.Mode.SCREEN);

        // no description text
        mChart.getDescription().setEnabled(false);

        // Remove axis values
        mChart.getAxisLeft().setDrawLabels(false);
        mChart.getAxisRight().setDrawLabels(false);
        mChart.getXAxis().setDrawLabels(false);
        mChart.getLegend().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(false);

        // enable scaling and dragging
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setScaleXEnabled(false);
        mChart.setScaleYEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        // set an alternative background color
        mChart.setBackgroundColor(Color.TRANSPARENT);
        //mChart.setBackgroundColor(Color.GRAY);
        mChart.setAlpha(0.5f);

        updateChartData();

        // Remove spaces on sides
        mChart.setViewPortOffsets(0, 0, 0, 0);
        mChart.invalidate();
    }


    private void updateChartData() {

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Tick> ticks = realm.where(Tick.class).findAll();

        if (ticks.size() == 0)
            return;

        ArrayList<Entry> values = new ArrayList<Entry>();

        for (Tick tick : ticks) {
            values.add(new Entry(tick.min, tick.max));
        }

        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet 1");

            //set1.setDrawIcons(false);
            // set the line to be drawn like this "- - - - - -"
            //set1.enableDashedLine(10f, 5f, 0f);
            //set1.enableDashedHighlightLine(10f, 5f, 0f);

            set1.setCircleColor(Color.TRANSPARENT);
            set1.setColor(Color.BLACK);
            set1.setFillColor(Color.GRAY);
            set1.setLineWidth(1f);
            set1.setCircleRadius(0f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(0f);
            set1.setDrawFilled(true);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);
            //data.setDrawValues(false);

            // set data
            mChart.setData(data);
        }
    }

    private void updateInfoPanel() {
        Balance balance = Realm.getDefaultInstance().where(Balance.class).findFirst();
        mTvBalance.setText(""+balance.totalBalance);
        mTvChange.setText(""+balance.usdChange);
        mHeaderBalance.setText(""+balance.totalBalance);
        mHeaderChange.setText(""+balance.usdChange);
        if(balance.usdChange<0){
            mTvChange.setTextColor(Color.RED);
            mIvIconDirection.setImageResource(R.drawable.triangle_down);
            mHeaderChange.setTextColor(Color.RED);
            mIvHeaderDirection.setImageResource(R.drawable.triangle_down);
        }else{
            mTvChange.setTextColor(Color.GREEN);
            mIvIconDirection.setImageResource(R.drawable.triangle_up);
            mHeaderChange.setTextColor(Color.GREEN);
            mIvHeaderDirection.setImageResource(R.drawable.triangle_up);
        }
    }

    //*************************************/
    // Realm DB Change Listener
    //*************************************/

    @Override
    public void onChange(Object element) {
        updateChartData();
        updateInfoPanel();
    }

}
