package com.app4each.balance;

import android.app.Application;

import com.app4each.balance.controller.network.Api;

import io.realm.Realm;

/**
 * Created by vito on 30/10/2017.
 */

public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        // Realm initialization
        Realm.init(this);

        // Retrofit Network module initialization
        Api.init();

        // Get initial data
        Api.updateBalance();
        Api.updateChart();
    }
}
