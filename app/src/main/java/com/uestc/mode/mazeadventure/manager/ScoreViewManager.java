package com.uestc.mode.mazeadventure.manager;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.uestc.mode.mazeadventure.R;

/**
 * @author mode
 * date: 2019/12/18
 */
public class ScoreViewManager {
    Activity mContext;
    View root;
    TextView mCheckpointTv;
    TextView mStepTv;
    TextView mTimeTv;
    public ScoreViewManager(Activity context){
        this.mContext = context;
        init();
        refreshData();
        initListener();
    }

    private void refreshData() {
        setmCheckpointTv(MazeDataCenter.getInstance().getCheckPoint());
        setmStepTv(MazeDataCenter.getInstance().getStepCount());
        setmTimeTv(MazeDataCenter.getInstance().getTime());
    }

    private void initListener() {
        MazeDataCenter.getInstance().bindScoreListener(new MazeDataCenter.ScoreListener() {
            @Override
            public void onStep(int step) {
                setmStepTv(step);
            }

            @Override
            public void onTime(int time) {
                setmTimeTv(time);
            }

            @Override
            public void onCheckpoint(int checkpoint) {
                setmCheckpointTv(checkpoint);
            }
        });
    }

    private void init() {
        root = mContext.findViewById(R.id.score_head);
        mCheckpointTv = mContext.findViewById(R.id.tv_current_guanka);
        mStepTv = mContext.findViewById(R.id.tv_current_step);
        mTimeTv = mContext.findViewById(R.id.tv_current_time);
    }

    private void setmCheckpointTv(int checkpoint){
        mCheckpointTv.setText(mContext.getResources().getString(R.string.checkoutpoint,checkpoint));
    }

    private void setmStepTv(int step){
        mStepTv.setText(mContext.getResources().getString(R.string.currentStep,step));
    }

    private void setmTimeTv(int time){
        mTimeTv.setText(mContext.getResources().getString(R.string.currentTime,time));
    }

}
