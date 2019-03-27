package com.uestc.mode.mazeadventure.manager;

import android.app.Activity;
import android.os.Handler;

import com.uestc.mode.mazeadventure.R;
import com.uestc.mode.mazeadventure.view.MazeView;


public class MazeOperationManager {
    MazeView mMazeView;
    Activity context;
    MazeDataManager mMazeDataManager;
    Handler handler = new Handler();

    public MazeOperationManager(Activity context) {
        mMazeView = context.findViewById(R.id.maze_view);
        mMazeView.setSizeCallback(new MazeView.SizeCallback() {
            @Override
            public void onSizeCallback(int width, int height) {
                mMazeDataManager = new MazeDataManager(width, height);
                initDataListener();
            }
        });
    }

    private void initDataListener() {
        mMazeDataManager.setCallback(new MazeDataManager.Callback() {
            @Override
            public void onGenerate(boolean isGenerated) {
                if (isGenerated) {
                    //TODO mazeview
                }
            }

            @Override
            public void onToTheEnd() {
                //TODO 到达顶端
            }

            @Override
            public void onStepChanged() {
                //子线程
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });
    }

    public void generate() {
        if (mMazeDataManager != null)
            mMazeDataManager.generate();
    }

}
