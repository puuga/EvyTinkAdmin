package com.appspace.evytinkadmin;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.appspace.appspacelibrary.manager.Contextor;
import com.appspace.appspacelibrary.util.LoggerUtils;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by siwaweswongcharoen on 6/5/2016 AD.
 */
public class EvyTinkAdminApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics());

        // Init
        Contextor.getInstance().init(getApplicationContext());

        // Control Log
        LoggerUtils.getInstance().setLogEnabled(true);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
