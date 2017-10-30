package com.app4each.balance.view;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.app4each.balance.R;
import com.app4each.balance.model.Tick;
import com.app4each.balance.view.adapters.RecyclerViewAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ScrollingActivity extends AppCompatActivity implements RealmChangeListener{


    private LineChart mChart;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerAdapter;
    private GridLayoutManager mManager;
    private ArrayList<String> mList = new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Toast.makeText(ScrollingActivity.this, "message 1", Toast.LENGTH_LONG).show();
                    return true;
                case R.id.navigation_dashboard:
                    Toast.makeText(ScrollingActivity.this, "message 2", Toast.LENGTH_LONG).show();
                    return true;
                case R.id.navigation_notifications:

                    Toast.makeText(ScrollingActivity.this, "message 3", Toast.LENGTH_LONG).show();
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(ScrollingActivity.this, "" + mList.size(), Toast.LENGTH_LONG).show();
                mList.add("new Record");
                mRecyclerAdapter.notifyDataSetChanged();

            }
        });


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mRecyclerView = findViewById(R.id.recyclerView);
        refreshRecycler();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //*************************************/
    // private methods
    //*************************************/
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

    private void refreshRecycler() {

        mRecyclerView.setLayoutManager(null);
        mManager = new GridLayoutManager(ScrollingActivity.this, 1);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerAdapter = new RecyclerViewAdapter(mList);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerAdapter.notifyDataSetChanged();

    }


    //*************************************/
    // Realm DB Change Listener
    //*************************************/
    @Override
    public void onChange(Object element) {
        updateChartData();
        mRecyclerAdapter.notifyDataSetChanged();
    }
}
