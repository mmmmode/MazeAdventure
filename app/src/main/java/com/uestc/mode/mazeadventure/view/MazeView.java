package com.uestc.mode.mazeadventure.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.uestc.mode.mazeadventure.manager.HandControlManager;
import com.uestc.mode.mazeadventure.util.VibratorUtil;
import com.uestc.mode.mazeadventure.bean.MazeUnit;
import com.uestc.mode.mazeadventure.manager.MazeDataManager;

import java.util.ArrayList;

/**
 * Created by mode on 2017/7/26.
 */

public class MazeView extends View {

    public static final String TAG = "modetag";
    public static final int POINT_SIZE = 40; //迷宫块点的大小
    int numHeight = 0;//数组高度
    int numWidth = 0;//数组宽度
    boolean isMeasured = false;
    Context context;
    Paint paint1 = new Paint();
    Paint paint2 = new Paint();
    ArrayList<MazeUnit> mazeUnits = new ArrayList<>();//迷宫单元
    Handler handler = new Handler();
    MazeDataManager mazeRandomManager;

    SizeCallback sizeCallback;

    boolean isDifficultMode = false;
    int currentStep = -1;

    public MazeView(Context context) {
        super(context);
        initView(context);
    }

    public MazeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MazeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void isDifficultMode(boolean isDiffcultMode){
        this.isDifficultMode = isDiffcultMode;
    }

    private void initView(Context context) {
        this.context = context;
        setBackgroundColor(Color.BLACK);
        paint1.setColor(Color.WHITE);
        paint1.setAntiAlias(true);
        paint1.setStyle(Paint.Style.STROKE);
        paint2.setColor(Color.YELLOW);
        paint2.setAntiAlias(true);
        paint2.setStyle(Paint.Style.FILL);
    }

    public void setMazeUnits(ArrayList<MazeUnit> mazeUnits) {
        this.mazeUnits = mazeUnits;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredWidth() != 0 && getMeasuredWidth() != 0 && !isMeasured) {
            this.numHeight = getMeasuredHeight() / POINT_SIZE;
            this.numWidth = getMeasuredWidth() / POINT_SIZE;
            isMeasured = true;
            if(sizeCallback != null){
                sizeCallback.onSizeCallback(numWidth,numHeight);
            }
            initPoint();
        }
    }

    private void initPoint() {
        mazeRandomManager = new MazeDataManager(numWidth, numHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mazeUnits.size(); i++) {
            MazeUnit mazeUnit = mazeUnits.get(i);
            if(isDifficultMode){
                if(!onFlashOff(currentStep,mazeUnit))continue;
            }
            Log.d("mdst",mazeUnit.id+"");
            int height = (mazeUnit.id) / numWidth;
            int width = mazeUnit.id + 1 - height * numWidth;
            if (mazeUnit.isLeftWallExist)
                canvas.drawRect(width * POINT_SIZE - POINT_SIZE / 2, height * POINT_SIZE, width * POINT_SIZE - POINT_SIZE / 2 + 2, (height + 1) * POINT_SIZE, paint1);
            if (mazeUnit.isRightWallExist)
                canvas.drawRect((width + 1) * POINT_SIZE - POINT_SIZE / 2, height * POINT_SIZE, (width + 1) * POINT_SIZE - POINT_SIZE / 2 + 2, (height + 1) * POINT_SIZE, paint1);
            if (mazeUnit.isTopWallExist)
                canvas.drawRect(width * POINT_SIZE - POINT_SIZE / 2, height * POINT_SIZE, (width + 1) * POINT_SIZE - POINT_SIZE / 2, height * POINT_SIZE + 2, paint1);
            if (mazeUnit.isBottomWallExist)
                canvas.drawRect(width * POINT_SIZE - POINT_SIZE / 2, (height + 1) * POINT_SIZE, (width + 1) * POINT_SIZE - POINT_SIZE / 2, (height + 1) * POINT_SIZE + 2, paint1);
            if (currentStep == mazeUnit.id)
                canvas.drawCircle((width * POINT_SIZE - POINT_SIZE / 2 + (width + 1) * POINT_SIZE - POINT_SIZE / 2) / 2, (height * POINT_SIZE + (height + 1) * POINT_SIZE) / 2, 10, paint2);
        }

    }

    //外部方法，执行自动寻路命令
    //测算轨迹
    public void autoStep() {
       mazeRandomManager.startAutoStep();
    }

    public void setSizeCallback(SizeCallback sizeCallback) {
        this.sizeCallback = sizeCallback;
    }

    public interface SizeCallback{
        void onSizeCallback(int width,int height);
    }

}
