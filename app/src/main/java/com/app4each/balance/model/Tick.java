package com.app4each.balance.model;

import io.realm.RealmObject;

/**
 * Created by vito on 30/10/2017.
 */

public class Tick extends RealmObject {
    public int min;
    public int max;


    public Tick(){ }

    public Tick(int min, int max){
        this.min = min;
        this.max = max;
    }
}