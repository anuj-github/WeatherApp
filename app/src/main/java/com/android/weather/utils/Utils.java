package com.android.weather.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
     * @param temp input temp
     * @return
     */
    public static String getTemp(Double temp)
    {
        String res = String.valueOf(temp);
        String str[] = res.split("\\.");
        return str[0] + " C";
    }

}
