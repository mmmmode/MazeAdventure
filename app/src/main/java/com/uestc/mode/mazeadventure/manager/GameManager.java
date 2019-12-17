package com.uestc.mode.mazeadventure.manager;

import android.app.Activity;
import android.os.Handler;

import com.uestc.mode.mazeadventure.R;
import com.uestc.mode.mazeadventure.bean.TwoDBean;
import com.uestc.mode.mazeadventure.util.GameParamUtils;
import com.uestc.mode.mazeadventure.util.VibratorUtil;
import com.uestc.mode.mazeadventure.view.MapLayout;

/**
 * 总控manager 在activity中初始化
 *
 * @author mode
 * date: 2019/12/16
 */
public class GameManager {
    private Activity mContext;
    private Handler handler = new Handler();

    private MapLayout mMapLayout;
    //控制类
    private ControlManager mControlManager;//手柄控制类
    private MazeDataCenter mMazeDataCenter;//数据中心类
    private MoveControlManager mMoveControlManager;//移动控制类

    private boolean isGenerated = false;
    private boolean isGameStarted = false;
    public boolean isGameEnd = false;

    public GameManager(Activity context) {
        this.mContext = context;
    }

    public void init() {
        mMapLayout = mContext.findViewById(R.id.map_layout);
        initManager();
        setControlListener();
    }

    //设置游戏参数
    private void reloadGameParamUtils(){
    }

    private void initManager() {
        mControlManager = new ControlManager(mContext);
        mMazeDataCenter = MazeDataCenter.getInstance();
        mMoveControlManager = new MoveControlManager();
    }

    private void setControlListener() {
        mControlManager.setCallback(new ControlManager.Callback() {
            @Override
            public void controlCallback(int controlType) {
                if (!isGameStarted) return;
                mMoveControlManager.controlNextStep(controlType);
            }
        });

        mMoveControlManager.setOnMoveCallback(new MoveControlManager.OnMoveCallback() {
            @Override
            public void onMove(boolean isMoved, TwoDBean currenStep) {
                if (!isMoved) {
                    if (GameParamUtils.isVibrate()) {
                        VibratorUtil.Vibrate((Activity) mContext, 100);
                    }
                }else {
                    MazeDataCenter.getInstance().startTimer();
                    MazeDataCenter.getInstance().setCurrenStep(currenStep);
                    mMapLayout.changeCurrentPosition();
                }
            }

            @Override
            public void onToTheEnd() {
                MazeDataCenter.getInstance().stopTimer();
            }
        });
    }

    //生成地图
    public boolean generateMaze() {
        boolean isSuccess = mMazeDataCenter.generateMaze(mMapLayout.mMapView.getMapBean());
        isGenerated = isSuccess;
        delayStartGame();
        return isSuccess;
    }

    private void delayStartGame(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isGameStarted = true;
                mMapLayout.refreshMapView();
            }
        },2000);
    }

    public void setGameMode(int mGameMode) {
    }
}
