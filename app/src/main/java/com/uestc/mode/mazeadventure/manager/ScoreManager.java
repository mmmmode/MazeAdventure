package com.uestc.mode.mazeadventure.manager;

import android.os.Handler;

/**
 * 计分板管理类
 * @author mode
 * date: 2019/12/17
 */
public class ScoreManager {
    private int step = 0;//当前步数
    private int time;//单位秒
    private OnTimerCallback onTimerCallback;
    Handler handler = new Handler();
    private TimerManager timerManager;
    public ScoreManager(){
        timerManager = new TimerManager();
        setTimerManagerListener();
    }

    public void reset() {
        time = 0;
        step = 0;
        timerManager.stop();
    }

    private void setTimerManagerListener(){
        timerManager.setTimerCallback(new TimerManager.OnTimerCallback() {
            @Override
            public void onTimerElapsed() {
                time++;
                if(onTimerCallback != null){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onTimerCallback.onTimerElapsed(time);
                        }
                    });
                }
            }
        });
    }

    public void start(){
        timerManager.start();
    }

    public void stop(){
        timerManager.stop();
    }

    public int getStep() {
        return step;
    }


    public void setStep(int step) {
        this.step = step;
        if(onTimerCallback!=null){
            onTimerCallback.onStepChanged(step);
        }
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setOnTimerCallback(OnTimerCallback onTimerCallback){
        this.onTimerCallback = onTimerCallback;
    }
    public interface OnTimerCallback{
        void onTimerElapsed(int time);
        void onStepChanged(int step);
    }

}
