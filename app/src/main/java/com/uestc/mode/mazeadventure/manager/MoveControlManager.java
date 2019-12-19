package com.uestc.mode.mazeadventure.manager;

import android.app.Activity;

import com.uestc.mode.mazeadventure.bean.MazeUnit;
import com.uestc.mode.mazeadventure.bean.TwoDBean;
import com.uestc.mode.mazeadventure.util.GameParamUtils;
import com.uestc.mode.mazeadventure.util.VibratorUtil;
import com.uestc.mode.mazeadventure.view.MazeView;

/**
 * @author mode
 * date: 2019/12/17
 */
public class MoveControlManager {
    private OnMoveCallback onMoveCallback;
    int stepCount = 0;

    public MoveControlManager(){
    }


    public void controlNextStep(int type) {
        MazeUnit[][] mazeUnits = MazeDataCenter.getInstance().getMazeUnits2D();
        TwoDBean currentStep = MazeDataCenter.getInstance().getCurrenStep();
        if(currentStep == null || mazeUnits == null ||
                currentStep.x > mazeUnits[0].length || currentStep.y > mazeUnits.length
                || currentStep.x < 0 || currentStep.y < 0)return;
            MazeUnit mazeUnit = mazeUnits[currentStep.y][currentStep.x];
            boolean isToWall = false;
            switch (type) {
                case ControlManager.CONTROL_LEFT:
                    if (!mazeUnit.isLeftWallExist) {
                        currentStep.x--;
                        break;
                    } else {
                        isToWall = true;
                        break;
                    }
                case ControlManager.CONTROL_RIGHT:
                    if (!mazeUnit.isRightWallExist) {
                        currentStep.x++;
                        break;
                    } else {
                        isToWall = true;
                        break;
                    }
                case ControlManager.CONTROL_TOP:
                    if (!mazeUnit.isTopWallExist) {
                        currentStep.y --;
                        break;
                    } else {
                        isToWall = true;
                        break;
                    }
                case ControlManager.CONTROL_BOTTOM:
                    if (!mazeUnit.isBottomWallExist) {
                        currentStep.y++;
                        break;
                    } else {
                        isToWall = true;
                        break;
                    }
            }
            if(onMoveCallback != null){
                if(!isToWall)stepCount++;
                onMoveCallback.onMove(!isToWall,currentStep,stepCount);
            }

            //判断是否走完
            if (currentStep.x >=  mazeUnits[0].length -1 && currentStep.y >= mazeUnits.length -1) {
                if (onMoveCallback != null) {
                    onMoveCallback.onToTheEnd();
                }
            }
    }

    public void setOnMoveCallback(OnMoveCallback onMoveCallback) {
        this.onMoveCallback = onMoveCallback;
    }

    public interface OnMoveCallback{
        void onMove(boolean isMoved,TwoDBean twoDBean,int stepCount);
        void onToTheEnd();
    }
}
