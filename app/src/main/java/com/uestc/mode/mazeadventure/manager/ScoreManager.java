package com.uestc.mode.mazeadventure.manager;

/**
 * 计分板管理类
 * @author mode
 * date: 2019/12/17
 */
public class ScoreManager {
    private int step;//当前步数
    private int time;//单位秒
    private OnTimerCallback onTimerCallback;

    private TimerManager timerManager;
    public ScoreManager(){
        timerManager = new TimerManager();
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
                    onTimerCallback.onTimerElapsed(time);
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
    }

}
