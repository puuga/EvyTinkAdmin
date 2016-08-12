package com.appspace.evytinkadmin;

import android.app.Application;

import com.appspace.appspacelibrary.manager.Contextor;
import com.appspace.appspacelibrary.util.LoggerUtils;
import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import io.fabric.sdk.android.Fabric;

/**
 * Created by siwaweswongcharoen on 6/5/2016 AD.
 */
public class EvyTinkAdminApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics());

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        // Init
        Contextor.getInstance().init(getApplicationContext());

        // Control Log
        LoggerUtils.getInstance().setLogEnabled(true);
    }
}
