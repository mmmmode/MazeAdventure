package com.uestc.mode.mazeadventure.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.InputDeviceCompat;
import android.util.AttributeSet;
import android.view.View;

import com.uestc.mode.mazeadventure.bean.MapBean;
import com.uestc.mode.mazeadventure.bean.MazeUnit;
import com.uestc.mode.mazeadventure.manager.MazeDataCenter;
import com.uestc.mode.mazeadventure.util.CommonUtils;
import com.uestc.mode.mazeadventure.util.GameParamUtils;
import com.uestc.mode.mazeadventure.util.MLog;

/**
 * @author mode
 * date: 2019/12/16
 */
public class MapView extends View {

    Paint paint1 = new Paint();
    Paint paint2 = new Paint();
    Context context;
    int cellWidthNum = 0;//单元格宽总数
    int cellHeightNum = 0;//单元格高总数
    private boolean isMeasured = false;//是否绘制了宽高

    public MapView(Context context) {
        super(context);
        initView(context);
    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        this.context = context;
        initPaint();
    }

    private void initPaint(){
        this.paint1.setColor(-1);
        this.paint1.setAntiAlias(true);
        this.paint1.setStyle(Paint.Style.STROKE);
        this.paint2.setColor(InputDeviceCompat.SOURCE_ANY);
        this.paint2.setAntiAlias(true);
        this.paint2.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(isMeasured)return;
        if(getMeasuredWidth() != 0 && getMeasuredHeight() != 0){
            isMeasured = true;
            initCellNum();
        }
    }

    //初始化格子宽高
    private void initCellNum(){
        cellHeightNum = getMeasuredHeight() / MazeDataCenter.getInstance().getGameParamUtils().getCellSize();
        cellWidthNum = getMeasuredWidth() /  MazeDataCenter.getInstance().getGameParamUtils().getCellSize();
    }

    public MapBean getMapBean(){
        if(!isMeasured)return null;
        return new MapBean(cellWidthNum,cellHeightNum);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int POINT_SIZE =  MazeDataCenter.getInstance().getGameParamUtils().getCellSize();
        MazeUnit[][] mazeUnits = MazeDataCenter.getInstance().getMazeUnits2D();
        if(mazeUnits == null)return;
        for(int currenCellHeight=0;currenCellHeight<mazeUnits.length;currenCellHeight++){
            for(int currentCellWidth=0;currentCellWidth<mazeUnits[currenCellHeight].length;currentCellWidth++){
                MazeUnit mazeUnit = mazeUnits[currenCellHeight][currentCellWidth];
                if(currenCellHeight == 0 && currentCellWidth == 0){
                    MLog.log((currentCellWidth * POINT_SIZE + POINT_SIZE / 2)+":"+currenCellHeight * POINT_SIZE);
                }
//                if (gameParamUtils.getMODE() == GameParamUtils.DARK_MODE || CommonUtils.isInRange(this.currentStep + 1, mazeUnit.id)){
                    if (mazeUnit.isLeftWallExist)
                        canvas.drawRect(currentCellWidth * POINT_SIZE + POINT_SIZE / 2, currenCellHeight * POINT_SIZE, currentCellWidth * POINT_SIZE + POINT_SIZE / 2 + 2, (currenCellHeight + 1) * POINT_SIZE, paint1);
                    if (mazeUnit.isRightWallExist)
                        canvas.drawRect((currentCellWidth + 1) * POINT_SIZE + POINT_SIZE / 2, currenCellHeight * POINT_SIZE, (currentCellWidth + 1) * POINT_SIZE + POINT_SIZE / 2 + 2, (currenCellHeight + 1) * POINT_SIZE, paint1);
                    if (mazeUnit.isTopWallExist)
                        canvas.drawRect(currentCellWidth * POINT_SIZE + POINT_SIZE / 2, currenCellHeight * POINT_SIZE, (currentCellWidth + 1) * POINT_SIZE + POINT_SIZE / 2, currenCellHeight * POINT_SIZE + 2, paint1);
                    if (mazeUnit.isBottomWallExist)
                        canvas.drawRect(currentCellWidth * POINT_SIZE + POINT_SIZE / 2, (currenCellHeight + 1) * POINT_SIZE, (currentCellWidth + 1) * POINT_SIZE + POINT_SIZE / 2, (currenCellHeight + 1) * POINT_SIZE + 2, paint1);

            }
        }

    }

}
