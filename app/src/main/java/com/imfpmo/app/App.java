package com.imfpmo.app;

import android.app.Application;

import com.bugfender.sdk.Bugfender;

public class App extends Application {
    // TODO: 8/10/19 Remove Bugfender logging
    @Override
    public void onCreate() {
        super.onCreate();

        //external api used only for testing purposes. To be removed
        Bugfender.init(this, "JAPF4gBVPzvowURq1obVtUxMIzdaAtlH", BuildConfig.DEBUG);
        Bugfender.enableCrashReporting();
        Bugfender.enableUIEventLogging(this);
        Bugfender.enableLogcatLogging();
    }
}
