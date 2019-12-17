package com.uestc.mode.mazeadventure.bean;

/**
 * @author mode
 * date: 2019/12/16
 */
public class MapBean {
    public int cellWidthNum;
    public int cellHeightNum;
    public MapBean(int cellWidthNum,int cellHeightNum){
        this.cellHeightNum = cellHeightNum;
        this.cellWidthNum = cellWidthNum;
    }
}
