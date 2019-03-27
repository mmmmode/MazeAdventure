package com.uestc.mode.mazeadventure.callback;

public interface OnMazeCallBack {
    void onGenerate(boolean isGenerated);//是否生成成功

    void onToTheEnd();//进入了新的结界

    void onStepChanged();//当前步数发生了改变
}
