package com.uestc.mode.mazeadventure.settingpref;

/**
 * @author mode
 * date: 2019/12/12
 */
public class SettingPrefsUtils {
    //param
    public static final String PARAM_PLAY_SOUND_BGM = "play_sound_bgm";//bgm
    public static final String PARAM_PLAY_VIBRATE = "play_vibrate";//震动
    //user action
    public static final String PARAM_FIRST_LOGIN = "is_first_login";//是否第一次登陆
    public static final String PARAM_LIGHT_MODE_LOCKED = "is_light_mode_locked";//是否光明模式开启
    public static final String PARAM_DARK_MODE_LOCKED = "is_dark_mode_locked";//是否黑暗模式开启

    //game loading
    public static final String HIGH_SCORE = "high_score";//最高分
    public static final String CURREN_STRUGGLE = "curren_struggle";//当前关数

    public class DefaultValue{
        public static final boolean DEFAULT_PLAY_SOUND_BGM = false;//bgm
        public static final boolean DEFAULT_PLAY_VIBRATE = true;//震动
        public static final boolean DEFAULT_FIRST_LOGIN = true;//第一次登陆
        public static final boolean DEFAULT_LIGHT_MODE_LOCKED = true;
        public static final boolean DEFAULT_DARK_MODE_LOCKED = true;
        public static final int DEFAULT_CURREN_STRUGGLE = 0;
    }
}
