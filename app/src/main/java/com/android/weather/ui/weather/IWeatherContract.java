package com.android.weather.ui.weather;

import com.android.weather.model.ApiResponse;

public interface IWeatherContract
{
    interface IWeatherView{
        void showProgress();
        void hideProgress();
        void refreshWeather(ApiResponse response);
        void showToast(String msg);
        void showError();
    }
    interface IWeatherPresenter{
        void loadWeatherData();
        void onDestroy();
    }

    interface onFinishedListener
    {

        void onFinished(ApiResponse response);

        void onFailure();

        void onInternetNotConnected();

    }
}
