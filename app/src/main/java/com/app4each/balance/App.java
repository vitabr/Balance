package com.app4each.balance;

import android.app.Application;

import com.app4each.balance.controller.network.Api;

/**
 * Created by vito on 30/10/2017.
 */

public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        // Retrofit Network module initialization
        Api.init();
    }
}
