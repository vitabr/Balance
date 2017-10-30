package com.app4each.balance.controller.network;

import android.util.Log;

import com.app4each.balance.controller.eventbus.IEvent;
import com.app4each.balance.controller.eventbus.MessageEvent;
import com.app4each.balance.controller.network.results.Result;
import com.app4each.balance.model.Balance;
import com.app4each.balance.model.Tick;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by vito on 30/10/2017.
 */

public class Api implements IEvent {

    private final String BASE_URL = "https://api.myjson.com/";
    private static Api sInstnce = new Api();

    private static Retrofit sRetrofit;
    private static IApi sService;

    private Api() {

        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        //Instance for Rapido API calls
        sRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        sService = sRetrofit.create(IApi.class);
    }


    /**
     * Init function MUST be called on application start before any network request.
     *
     * @return
     */
    public static Api init() {
        if (sInstnce == null) {
            sInstnce = new Api();
        }
        return sInstnce;
    }

    public static void updateBalance() {


        if (sInstnce == null) {
            throw new ExceptionInInitializerError("Must call init() first!");
        }

        sService.getBalance().enqueue(
                new Callback<Balance>() {
                    @Override
                    public void onResponse(Call<Balance> call, Response<Balance> response) {
                        try {

                            Log.i("XXX", "url: " + call.request().url().toString());
                            final Balance result = response.body();
                            Log.i("XXX", "Response: " + response.body());



                            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {

                                    realm.where(Balance.class).findAll().deleteAllFromRealm();
                                    realm.copyToRealm(result);

                                    EventBus.getDefault().post(new MessageEvent(EVENT_BALANCE_DATA_UPDATED));
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<Balance> call, Throwable t) {

                        Log.e("XXX", "url: " + call.request().url().toString());
                        t.printStackTrace();
                        EventBus.getDefault().post(new MessageEvent(EVENT_NETWORK_CONNECTION_ERROR));
                        //TaxiApp.resetCurrentUserAndRestart();
                    }
                });
    }

    public static void updateChart() {

        if (sInstnce == null) {
            throw new ExceptionInInitializerError("Must call init() first!");
        }

        sService.getChart().enqueue(new Callback<Result<List<List<Integer>>>>() {
            @Override
            public void onResponse(Call<Result<List<List<Integer>>>> call, Response<Result<List<List<Integer>>>> response) {

                try {

                    Log.e("XXX", "url: " + call.request().url().toString());
                    final Result<List<List<Integer>>> result = response.body();
                    Log.e("XXX", "JSON: " + result);

                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {

                            for (List<Integer> tick : result.data) {
                                realm.copyToRealmOrUpdate(new Tick(tick.get(0), tick.get(1)));
                            }

                            EventBus.getDefault().post(new MessageEvent(EVENT_CHART_DATA_UPDATED));
                        }
                    });

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }


            @Override
            public void onFailure(Call<Result<List<List<Integer>>>> call, Throwable t) {

                Log.e("XXX", "url: " + call.request().url().toString());
                EventBus.getDefault().post(new MessageEvent(EVENT_NETWORK_CONNECTION_ERROR));
                t.printStackTrace();
            }
        });
    }

}
