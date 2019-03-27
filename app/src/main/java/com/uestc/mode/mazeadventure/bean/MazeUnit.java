package com.uestc.mode.mazeadventure.bean;

/**
 * Created by Administrator on 2017/7/27.
 */

public class MazeUnit {
    public int id;
    public int x;//x坐标
    public int y;//y坐标
    public boolean isLeftWallExist = true;
    public boolean isRightWallExist = true;
    public boolean isTopWallExist = true;
    public boolean isBottomWallExist = true;
    public int group;
    public boolean isSearched = false;
}
