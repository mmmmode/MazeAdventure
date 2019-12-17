package com.uestc.mode.mazeadventure.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.uestc.mode.mazeadventure.BuildConfig;
import com.uestc.mode.mazeadventure.bean.RunningManBean;
import com.uestc.mode.mazeadventure.manager.MazeRandomManager;
import com.uestc.mode.mazeadventure.util.CommonUtils;
import com.uestc.mode.mazeadventure.util.GameParamUtils;
import com.uestc.mode.mazeadventure.util.VibratorUtil;
import com.uestc.mode.mazeadventure.bean.MazeUnit;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by mode on 2017/7/26.
 */

public class MazeView extends View {
    public static final int BOTTOM = 4;
    public static final int LEFT = 1;
    public static final int POINT_SIZE = 80;
    public static final int RIGHT = 2;
    public static final String TAG = "modetag";
    public static final int TOP = 3;
    Callback callback;
    Context context;
    int currentStep = -1;
    Handler handler = new Handler();
    Stack<Integer> integerStack = new Stack<>();
    boolean isDifficultMode = false;
    boolean isFinished = false;
    boolean isMeasured = false;
    MazeRandomManager mazeRandomManager;
    ArrayList<MazeUnit> mazeUnits = new ArrayList<>();
    int numHeight = 0;
    int numWidth = 0;
    Paint paint1 = new Paint();
    Paint paint2 = new Paint();
    GameParamUtils mGameParamUtils = new GameParamUtils();

    public interface Callback {
        void onGenerate(boolean z);

        void onToTheEnd();
    }

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

    public void isDifficultMode(boolean isDiffcultMode) {
        this.isDifficultMode = isDiffcultMode;
    }

    private void initView(Context context) {
        this.context = context;
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
        if (getMeasuredWidth() != 0 && getMeasuredWidth() != 0 && !this.isMeasured) {
            this.numHeight = getMeasuredHeight() / POINT_SIZE;
            this.numWidth = getMeasuredWidth() / POINT_SIZE;
            this.isMeasured = true;
            initPoint();
        }
    }

    private void initPoint() {
        mazeRandomManager = new MazeRandomManager();
        mazeRandomManager.reload(this.numWidth, this.numHeight);
    }

    //开始生成地图
    public void startGenerate() {
        if (mazeRandomManager != null) {
            mazeUnits = mazeRandomManager.generate();
            if (mazeUnits.size() > 2) {
                mazeUnits.get(this.mazeUnits.size() - 1).isRightWallExist = false;
            }
            handler.postDelayed(new Runnable() {
                public void run() {
                    MazeView.this.currentStep = 0;
                    MazeView.this.invalidate();
                    if (callback != null) {
                        callback.onGenerate(true);
                    }
                }
            }, 2000);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < this.mazeUnits.size(); i++) {
            MazeUnit mazeUnit = this.mazeUnits.get(i);
            if (!this.isDifficultMode || onFlashOff(this.currentStep + 1, mazeUnit.id)) {
                int height = (mazeUnit.id - 1) / numWidth;
                int width = mazeUnit.id - height * numWidth;
                if (mazeUnit.isLeftWallExist)
                    canvas.drawRect(width * POINT_SIZE - POINT_SIZE / 2, height * POINT_SIZE, width * POINT_SIZE - POINT_SIZE / 2 + 2, (height + 1) * POINT_SIZE, paint1);
                if (mazeUnit.isRightWallExist)
                    canvas.drawRect((width + 1) * POINT_SIZE - POINT_SIZE / 2, height * POINT_SIZE, (width + 1) * POINT_SIZE - POINT_SIZE / 2 + 2, (height + 1) * POINT_SIZE, paint1);
                if (mazeUnit.isTopWallExist)
                    canvas.drawRect(width * POINT_SIZE - POINT_SIZE / 2, height * POINT_SIZE, (width + 1) * POINT_SIZE - POINT_SIZE / 2, height * POINT_SIZE + 2, paint1);
                if (mazeUnit.isBottomWallExist)
                    canvas.drawRect(width * POINT_SIZE - POINT_SIZE / 2, (height + 1) * POINT_SIZE, (width + 1) * POINT_SIZE - POINT_SIZE / 2, (height + 1) * POINT_SIZE + 2, paint1);
                if(currentStep + 1 == mazeUnit.id)
                    canvas.drawCircle((width * POINT_SIZE - POINT_SIZE / 2+(width + 1) * POINT_SIZE - POINT_SIZE / 2)/2,(height * POINT_SIZE+(height + 1) * POINT_SIZE)/2,10,paint2);
            }
        }
    }

    //关灯操作
    private boolean onFlashOff(int currentStep, int id) {
        int range = mGameParamUtils.getRANGE();
        boolean isIn = false;
        int x = id % numWidth ;
        if(x == 0) x = numWidth;
        int y = (id / numWidth) +1 ;
        int currentX = currentStep % numWidth;
        int currentY = (currentStep / numWidth) + 1;
        if(currentX == 0 )currentX = numWidth;
        int diffX = CommonUtils.getBiggerZhanZero(x - currentX);
        int diffY = CommonUtils.getBiggerZhanZero(y - currentY);
        if(diffX <= range && diffY <= range)isIn = true;
        return isIn;
    }

    public void controlNextStep(int type) {
        if (this.currentStep >= 0 && this.currentStep < this.mazeUnits.size()) {
            MazeUnit mazeUnit = (MazeUnit) this.mazeUnits.get(this.currentStep);
            boolean isToWall = false;
            switch (type) {
                case 0:
                    if (!mazeUnit.isLeftWallExist) {
                        this.currentStep--;
                        break;
                    } else {
                        isToWall = true;
                        break;
                    }
                case 1:
                    if (!mazeUnit.isRightWallExist) {
                        this.currentStep++;
                        break;
                    } else {
                        isToWall = true;
                        break;
                    }
                case 2:
                    if (!mazeUnit.isTopWallExist) {
                        this.currentStep -= this.numWidth;
                        break;
                    } else {
                        isToWall = true;
                        break;
                    }
                case 3:
                    if (!mazeUnit.isBottomWallExist) {
                        this.currentStep += this.numWidth;
                        break;
                    } else {
                        isToWall = true;
                        break;
                    }
            }
            if (isToWall && GameParamUtils.isVibrate()) {
                VibratorUtil.Vibrate((Activity) this.context, 100);
            }
            invalidate();
            if (this.currentStep > this.mazeUnits.size() - 1) {
                this.handler.post(new Runnable() {
                    public void run() {
                        if (MazeView.this.callback != null) {
                            MazeView.this.callback.onToTheEnd();
                        }
                    }
                });
            }
        }
    }


    //外部方法，执行自动寻路命令
    //测算轨迹
    public void autoStep() {
        RunningManBean runningManBean = new RunningManBean();
//        runningManBean.currentStep = mazeUnits.get(0).id;
        integerStack.push(mazeUnits.get(0).id + 1);
        isFinished = false;
        toNextStep(RIGHT);
        final ArrayList<Integer> integerArrayList = new ArrayList<>();
        for(;;){
            if(integerStack.empty())break;
            integerArrayList.add(integerStack.pop());
        }
        for(int m=0;m<integerArrayList.size();m++){
            Log.d(TAG,integerArrayList.get(m)+"");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {

                for(int i=integerArrayList.size()-1;i>=0;i--) {
                    currentStep = integerArrayList.get(i);
                    Log.d(TAG,currentStep+" ");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            invalidate();
                        }
                    });
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(callback!=null)
                            callback.onToTheEnd();
                    }
                });
            }
        }).start();

    }

    public void toNextStep(int currentDirect) {
        if (!this.isFinished) {
            if ((Integer) this.integerStack.peek() == this.mazeUnits.size() - 1) {
                Log.d(TAG, "成功");
                this.isFinished = true;
                return;
            }
            String stringBuilder = String.valueOf(this.integerStack.peek()) + " ";
            Log.d(TAG, stringBuilder);
            invalidate();
            if (!(this.isFinished || ((MazeUnit) this.mazeUnits.get((Integer) this.integerStack.peek())).isLeftWallExist || ((MazeUnit) this.mazeUnits.get(((Integer) this.integerStack.peek()).intValue() - 1)).isSearched)) {
                ((MazeUnit) this.mazeUnits.get((Integer) this.integerStack.peek() - 1)).isSearched = true;
                this.integerStack.push((Integer) this.integerStack.peek() - 1);
                Log.d(TAG, "LEFT");
                toNextStep(1);
            }
            if (!(this.isFinished || ((MazeUnit) this.mazeUnits.get((Integer) this.integerStack.peek())).isTopWallExist || ((MazeUnit) this.mazeUnits.get(((Integer) this.integerStack.peek()).intValue() - this.numWidth)).isSearched)) {
                ((MazeUnit) this.mazeUnits.get((Integer) this.integerStack.peek() - this.numWidth)).isSearched = true;
                this.integerStack.push((Integer) this.integerStack.peek() - this.numWidth);
                Log.d(TAG, "TOP");
                toNextStep(3);
            }
            if (!(this.isFinished || ((MazeUnit) this.mazeUnits.get((Integer) this.integerStack.peek())).isRightWallExist || ((MazeUnit) this.mazeUnits.get(((Integer) this.integerStack.peek()).intValue() + 1)).isSearched)) {
                ((MazeUnit) this.mazeUnits.get((Integer) this.integerStack.peek() + 1)).isSearched = true;
                this.integerStack.push((Integer) this.integerStack.peek() + 1);
                Log.d(TAG, "RIGHT");
                toNextStep(2);
            }
            if (!(this.isFinished || ((MazeUnit) this.mazeUnits.get((Integer) this.integerStack.peek())).isBottomWallExist || ((MazeUnit) this.mazeUnits.get(((Integer) this.integerStack.peek()).intValue() + this.numWidth)).isSearched)) {
                ((MazeUnit) this.mazeUnits.get((Integer) this.integerStack.peek() + this.numWidth)).isSearched = true;
                this.integerStack.push((Integer) this.integerStack.peek() + this.numWidth);
                Log.d(TAG, "BOTTOM");
                toNextStep(4);
            }
            if (!this.isFinished) {
                this.integerStack.pop();
            }
        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}