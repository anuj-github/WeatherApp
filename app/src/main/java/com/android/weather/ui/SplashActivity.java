package com.android.weather.ui;

import com.android.weather.R;
import com.android.weather.ui.weather.WeatherActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity
{

    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Spalsh activity created");
        if (!checkPermission()) {
            requestPermission();
        }
        else {
            startWeatherActivity();
        }

    }

    /**
     * check location permission
     *
     * @return
     */
    private boolean checkPermission()
    {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * request for location permission
     */
    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
            PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        Log.i(TAG, "Request code is " + requestCode);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                Log.i(TAG, "Location Accepted? " + locationAccepted);
                if (locationAccepted) {
                    startWeatherActivity();
                }
                else {
                    checkPermissionStatus();
                }
            }
        }
    }

    /**
     * start weather activity
     */
    private void startWeatherActivity()
    {
        Intent intent = new Intent(this, WeatherActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * check/request permission if device is above M
     */
    private void checkPermissionStatus()
    {
        Toast.makeText(this, R.string.permission_error,
            Toast.LENGTH_LONG).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                showMessageOKCancel(R.string.permision_reason, getPermissionDialog());
            }
            else {
                finish();
            }
        }
    }

    /**
     * permission request dialog
     *
     * @return
     */
    private DialogInterface.OnClickListener getPermissionDialog()
    {
        return new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_CODE);
                }
            }
        };
    }

    private void showMessageOKCancel(int message, DialogInterface.OnClickListener okListener)
    {
        new AlertDialog.Builder(SplashActivity.this)
            .setMessage(message)
            .setPositiveButton(R.string.OK, okListener)
            .setNegativeButton(R.string.cancel, null)
            .create()
            .show();
    }

}
