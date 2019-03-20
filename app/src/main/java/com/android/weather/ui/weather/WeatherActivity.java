package com.android.weather.ui.weather;

import java.util.List;

import com.android.weather.R;
import com.android.weather.model.ApiResponse;
import com.android.weather.model.Current;
import com.android.weather.model.Forecastday;
import com.android.weather.utils.Utils;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeatherActivity extends AppCompatActivity implements IWeatherContract.IWeatherView
{
    private static final String TAG = WeatherActivity.class.getSimpleName();
    IWeatherContract.IWeatherPresenter presenter;
    @BindView(R.id.weatherscreen)
    View weatherView;
    @BindView(R.id.errorscreen)
    View errorView;
    @BindView(R.id.retryButton)
    Button button;
    @BindView(R.id.temp_text)
    TextView currentCityTemp;
    @BindView(R.id.temp_city)
    TextView currentCity;
    //nextday
    @BindView(R.id.forecast_day_one)
    TextView nextDay;
    @BindView(R.id.forecast_temp_one)
    TextView nextDayTemp;
    //2ndday
    @BindView(R.id.forecast_day_two)
    TextView secondDay;
    @BindView(R.id.forecast_temp_two)
    TextView secondDayTemp;

    //3rdday
    @BindView(R.id.forecast_day_three)
    TextView thirdDay;
    @BindView(R.id.forecast_temp_three)
    TextView thirdDayTemp;

    //4th Day
    @BindView(R.id.forecast_day_four)
    TextView fourthDay;
    @BindView(R.id.forecast_temp_four)
    TextView fourthDayTemp;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Weather activity created");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new WeatherPresenter(this);
    }

    @Override
    public void showProgress()
    {

    }

    @Override
    public void hideProgress()
    {

    }

    @Override
    public void refreshWeather(ApiResponse response)
    {
        errorView.setVisibility(View.GONE);
        weatherView.setVisibility(View.VISIBLE);
        updateData(response);
    }

    /**
     * update text views with response text
     *
     * @param response
     */
    private void updateData(ApiResponse response)
    {
        //TODO Anuj Avoid duplicating and try recyler view for this
        Current current = response.getCurrent();
        currentCity.setText(response.getLocation().getName());
        currentCityTemp.setText(current.getTempC() + "");
        List<Forecastday> forecastdayList = response.getForecast().getForecastday();
        nextDay.setText(Utils.getDay(forecastdayList.get(1).getDate()));
        nextDayTemp.setText(Utils.getTemp(forecastdayList.get(1).getDay().getAvgtempC()));

        //
        secondDay.setText(Utils.getDay(forecastdayList.get(2).getDate()));
        secondDayTemp.setText(Utils.getTemp(forecastdayList.get(2).getDay().getAvgtempC()));
        //
        thirdDay.setText(Utils.getDay(forecastdayList.get(3).getDate()));
        thirdDayTemp.setText(Utils.getTemp(forecastdayList.get(3).getDay().getAvgtempC()));
        //
        //
        fourthDay.setText(Utils.getDay(forecastdayList.get(4).getDate()));
        fourthDayTemp.setText(Utils.getTemp(forecastdayList.get(4).getDay().getAvgtempC()));


    }

    @Override
    public void showToast(String msg)
    {

    }

    @Override
    public void showError()
    {
        errorView.setVisibility(View.VISIBLE);
        weatherView.setVisibility(View.GONE);
    }

    @OnClick(R.id.retryButton)
    public void onRetry()
    {
        presenter.loadWeatherData();
    }

}
