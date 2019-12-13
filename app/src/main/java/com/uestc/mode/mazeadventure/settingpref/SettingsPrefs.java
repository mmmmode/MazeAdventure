package com.uestc.mode.mazeadventure.settingpref;

import android.content.Context;
import android.content.SharedPreferences;

import com.uestc.mode.mazeadventure.ApplicationEx;

import com.uestc.mode.mazeadventure.settingpref.SettingPrefsUtils.DefaultValue;

import static com.uestc.mode.mazeadventure.settingpref.SettingPrefsUtils.DefaultValue.*;

/**
 * @author mode
 * date: 2019/12/12
 */
public class SettingsPrefs extends SettingPrefsUtils{
    private static final String NAME = "shared_name";
    private static SettingsPrefs instance;
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    public static SettingsPrefs getInstance(){
        if(instance == null)
        synchronized (SettingsPrefs.class){
            if(instance == null){
                instance = new SettingsPrefs();
            }
        }
        return instance;
    }

    private SettingsPrefs(){
        mContext = ApplicationEx.getApplicationEx();
        mSharedPreferences = mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
    }

    public void setIsFirstLogin(boolean isFirstLogin){
        mSharedPreferences.edit().putBoolean(IS_FIRST_LOGIN,isFirstLogin).apply();
    }

    public boolean isFirstLogin(){
        return mSharedPreferences.getBoolean(IS_FIRST_LOGIN,DEFAULT_FIRST_LOGIN);
    }
}
