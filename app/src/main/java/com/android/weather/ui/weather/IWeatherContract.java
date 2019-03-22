package com.android.weather.ui.weather;

import com.android.weather.model.ApiResponse;

public interface IWeatherContract
{
    interface IWeatherView{
        void showProgress();
        void hideProgress();
        void refreshWeather(ApiResponse response);
        void showToast(String msg);
        void showError(int errorCode);
    }
    interface IWeatherPresenter{
        void loadWeatherData(String address);
        void onDestroy();
    }

    interface onFinishedListener
    {

        void onFinished(ApiResponse response);

        void onFailure();

        void onInternetNotConnected();

    }
}
