package com.uestc.mode.mazeadventure.dialog;

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

    public static void onBackPressDialog(Activity context,final OnDialogClickListener onDialogClickListener){
        new  AlertDialog.Builder(context)
                .setIcon(R.drawable.girl1)
                .setTitle("嗯?公主不救了？")
                .setCancelable(true)
                .setMessage("公主还在魔王城堡默默等待宁的解救....")
                .setPositiveButton("emm...那啥，我点错了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onDialogClickListener.onPositiveButtonClick();
                    }
                })
                .setNegativeButton("要啥公主啊，烂迷宫.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onDialogClickListener.onNagetiveButtonClick();
                    }
                }).show();
    }

    public static class OnDialogClickListener{

        public void onPositiveButtonClick(){
        }

        public void onNagetiveButtonClick(){

        }
    }

    public static void showBeijingDialog(Activity context, final OnDialogClickListener onDialogClickListener) {
        String msg = "某天，公主带领她的贴身侍卫出门买水晶鞋，突然间，天空一片漆黑，王族世敌奥利给大魔王出现了！！！他带走了公主，此时我们的主人公追了出去，结果误入迷宫...等待他的会是什么呢？他能打败最终的大魔王吗，敬请期待（代码还没写完，我也不知道结局）";
        new  AlertDialog.Builder(context)
                .setIcon(R.drawable.girl1)
                .setTitle("当然我在扯淡...")
                .setCancelable(false)
                .setMessage(msg)
                .setPositiveButton("哦。", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

}
