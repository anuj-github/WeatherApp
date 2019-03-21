package com.android.weather.network;

import com.android.weather.model.ApiResponse;
import com.android.weather.ui.weather.IWeatherContract;
import com.android.weather.utils.Constant;


import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkApiManager
{
    private static final String TAG = NetworkApiManager.class.getSimpleName();
    public static Retrofit retrofit;
    private volatile static NetworkApiManager instance;

    private NetworkApiManager()
    {
        initializeRetroFit();
    }

    public static NetworkApiManager getInstance()
    {
        if (instance == null) {
            synchronized (NetworkApiManager.class) {
                if (instance == null) {
                    instance = new NetworkApiManager();
                }
            }
        }
        return instance;
    }

    /**
     * Initialize retrofit instance
     */
    private void initializeRetroFit()
    {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                .baseUrl(Constant.CURRENT_URI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }
    }


    /**
     * do webservice call to get weather for current day and next 5 days
     *
     * @param listener
     */
    public void loadFromApi(String address, IWeatherContract.onFinishedListener listener)
    {
        ApiInterface showApi = retrofit.create(ApiInterface.class);
        String url = Constant.FORECAST_URI+address+Constant.day;
        Log.i(TAG, "Url is " + url);
        Call<ApiResponse> call = showApi.getWeather(url);
        call.enqueue(new Callback<ApiResponse>()
        {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response)
            {
                //onFinishedListener.onFinished(response.body());
                listener.onFinished(response.body());
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t)

            {
                Log.e(TAG, "fail " + t.toString());
                listener.onFailure();
            }
        });
    }

}
