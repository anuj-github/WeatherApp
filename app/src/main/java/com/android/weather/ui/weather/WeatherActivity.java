package com.android.weather.ui.weather;

import java.util.List;

import com.android.weather.R;
import com.android.weather.adapter.WeatherForecastAdapter;
import com.android.weather.model.ApiResponse;
import com.android.weather.model.Current;
import com.android.weather.model.Forecastday;
import com.android.weather.utils.Constant;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//TODO move Location listener to separate class
public class WeatherActivity extends AppCompatActivity implements IWeatherContract.IWeatherView, LocationListener
{
    private static final String TAG = WeatherActivity.class.getSimpleName();
    LocationManager mLocationManager;
    IWeatherContract.IWeatherPresenter mPresenter;
    @BindView(R.id.weatherscreen)
    View weatherView;
    @BindView(R.id.errorscreen)
    View errorView;
    @BindView(R.id.errorMsg)
    TextView errorText;
    @BindView(R.id.retryButton)
    Button button;
    @BindView(R.id.temp_text)
    TextView currentCityTemp;
    @BindView(R.id.temp_city)
    TextView currentCity;
    private WeatherForecastAdapter mAdapter;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.weatherRecylerView)
    RecyclerView mRecyclerView;
    String mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Weather activity created");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mPresenter = new WeatherPresenter(this);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getLocation();
        initAdapter();
    }

    /**
     * initialize adaper
     */
    private void initAdapter(){
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new WeatherForecastAdapter();
        mRecyclerView.setAdapter(mAdapter);
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

    /**
     * update ui with received response
     * @param response
     */
    @Override
    public void refreshWeather(ApiResponse response)
    {
        hideProgress();
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
        //current day
        Current current = response.getCurrent();
        currentCity.setText(response.getLocation().getName());
        currentCityTemp.setText(Utils.getTemp(current.getTempC()));
        List<Forecastday> forecastdayList = response.getForecast().getForecastday();
        //remove current day
        forecastdayList.remove(0);
        mAdapter.setForecastdayList(forecastdayList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showToast(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(int errorCode)
    {
        hideProgress();
        updateErrorText(errorCode);
        errorView.setVisibility(View.VISIBLE);
        weatherView.setVisibility(View.GONE);
    }

    /**
     * update error text
     *
     * @param errorCode
     */
    public void updateErrorText(int errorCode)
    {
        switch (errorCode) {
        case Constant
            .Error.INTERNET_NOT_AVAILABLE:
            errorText.setText(R.string.internet_not_available);
            break;
        case Constant.Error.LOCATION_PROVIDER_DISABLED:
            errorText.setText(R.string.enablegps);
            break;
        case Constant.Error.DEFAULT:
            errorText.setText(R.string.error);
            break;
        }
    }

    @OnClick(R.id.retryButton)
    public void onRetry()
    {
        Log.i(TAG, "on Retry");
        showProgress();
        if (mAddress != null) {
            mPresenter.loadWeatherData(mAddress);
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
        Log.i(TAG, "check location");

        try {
            Location location = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            //if last known location is already available use this to fetch location
            if (location != null) {
                Log.i(TAG, "Location available");
                onLocationReceived(location);
                return;
            }
            //check if gps and network provider is enabled
            boolean gpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean networkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            Log.i(TAG, "is gps enabled? " + networkEnabled + " Requesting location now");
            if (gpsEnabled && networkEnabled) {
                showProgress();
                showToast(getString(R.string.pleaseWait));
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 500, this);
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 500, this);
            }
            else {
                //prompt to enable location as gps is not enabled
                showPromptToEnableLocation();
            }
        }
        catch (SecurityException e) {
            Log.i(TAG, "Error while getting location" + e);
            showError(Constant.Error.DEFAULT);
        }
    }

    /**
     * show prompt to enabled location
     */
    private void showPromptToEnableLocation()
    {
        // notify user
        showError(Constant.Error.LOCATION_PROVIDER_DISABLED);
        new AlertDialog.Builder(this)
            .setMessage(R.string.gps_network_not_enabled)
            .setPositiveButton(R.string.open_location_settings, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt)
                {
                    //start setting screen to turn on location
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
        onLocationReceived(location);
    }

    /**
     * location received
     * @param location
     */
    private void onLocationReceived(Location location)
    {
        Log.i(TAG, "onLocation Received");
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        mAddress = Utils.getLatLong(latitude, longitude);
        mPresenter.loadWeatherData(mAddress);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
        //TODO
        Log.i(TAG, "Location provider status changed");
    }

    /**
     * called when location provider enabled
     * @param provider
     */
    @Override
    public void onProviderEnabled(String provider)
    {
        Log.i(TAG, "Location provider enabled");
        if (mAddress == null) {
            getLocation();
        }
    }

    /**
     * called when location provider disabled
     * @param provider
     */
    @Override
    public void onProviderDisabled(String provider)
    {
        Log.i(TAG, "Location provider disabled");
        if (mAddress == null) {
            showToast(getString(R.string.enablegps));
            showError(Constant.Error.LOCATION_PROVIDER_DISABLED);
        }
    }

    /**
     * remove location update listener
     */
    @Override
    public void onDestroy()
    {
        Log.i(TAG, "on Destroy");
        mLocationManager.removeUpdates(this);
        super.onDestroy();
        mPresenter.onDestroy();

    }
}
