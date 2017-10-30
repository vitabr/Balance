package com.app4each.balance.controller.network;

import com.app4each.balance.controller.network.results.Result;
import com.app4each.balance.model.Balance;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by vito on 30/10/2017.
 */

public interface IApi {

    @GET("bins/iptrr")
    Call<Balance> getBalance();

    @GET("bins/h1bj3")
    Call<Result<List<List<Integer>>>> getChart();

}
