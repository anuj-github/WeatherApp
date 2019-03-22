package com.android.weather.utils;

public class Constant
{
    //public static final String API_KEY = "03197dbd84254f939a6143632191903";
    public static final String FORECAST_URI =
        "https://api.apixu.com/v1/forecast.json?key=03197dbd84254f939a6143632191903&q=";
    public static final String day = "&days=5";
    public static final String CURRENT_URI = "https://api.apixu.com/v1/";
    public interface Error{
        int DEFAULT = 0;
        int INTERNET_NOT_AVAILABLE = 1;
        int LOCATION_PROVIDER_DISABLED =2 ;
    }
}
