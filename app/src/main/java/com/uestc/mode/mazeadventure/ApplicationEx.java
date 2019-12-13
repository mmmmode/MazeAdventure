package com.uestc.mode.mazeadventure;

import android.app.Application;

/**
 * @author mode
 * date: 2019/12/12
 */
public class ApplicationEx extends Application {
    static ApplicationEx applicationEx;

    public static ApplicationEx getApplicationEx() {
        return applicationEx;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationEx = this;
    }
}
