package com.android.weather.ui.weather;

import java.util.List;

import com.android.weather.R;
import com.android.weather.model.ApiResponse;
import com.android.weather.model.Current;
import com.android.weather.model.Forecastday;
import com.android.weather.utils.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//TODO move Location listner to separate class
public class WeatherActivity extends AppCompatActivity implements IWeatherContract.IWeatherView, LocationListener
{
    private static final String TAG = WeatherActivity.class.getSimpleName();
    LocationManager locationManager;
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
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Weather activity created");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new WeatherPresenter(this);
        getLocation();
    }

    @Override
    public void showProgress()
    {
        progressBar.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        weatherView.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress()
    {
        progressBar.setVisibility(View.GONE);
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
        //current day
        Current current = response.getCurrent();
        currentCity.setText(response.getLocation().getName());
        currentCityTemp.setText(Utils.getTemp(current.getTempC()));
        List<Forecastday> forecastdayList = response.getForecast().getForecastday();
        //next day
        nextDay.setText(Utils.getDay(forecastdayList.get(1).getDate()));
        nextDayTemp.setText(Utils.getTemp(forecastdayList.get(1).getDay().getAvgtempC()));
        //second day
        secondDay.setText(Utils.getDay(forecastdayList.get(2).getDate()));
        secondDayTemp.setText(Utils.getTemp(forecastdayList.get(2).getDay().getAvgtempC()));
        //third day
        thirdDay.setText(Utils.getDay(forecastdayList.get(3).getDate()));
        thirdDayTemp.setText(Utils.getTemp(forecastdayList.get(3).getDay().getAvgtempC()));
        // fourth day
        fourthDay.setText(Utils.getDay(forecastdayList.get(4).getDate()));
        fourthDayTemp.setText(Utils.getTemp(forecastdayList.get(4).getDay().getAvgtempC()));


    }

    @Override
    public void showToast(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError()
    {
        hideProgress();
        errorView.setVisibility(View.VISIBLE);
        weatherView.setVisibility(View.GONE);
    }

    @OnClick(R.id.retryButton)
    public void onRetry()
    {
        showProgress();
        if (address != null) {
            presenter.loadWeatherData(address);
        }
        else {
            getLocation();
        }
    }

    /**
     * Get current location of the device
     */
    void getLocation()
    {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (gpsEnabled && networkEnabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 500, this);
            }
            else {
                showPromptToEnableLocation();
            }
        }
        catch (SecurityException e) {
            Log.i(TAG, "Error while getting location" + e);
            showError();
        }
    }

    private void showPromptToEnableLocation()
    {
        // notify user
        showError();
        new AlertDialog.Builder(this)
            .setMessage(R.string.gps_network_not_enabled)
            .setPositiveButton(R.string.open_location_settings, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt)
                {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            })

            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                }
            })
            .show();
    }

    @Override
    public void onLocationChanged(Location location)
    {
        Log.i(TAG, "on Location changed");
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        address = Utils.getLatLong(latitude, longitude);
        presenter.loadWeatherData(address);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
        //TODO
        Log.i(TAG, "Location provider status changed");
    }

    @Override
    public void onProviderEnabled(String provider)
    {
        Log.i(TAG, "Location provider enabled");
        if (address == null) {
            getLocation();
        }
    }

    @Override
    public void onProviderDisabled(String provider)
    {
        Log.i(TAG, "Location provider disabled");
        if (address == null) {
            showToast(getString(R.string.enablegps));
            showError();
        }
    }
}
