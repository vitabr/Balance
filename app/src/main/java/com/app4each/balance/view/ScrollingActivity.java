package com.app4each.balance.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.app4each.balance.R;
import com.app4each.balance.view.adapters.RecyclerViewAdapter;

import java.util.ArrayList;

public class ScrollingActivity extends AppCompatActivity {

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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

    private void refreshRecycler() {

        mRecyclerView.setLayoutManager(null);
        mManager = new GridLayoutManager(ScrollingActivity.this, 1);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerAdapter = new RecyclerViewAdapter(mList);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerAdapter.notifyDataSetChanged();

    }
}
