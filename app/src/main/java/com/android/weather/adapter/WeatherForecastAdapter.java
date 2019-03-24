package com.android.weather.adapter;

import java.util.ArrayList;
import java.util.List;

import com.android.weather.R;
import com.android.weather.model.Forecastday;
import com.android.weather.utils.Utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * adapter class to show forecast weather
 */
public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherHolder>
{
    List<Forecastday> forecastdayList;

    public WeatherForecastAdapter()
    {
        forecastdayList = new ArrayList<>();
    }

    public void setForecastdayList(List<Forecastday> forecastdayList)
    {
        this.forecastdayList = forecastdayList;
    }

    @NonNull
    @Override
    public WeatherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View viewItem = inflater.inflate(R.layout.weather_forecast_day, parent, false);
        WeatherHolder viewHolder = new WeatherHolder(viewItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherHolder holder, int position)
    {
        final Forecastday forecastday = forecastdayList.get(position);
        holder.dayView.setText(Utils.getDay(forecastday.getDate()));
        holder.dayTemp.setText(Utils.getTemp(forecastday.getDay().getAvgtempC()));

    }

    @Override
    public int getItemCount()
    {
        return forecastdayList == null ? 0 : forecastdayList.size();
    }
}

/**
 * holder class to hold views of forecast weather
 */
class WeatherHolder extends RecyclerView.ViewHolder
{
    @BindView(R.id.forecast_day)
    public TextView dayView;
    @BindView(R.id.forecast_temp)
    public TextView dayTemp;

    public WeatherHolder(@NonNull View itemView)
    {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
