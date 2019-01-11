package com.pipnet.wallenews;

import android.app.Application;

/**
 * Created by LeeBoo on 2018/3/30.
 */
public class App extends Application {

    private static App instance;

    public static synchronized App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
