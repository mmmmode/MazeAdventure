package com.uestc.mode.mazeadventure.util;

import android.util.Log;

/**
 * @author mode
 * date: 2019/12/16
 */
public class MLog {
    private static final String LOG = "mode_log";
    public static void log(String log){
        Log.d(LOG,log);
    }
}
