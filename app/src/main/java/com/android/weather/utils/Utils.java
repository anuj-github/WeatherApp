package com.android.weather.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils
{
    /**
     * get day from date for ex: if given date 2019-03-20 will return Wednesday
     *
     * @param dateString input date
     * @return day string
     */
    public static String getDay(String dateString)
    {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
            SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE"); // the day of the week spelled out
            // completely
            return simpleDateformat.format(date);
        }
        catch (ParseException e) {
            e.printStackTrace();
            return "Invalid Day";
        }
    }

    /**
     * convert a double temp value to string temp
     *
     * @param temp input temp
     * @return
     */
    public static String getTemp(Double temp)
    {
        String res = String.valueOf(temp);
        String str[] = res.split("\\.");
        return str[0] + (char) 0x00B0 + "C";
    }

    /**
     * get lat long
     *
     * @param latitude
     * @param longitude
     * @return
     */
    public static String getLatLong(double latitude, double longitude)
    {
        return latitude + "," + longitude;
    }

    /**
     * This method take the application context and check on the internet connectivity.
     * if the device connected with the internet! the method will return true, else it will return false.
     *
     * @param context application context.
     * @return boolean true if internet connected, false if it not connected.
     */
    public static boolean checkInternetConnection(Context context)
    {
        ConnectivityManager connectivityManager =

            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
