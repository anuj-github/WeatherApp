package com.android.weather.ui.weather;

import com.android.weather.model.ApiResponse;
import com.android.weather.network.NetworkApiManager;

public class WeatherPresenter implements IWeatherContract.IWeatherPresenter, IWeatherContract.onFinishedListener
{
    IWeatherContract.IWeatherView view;

    public WeatherPresenter(IWeatherContract.IWeatherView view)
    {
        this.view = view;
    }

    @Override
    public void loadWeatherData(String address)
    {
        view.showProgress();
        NetworkApiManager.getInstance().loadFromApi(address, this);
    }

    @Override
    public void onDestroy()
    {
        view = null;
    }

    @Override
    public void onFinished(ApiResponse response)
    {
        if (view != null) {
            view.hideProgress();
            view.refreshWeather(response);
        }
    }

    @Override
    public void onFailure()
    {
        if (view != null) {
            view.hideProgress();
            view.showError();
        }
    }

    @Override
    public void onInternetNotConnected()
    {
        if (view != null) {
            view.hideProgress();
            view.showError();
        }
    }
}
