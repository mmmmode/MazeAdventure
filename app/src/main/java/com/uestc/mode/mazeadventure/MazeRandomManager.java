package com.uestc.mode.mazeadventure;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 2017/7/27.
 */

public class MazeRandomManager {
    public int width;
    public int height;
    ArrayList<MazeUnit> mazeUnits = null;

    public MazeRandomManager(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public ArrayList<MazeUnit> generate() {
        mazeUnits = new ArrayList<>();//初始化一个有序的迷宫单元集合
        //生成一面全是墙的迷宫单元
        for (int i = 0; i < height; i++) {
            for (int j = 1; j <= width; j++) {
                MazeUnit mazeUnit = new MazeUnit();
                mazeUnit.id = j + i * width;//从左到右数id从1开始排序
                mazeUnit.group = j+i*width;
                mazeUnits.add(mazeUnit);
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
                boolean isValid = tempID%width+id1%width != 1;
                if (tempID <= width * height && tempID > 0 && isValid) {
                    id2 = tempID;
                    mDirect = direct;
                    break;
                }
            }
            Log.d("qwerqqqq",id1+":"+id2);
            //确定了两个点之后，判断是否相连，这时候就需要根据链表来判断了
            boolean isConnected = detectIsConnected(id1, id2, true);
            Log.d("qwerqq", "qwer:" + isConnected);
            if (!isConnected) {
                switch (mDirect) {
                    case 1:
                        mazeUnits.get(id1 - 1).isLeftWallExist = false;
                        mazeUnits.get(id2 - 1).isRightWallExist = false;
                        break;
                    case 2:
                        mazeUnits.get(id1 - 1).isRightWallExist = false;
                        mazeUnits.get(id2 - 1).isLeftWallExist = false;
                        break;
                    case 3:
                        mazeUnits.get(id1 - 1).isTopWallExist = false;
                        mazeUnits.get(id2 - 1).isBottomWallExist = false;
                        break;
                    case 4:
                        mazeUnits.get(id1 - 1).isBottomWallExist = false;
                        mazeUnits.get(id2 - 1).isTopWallExist = false;
                        break;
                }
//                mazeUnits.get(id1-1).son = id2;
//                mazeUnits.get(id2-1).father = id1;
                //判断起点和终点是否相连
                if (detectIsConnected(startId, endId, false)) {
                    Log.d("qqwwe","qqwwe");
                    break;
                }
            }
        }
        return mazeUnits;
    }


    //判断是否相连的方法
    private boolean detectIsConnected(final int id, int id2, boolean isCheck) {
        if(mazeUnits.get(id-1).group==mazeUnits.get(id2-1).group)return true;
        else {
            if(!isCheck)return false;
            int newGroup = mazeUnits.get(id-1).group;
            int oldGroup = mazeUnits.get(id2-1).group;
            for (int i=0;i<mazeUnits.size();i++){
                if(mazeUnits.get(i).group == oldGroup){
                    mazeUnits.get(i).group = newGroup;
                }
            }
        }
        return false;
    }

}
