package com.app4each.balance.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app4each.balance.R;
import com.app4each.balance.model.Tick;
import com.app4each.balance.view.adapters.RecyclerViewAdapter;
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
public class ScrollingActivity extends AppCompatActivity  {



    private MainFragment mMainFragment;
    private DashboardFragment mDashboardFragment;
    private SettingsFragment mSettingsFragment;
    private FragmentManager mFragmentManager;
    private final static String MAIN_FRAGMENT = "main";
    private final static String DASHBOARD_FRAGMENT = "dashboard";
    private final static String SETTINGS_FRAGMENT = "settings";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    if (!item.isChecked()) {

                        mFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, mMainFragment, MAIN_FRAGMENT)
                                .addToBackStack(MAIN_FRAGMENT)
                                .commit();

                    }

                    return true;
                case R.id.navigation_dashboard:

                    if (!item.isChecked()) {


                        mFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, mDashboardFragment, DASHBOARD_FRAGMENT)
                                .addToBackStack(DASHBOARD_FRAGMENT)
                                .commit();

                    }

                    return true;
                case R.id.navigation_notifications:
                    if (!item.isChecked()) {

                        mFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, mSettingsFragment, SETTINGS_FRAGMENT)
                                .addToBackStack(SETTINGS_FRAGMENT)
                                .commit();

                    }
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        mMainFragment = new MainFragment();
        mDashboardFragment = new DashboardFragment();
        mSettingsFragment = new SettingsFragment();
        mFragmentManager = getSupportFragmentManager();

        mFragmentManager.beginTransaction().add(R.id.fragment_container, mMainFragment, "main").addToBackStack("main").commit();

        final BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }





}
