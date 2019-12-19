package com.uestc.mode.mazeadventure.manager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author mode
 * date: 2019/12/17
 */
public class TimerManager {
    Timer timer;
    private boolean isStarted;
    private OnTimerCallback onTimerCallback;
    public TimerManager(){
    }

    public void reset(){
        timer.cancel();
        isStarted = false;
    }

    public boolean isStarted(){
        return isStarted;
    }

    public void start(){
        if(isStarted)return;
        isStarted = true;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(onTimerCallback != null){
                    onTimerCallback.onTimerElapsed();
                }
            }
        },0,1000);
    }

    public void stop(){
        isStarted = false;
        try {
            timer.cancel();
            timer = null;
        }catch (Exception e){}
    }

    public void setTimerCallback(OnTimerCallback onTimerCallback){
        this.onTimerCallback = onTimerCallback;
    }

    public interface OnTimerCallback{
        void onTimerElapsed();
    }
}
