package com.android.weather.network;

import com.android.weather.model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiInterface
{
    @GET
    Call<ApiResponse> getWeather(@Url String url);
}
