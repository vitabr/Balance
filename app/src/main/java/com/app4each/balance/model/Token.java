package com.app4each.balance.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by vito on 30/10/2017.
 */

public class Token extends RealmObject {

    @PrimaryKey
    public String tokenName;
    public String iconUrl;
    public double usdValuePerToken;
    public double totUsdValue;
    public double usdChange;
    public double balance;

    public Token(){ }
}
