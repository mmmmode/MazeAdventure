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
    //----------------------------------------------------------
    //----------------------------------------------------------
    //----------------------------------------------------------
    //--------------------- start ------------------------------
    //----------------------------------------------------------
    //----------------------------------------------------------
    //----------------------------------------------------------

    public void setIsFirstLogin(boolean isFirstLogin){
        mSharedPreferences.edit().putBoolean(PARAM_FIRST_LOGIN,isFirstLogin).apply();
    }

    public boolean isFirstLogin(){
        return mSharedPreferences.getBoolean(PARAM_FIRST_LOGIN,DEFAULT_FIRST_LOGIN);
    }

    //黑暗模式
    public void setDarkModeLocked(boolean locked){
        mSharedPreferences.edit().putBoolean(PARAM_DARK_MODE_LOCKED,locked).apply();
    }

    public boolean isDarkModeLocked(){
        return mSharedPreferences.getBoolean(PARAM_DARK_MODE_LOCKED,DEFAULT_DARK_MODE_LOCKED);
    }

    //光明模式
    public void setLightModeLocked(boolean locked){
        mSharedPreferences.edit().putBoolean(PARAM_LIGHT_MODE_LOCKED,locked).apply();
    }

    public boolean isLightModeLocked(){
        return mSharedPreferences.getBoolean(PARAM_LIGHT_MODE_LOCKED,DEFAULT_LIGHT_MODE_LOCKED);
    }

    //声音打开
    public void setSoundOpened(boolean opened){
        mSharedPreferences.edit().putBoolean(PARAM_PLAY_SOUND_BGM,opened).apply();
    }

    public void isSoundEnabled(){
        mSharedPreferences.getBoolean(PARAM_PLAY_SOUND_BGM,DEFAULT_PLAY_SOUND_BGM);
    }

    //震动打开
    public void setVibratorOpened(boolean opened){
        mSharedPreferences.edit().putBoolean(PARAM_PLAY_VIBRATE,opened).apply();
    }

    public void isVibratorEnabled(){
        mSharedPreferences.getBoolean(PARAM_PLAY_VIBRATE,DEFAULT_PLAY_VIBRATE);
    }

    public void setCurrentCheckPoint(int checkPoint){
        mSharedPreferences.edit().putInt(CURREN_STRUGGLE,checkPoint).apply();
    }

    public int getCurrentCheckouPoint(){
        return mSharedPreferences.getInt(CURREN_STRUGGLE,DEFAULT_CURREN_STRUGGLE);
    }
    //----------------------------------------------------------
    //----------------------------------------------------------
    //----------------------------------------------------------
    //--------------------- end ------------------------------
    //----------------------------------------------------------
    //----------------------------------------------------------
    //----------------------------------------------------------

}
