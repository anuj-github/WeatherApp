package com.android.weather;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application
{
    public static MyApplication application;

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        application = this;

    }

    public static MyApplication getApplication()
    {
        return application;
    }
}
