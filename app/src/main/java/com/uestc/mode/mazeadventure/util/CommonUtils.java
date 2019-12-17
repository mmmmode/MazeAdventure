package com.uestc.mode.mazeadventure.util;

import android.content.Context;

import com.uestc.mode.mazeadventure.bean.MapBean;

/**
 * @author mode
 * date: 2019/12/11
 */
public class CommonUtils {

    //
    public static int getBiggerZhanZero(int num){
        return num<0?-num:num;
    }

    //是否在指定范围内
    public static boolean isInRange(int currentX,int currentY,int aimX,int aimY, int range) {
        boolean isIn = false;
        int diffX = CommonUtils.getBiggerZhanZero(aimX - currentX);
        int diffY = CommonUtils.getBiggerZhanZero(aimY - currentY);
        if(diffX <= range && diffY <= range)isIn = true;
        return isIn;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
