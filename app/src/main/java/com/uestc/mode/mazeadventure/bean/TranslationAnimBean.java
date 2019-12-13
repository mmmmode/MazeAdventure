package com.uestc.mode.mazeadventure.bean;

import android.view.animation.TranslateAnimation;

/**
 * 平移动画bean
 * @author mode
 * date: 2019/12/13
 */
public class TranslationAnimBean {
    private float fromX;
    private float fromY;
    private float toX;
    private float toY;
    private long time;
    public TranslationAnimBean(){

    }

    public TranslationAnimBean(float fromX,float fromY,float toX,float toY){
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
    }



    public float getFromX() {
        return fromX;
    }

    public void setFromX(float fromX) {
        this.fromX = fromX;
    }

    public float getFromY() {
        return fromY;
    }

    public void setFromY(float fromY) {
        this.fromY = fromY;
    }

    public float getToX() {
        return toX;
    }

    public void setToX(float toX) {
        this.toX = toX;
    }

    public float getToY() {
        return toY;
    }

    public void setToY(float toY) {
        this.toY = toY;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
