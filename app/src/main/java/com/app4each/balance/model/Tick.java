package com.app4each.balance.model;

import io.realm.RealmObject;

/**
 * Created by vito on 30/10/2017.
 */

public class Tick extends RealmObject {
    public float min;
    public float max;


    public Tick(){ }

    public Tick(float min, float max){
        this.min = min;
        this.max = max;
    }
}