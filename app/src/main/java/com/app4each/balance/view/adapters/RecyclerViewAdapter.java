package com.app4each.balance.view.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app4each.balance.App;
import com.app4each.balance.R;
import com.app4each.balance.model.Token;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.realm.RealmResults;

/**
 * Created by vito on 30/10/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static RealmResults<Token> mTokens;


    public RecyclerViewAdapter(RealmResults<Token> tokens) {
        this.mTokens = tokens;
    }

    //Holder
    //RecyclerView

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iconCoin;
        ImageView iconDirection;
        TextView name;
        TextView valPerToken;
        TextView balance;
        TextView change;

        Context mContext;

        public ViewHolder(final View v) {
            super(v);

            mContext = v.getContext();

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(mContext,"Clicked",Toast.LENGTH_LONG).show();
                }
            });
            iconCoin = v.findViewById(R.id.iconCoin);
            iconDirection = v.findViewById(R.id.iconDirection);
            name = v.findViewById(R.id.tokenName);
            change = v.findViewById(R.id.change);
            balance = v.findViewById(R.id.balance);
            valPerToken = v.findViewById(R.id.valPerToken);

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

        Token token = mTokens.get(position);
        mHolder.name.setText(token.tokenName);
        mHolder.balance.setText(""+token.balance);
        mHolder.valPerToken.setText(""+token.usdValuePerToken);
        mHolder.change.setText("("+token.usdChange+")");


        mHolder.change.setTextColor((token.usdChange<0)?Color.RED:Color.GREEN);
        mHolder.iconDirection.setImageResource((token.usdChange<0)?R.drawable.triangle_down:R.drawable.triangle_up);

        if(TextUtils.isEmpty(token.iconUrl)){
            mHolder.iconCoin.setVisibility(View.GONE);
        }else{
            Glide.with(App.getAppContext())
                    .load(token.iconUrl)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .error(R.drawable.empty)
                    .placeholder(R.drawable.empty)
                    .into(mHolder.iconCoin);
        }

    }

    @Override
    public int getItemCount() {
        return mTokens.size();
    }
}