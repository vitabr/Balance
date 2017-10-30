package com.app4each.balance.view;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.app4each.balance.R;
import com.app4each.balance.model.Balance;
import com.app4each.balance.model.Tick;
import com.app4each.balance.view.adapters.ViewPagerAdapter;
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
public class ScrollingActivity extends AppCompatActivity implements RealmChangeListener{


    private LineChart mChart;
    private ViewPager viewPager;
    private MainFragment mainFragment;
    private DashboardFragment viewFagment;
    private SettingsFragment settingsFragment;
    private MenuItem prevMenuItem;
    private AppBarLayout mAppbar;
    private TextView mTvBalance;
    private TextView mTvChange;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    viewPager.setCurrentItem(0);
                    mAppbar.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_dashboard:

                    viewPager.setCurrentItem(1);
                    mAppbar.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_notifications:

                    viewPager.setCurrentItem(2);

                    mAppbar.setVisibility(View.GONE);
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
       /* Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

       mTvBalance = findViewById(R.id.tvBalance);
       mTvChange = findViewById(R.id.tvChange);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        mAppbar = findViewById(R.id.app_bar);

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        final BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position == 0){

                    mAppbar.setVisibility(View.VISIBLE);
                }else{
                    mAppbar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                navigation.getMenu().getItem(position).setChecked(true);

                prevMenuItem = navigation.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        updateInfoPanel();
        initChart();

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Add watcher for DB changes
        Realm.getDefaultInstance().addChangeListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Remove watcher for DB changes
        Realm.getDefaultInstance().removeChangeListener(this);
    }


    //*************************************/
    // private methods
    //*************************************/

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mainFragment = new MainFragment();
        viewFagment = new DashboardFragment();
        settingsFragment = new SettingsFragment();
        adapter.addFragment(mainFragment);
        adapter.addFragment(viewFagment);
        adapter.addFragment(settingsFragment);
        viewPager.setAdapter(adapter);
    }

    private void initChart(){
        mChart =  findViewById(R.id.chart);
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
            set1 = (LineDataSet)mChart.getData().getDataSetByIndex(0);
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
