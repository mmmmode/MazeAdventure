package com.uestc.mode.mazeadventure.util;

import android.widget.Toast;

import com.uestc.mode.mazeadventure.ApplicationEx;

/**
 * @author mode
 * date: 2019/12/17
 */
public class ToastUtils {

    public static void showShortToast(String toast){
        Toast.makeText(ApplicationEx.getApplicationEx(),toast,Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(String toast){
        Toast.makeText(ApplicationEx.getApplicationEx(),toast,Toast.LENGTH_LONG).show();
    }
}
