package com.uestc.mode.mazeadventure.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.uestc.mode.mazeadventure.ApplicationEx;
import com.uestc.mode.mazeadventure.R;

/**
 * 控制弹窗弹出的类
 * @author mode
 * date: 2019/12/13
 */
public class DialogManager {

    public DialogManager(){
    }


    public static void showMDialog(Activity context, final OnDialogClickListener onDialogClickListener) {
        new  AlertDialog.Builder(context)
                .setIcon(R.drawable.girl1)
                .setTitle("是否打开音效?")
                .setCancelable(false)
                .setMessage("打开音效可以更加沉浸式体验游戏乐趣")
                .setPositiveButton("听宁的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onDialogClickListener.onPositiveButtonClick();
                    }
                })
                .setNegativeButton("室友睡了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public static class OnDialogClickListener{

        public void onPositiveButtonClick(){
        }

        public void onNagetiveButtonClick(){

        }
    }
}
