package com.uestc.mode.mazeadventure.util;

/**
 * @author mode
 * date: 2019/12/11
 */
public class GameParamUtils {
    private static int RANGE =2;//开灯范围
    private static int MODE = 0;
    private static boolean isVibrate = true;
    private static boolean isSoundOpen = false;

    public static int getRANGE() {//获取开灯范围
        return RANGE;
    }

    public static void setRANGE(int range) {//设置开灯范围
        RANGE = range;
    }

    public static boolean isVibrate() {
        return isVibrate;
    }

    public static void setVibrate(boolean vibrate) {
        isVibrate = vibrate;
    }

    public static boolean isIsSoundOpen() {
        return isSoundOpen;
    }

    public static void setIsSoundOpen(boolean isSoundOpen) {
        GameParamUtils.isSoundOpen = isSoundOpen;
    }
}
