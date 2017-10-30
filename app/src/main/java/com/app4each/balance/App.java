package com.app4each.balance;

import android.app.Application;
import android.content.Context;

import com.app4each.balance.controller.network.Api;

import io.realm.Realm;

/**
 * Created by vito on 30/10/2017.
 */

public class App extends Application{

    private static Context sContext;
    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;

        // Realm initialization
        Realm.init(this);

        // Retrofit Network module initialization
        Api.init();

        // Get initial data
        Api.updateBalance();
        Api.updateChart();
    }

    public static Context getAppContext() {
        return sContext;
    }
}
