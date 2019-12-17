package com.uestc.mode.mazeadventure.manager;

import com.uestc.mode.mazeadventure.bean.MapBean;
import com.uestc.mode.mazeadventure.bean.MazeUnit;
import com.uestc.mode.mazeadventure.util.MLog;

import java.util.ArrayList;

/**
 * 迷宫数据中心管理类
 * @author mode
 * date: 2019/12/16
 */
public class MazeDataCenter {
    private MapBean mapBean;//地图bean 定义宽高
    private MazeRandomManager mMazeRandomManager = new MazeRandomManager();//迷宫随机生成类
    private ArrayList<MazeUnit> mMazeUnits1D;//一维迷宫数组
    private MazeUnit[][] mazeUnits2D;//二维迷宫数组

    public MazeDataCenter(){
        mMazeRandomManager = new MazeRandomManager();
    }

    //生成迷宫
    public boolean generateMaze(MapBean mapBean){
        this.mapBean = mapBean;
        mMazeRandomManager.reload(mapBean.cellWidthNum,mapBean.cellHeightNum);
        mMazeUnits1D = mMazeRandomManager.generate();
        mazeUnits2D = translate1DTo2D(mMazeUnits1D);
        if(mazeUnits2D == null)return false;
        return true;
    }

    //1D迷宫转2D
    private MazeUnit[][] translate1DTo2D(ArrayList<MazeUnit> mMazeUnits1D){
        if(mMazeUnits1D.size() != mapBean.cellWidthNum * mapBean.cellHeightNum){
            MLog.log("两者大小不一样，无法转换");
            return null;
        }
        MazeUnit[][] mazeUnits2D = new MazeUnit[mapBean.cellHeightNum][mapBean.cellWidthNum];
        for(int i=0;i<mapBean.cellHeightNum;i++){
            for(int j=0;j<mapBean.cellWidthNum;j++){
                mazeUnits2D[i][j] = mMazeUnits1D.get(i * mapBean.cellWidthNum + j);
            }
        }
        return mazeUnits2D;
    }

    public MazeUnit[][] getMazeUnits2D() {
        return mazeUnits2D;
    }

    private void setMazeUnits2D(MazeUnit[][] mazeUnits2D) {
        this.mazeUnits2D = mazeUnits2D;
    }


}
