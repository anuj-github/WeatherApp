
package com.android.weather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Current
{

    @SerializedName("temp_c")
    @Expose
    private Double tempC;

    public Double getTempC()
    {
        return tempC;
    }

    public void setTempC(Double tempC)
    {
        this.tempC = tempC;
    }

}
