package com.uestc.mode.mazeadventure.util;

/**
 * @author mode
 * date: 2019/12/11
 */
public class GameParamUtils {
    public static final String SET_MODE = "game_mode";
    public static final int TEACH_MODE = 0;
    public static final int LIGHT_MODE = 1;
    public static final int DARK_MODE = 2;
    private int RANGE =2;//开灯范围
    private int MODE = 0;//游戏模式
    private int checkoutpoint = 1;//关卡数
    private int cellSize = 80;//格子大小
    private static boolean isVibrate = true;
    private static boolean isSoundOpen = false;

    public int getRANGE() {//获取开灯范围
        return RANGE;
    }

    public void setRANGE(int range) {//设置开灯范围
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

    public int getMODE() {
        return MODE;
    }

    public void setMODE(int MODE) {
        this.MODE = MODE;
    }

    public int getCellSize() {
        return cellSize;
    }

    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
    }
}
