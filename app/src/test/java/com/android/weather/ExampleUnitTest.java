package com.android.weather;

import org.junit.Test;

import com.android.weather.utils.Utils;

import static org.junit.Assert.*;

/**
 * ApiResponse local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest
{
    @Test
    public void testWenesday()
    {
        String millis = "2019-03-20";
        String res = Utils.getDay(millis);
        assertNotNull(res);
        assertEquals("Wednesday", res);
    }

    @Test
    public void testSunday()
    {
        String millis = "2019-03-24";
        String res = Utils.getDay(millis);
        assertNotNull(res);
        assertEquals("Sunday", res);
    }

    @Test
    public void testTemp()
    {
        //test with decimal
        double temp = 29.4;
        String res = Utils.getTemp(temp);
        assertNotNull(res);
        assertEquals("29 C", res);
        //test without decimal
        double d = 28;
        res = Utils.getTemp(d);
        assertNotNull(res);
        assertEquals("28 C", res);
    }
}