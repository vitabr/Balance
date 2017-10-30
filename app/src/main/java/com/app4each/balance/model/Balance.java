package com.app4each.balance.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by vito on 30/10/2017.
 */

public class Balance extends RealmObject {
    public RealmList<Token> balances;
    public double totalBalance;
    public double usdChange;

    public Balance(){ }
}