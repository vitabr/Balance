package com.app4each.balance.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.app4each.balance.R;
import java.util.ArrayList;

/**
 * Created by vito on 30/10/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static ArrayList<String> mTempList;


    public RecyclerViewAdapter(ArrayList<String> movies) {
        this.mTempList = movies;
    }

    //Holder
    //RecyclerView

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView rate;
        TextView balance;
        TextView amplitude;
        Context mContext;
        //Create viewHolder Constructor

        public ViewHolder(final View v) {
            super(v);

            mContext = v.getContext();

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(mContext,"Clicked",Toast.LENGTH_LONG).show();
                }
            });

            title = v.findViewById(R.id.titleCoin);
            balance = v.findViewById(R.id.titleBalance);
            amplitude = v.findViewById(R.id.titleAmplitude);
            rate = v.findViewById(R.id.titleRate);


        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_item,parent , false); //row_card for linear

        ViewHolder mHolder = new ViewHolder(v);


        return mHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder mHolder, int position) {

        mHolder.title.setText(mTempList.get(position));
        mHolder.balance.setText(mTempList.get(position));
        mHolder.rate.setText(mTempList.get(position));
        mHolder.amplitude.setText(mTempList.get(position));


    }

    @Override
    public int getItemCount() {
        return mTempList.size();
    }
}