
package com.android.weather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Day
{

    @SerializedName("avgtemp_c")
    @Expose
    private Double avgtempC;


    public Double getAvgtempC()
    {
        return avgtempC;
    }

    public void setAvgtempC(Double avgtempC)
    {
        this.avgtempC = avgtempC;
    }



}
