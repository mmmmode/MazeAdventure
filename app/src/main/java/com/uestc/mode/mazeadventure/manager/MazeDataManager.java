package com.uestc.mode.mazeadventure.manager;

import android.app.Activity;
import android.nfc.Tag;
import android.os.Handler;
import android.util.Log;

import com.uestc.mode.mazeadventure.bean.MazeUnit;
import com.uestc.mode.mazeadventure.callback.OnMazeCallBack;
import com.uestc.mode.mazeadventure.util.VibratorUtil;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/**
 * 数据管理类
 * Created by Administrator on 2017/7/27.
 */

public class MazeDataManager implements OnMazeCallBack{
    public static final String TAG = "modemaze";
    public int width;
    public int height;
    public ArrayList<MazeUnit> mapUnits = new ArrayList<>();//地图坐标点
    public ArrayList<MazeUnit> autoUnits = new ArrayList<>();//自动寻路
    private Stack<MazeUnit> mazeUnitStack = new Stack<>();

    public MazeUnit characterMazeUnit = new MazeUnit();//当前执行单位位置

    Handler handler = new Handler();

    public MazeDataManager(int width, int height) {
        this.width = width;
        this.height = height;
    }

    //**********************生成地图的方法**********************8
    public void generate() {
        //生成一面全是墙的迷宫单元
        for (int i = 0; i < height; i++) {
            for (int j = 0; j <= width - 1; j++) {
                MazeUnit mazeUnit = new MazeUnit();
                mazeUnit.id = j + i * width;//从左到右数id从1开始排序
                mazeUnit.group = j + i * width;
                mazeUnit.x = j;
                mazeUnit.y = i;
                mapUnits.add(mazeUnit);
            }
        }

        //确定起点和终点，先默认为左上角和右下角
        int startId = 1;
        int endId = height * width;
        //随机取两个相邻的点，如果相连，就不处理，如果不相连，就打通这两个点，如果起点和终点打通了，就结束
        Random random = new Random();
        for (; ; ) {
            int id1 = random.nextInt(width * height) + 1;//确定第一个点

            //确定第二个点
            int id2 = -1;
            int mDirect = -1;//确定第二个点在第一个点的位置（左右上下）
            for (; ; ) {
                int tempID = 0;//循环中可能存在的id2
                int direct = random.nextInt(4) + 1;
                switch (direct) {
                    case 1:
                        tempID = id1 - 1;
                        break;
                    case 2:
                        tempID = id1 + 1;
                        break;
                    case 3:
                        tempID = id1 - width;
                        break;
                    case 4:
                        tempID = id1 + width;
                        break;
                }
                //判断是否越界,没有越界就更新id2和方向
                boolean isValid = tempID % width + id1 % width != 1;
                if (tempID <= width * height && tempID > 0 && isValid) {
                    id2 = tempID;
                    mDirect = direct;
                    break;
                }
            }
            //确定了两个点之后，判断是否相连，这时候就需要根据链表来判断了
            boolean isConnected = detectIsConnected(id1, id2, true);
            if (!isConnected) {
                switch (mDirect) {
                    case 1:
                        mapUnits.get(id1 - 1).isLeftWallExist = false;
                        mapUnits.get(id2 - 1).isRightWallExist = false;
                        break;
                    case 2:
                        mapUnits.get(id1 - 1).isRightWallExist = false;
                        mapUnits.get(id2 - 1).isLeftWallExist = false;
                        break;
                    case 3:
                        mapUnits.get(id1 - 1).isTopWallExist = false;
                        mapUnits.get(id2 - 1).isBottomWallExist = false;
                        break;
                    case 4:
                        mapUnits.get(id1 - 1).isBottomWallExist = false;
                        mapUnits.get(id2 - 1).isTopWallExist = false;
                        break;
                }
//                mazeUnits.get(id1-1).son = id2;
//                mazeUnits.get(id2-1).father = id1;
                //判断起点和终点是否相连
                if (detectIsConnected(startId, endId, false)) {
                    break;
                }
            }
        }
        if (mapUnits.size() > 2) {
//            mazeUnits.get(0).isLeftWallExist = false;
            mapUnits.get(mapUnits.size() - 1).isRightWallExist = false;
        }

        if (callback != null)
            callback.onGenerate(true);
    }


    //**自动寻路的方法
    private boolean isFinished;

    //开始自动寻路方法
    public void startAutoStep() {
        isFinished = false;
        if (mapUnits.size() > 0) {
            mazeUnitStack.push(mapUnits.get(0));
            //栈递归
            toNextStep();
        }

        for (; ; ) {
            if (mazeUnitStack.empty()) break;
            mazeUnitStack.add(mazeUnitStack.pop());
        }
        for (int m = 0; m < autoUnits.size(); m++) {
            Log.d(TAG, autoUnits.get(m) + "");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = autoUnits.size() - 1; i >= 0; i--) {
                    characterMazeUnit = autoUnits.get(i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (callback != null)
                        callback.onStepChanged();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null)
                            callback.onToTheEnd();
                    }
                });
            }
        }).start();
    }

    public void controlNextStep(int type) {
        if (currentStep < 0 || currentStep >= mazeUnits.size()) return;
        MazeUnit mazeUnit = mazeUnits.get(currentStep);
        boolean isToWall = false;
        switch (type) {
            case HandControlManager.CONTROL_LEFT:
                if (mazeUnit.isLeftWallExist) {
                    isToWall = true;
                } else
                    currentStep = currentStep - 1;
                break;
            case HandControlManager.CONTROL_RIGHT:
                if (mazeUnit.isRightWallExist) {
                    isToWall = true;
                } else
                    currentStep += 1;
                break;
            case HandControlManager.CONTROL_TOP:
                if (mazeUnit.isTopWallExist) {
                    isToWall = true;
                } else
                    currentStep -= numWidth;
                break;
            case HandControlManager.CONTROL_BOTTOM:
                if (mazeUnit.isBottomWallExist) {
                    isToWall = true;
                } else
                    currentStep += numWidth;
                break;
        }
        if (isToWall)
            VibratorUtil.Vibrate((Activity) context, 100);
        Log.d("modetest", "currentStep:" + currentStep);
        invalidate();
        if (currentStep > mazeUnits.size() - 1) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (callback != null)
                        callback.onToTheEnd();
                }
            });
        }
    }

    private void toNextStep() {
        if (isFinished) return;
        if (mazeUnitStack.peek().id == mapUnits.size() - 1) {
            Log.d(TAG, "成功");
            isFinished = true;
            return;
        }
        Log.d(TAG, mazeUnitStack.peek() + " ");
//        invalidate();
        if (!isFinished && !mazeUnitStack.peek().isLeftWallExist && !mapUnits.get(mazeUnitStack.peek().id - 1).isSearched) {
            mapUnits.get(mazeUnitStack.peek().id - 1).isSearched = true;
            mazeUnitStack.push(mapUnits.get(mazeUnitStack.peek().id - 1));
            Log.d(TAG, "LEFT");
            toNextStep();
        }
        if (!isFinished && !mazeUnitStack.peek().isTopWallExist && !mapUnits.get(mazeUnitStack.peek().id - width).isSearched) {
            mapUnits.get(mazeUnitStack.peek().id - width).isSearched = true;
            mazeUnitStack.push(mapUnits.get(mazeUnitStack.peek().id - width));
            Log.d(TAG, "TOP");
            toNextStep();
        }
        if (!isFinished && !mazeUnitStack.peek().isRightWallExist && !mapUnits.get(mazeUnitStack.peek().id + 1).isSearched) {
            mapUnits.get(mazeUnitStack.peek().id + 1).isSearched = true;
            mazeUnitStack.push(mapUnits.get(mazeUnitStack.peek().id + 1));
            Log.d(TAG, "RIGHT");
            toNextStep();
        }
        if (!isFinished && !mazeUnitStack.peek().isBottomWallExist && !mapUnits.get(mazeUnitStack.peek().id + width).isSearched) {
            mapUnits.get(mazeUnitStack.peek().id + width).isSearched = true;
            mazeUnitStack.push(mapUnits.get(mazeUnitStack.peek().id + width));
            Log.d(TAG, "BOTTOM");
            toNextStep();
        }
        //回溯路线
        if (isFinished) return;
        mazeUnitStack.pop();
    }

    //判断是否相连的方法
    private boolean detectIsConnected(final int id, int id2, boolean isCheck) {
        if (mapUnits.get(id - 1).group == mapUnits.get(id2 - 1).group) return true;
        else {
            if (!isCheck) return false;
            int newGroup = mapUnits.get(id - 1).group;
            int oldGroup = mapUnits.get(id2 - 1).group;
            for (int i = 0; i < mapUnits.size(); i++) {
                if (mapUnits.get(i).group == oldGroup) {
                    mapUnits.get(i).group = newGroup;
                }
            }
        }
        return false;
    }

    /**
     * 当前点和扫描点是否在一个范围内
     *
     * @param currentUnit 当前点
     * @param mazeUnit    扫描点
     *                    TODO 边界问题还没有解决
     */
    private boolean isLightOn(MazeUnit currentUnit, MazeUnit mazeUnit) {
        boolean isIn = false;
        int WIDTH = 2;
//        int result1 = currentStep - id;
//        if(result1 < 0 )result1 = 0-result1;
//        if(result1 <= WIDTH)isIn = true;
//
//        int result2 = currentStep+numWidth - id;
//        if(result2 < 0 )result2 = 0-result2;
//        if(result2 <= WIDTH)isIn = true;
//
//        int result3 = currentStep + numWidth * 2 - id;
//        if(result3 < 0 )result3 = 0-result3;
//        if(result3 <= WIDTH)isIn = true;
//
//        int result4 = currentStep - numWidth - id;
//        if(result4 < 0 )result4 = 0-result4;
//        if(result4 <= WIDTH)isIn = true;
//
//        int result5 = currentStep - numWidth * 2 - id;
//        if(result5 < 0 )result5 = 0 -result5;
//        if(result5 <= WIDTH)isIn = true;
        return isIn;
    }


}
