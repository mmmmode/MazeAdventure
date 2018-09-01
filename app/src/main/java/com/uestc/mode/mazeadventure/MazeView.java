package com.uestc.mode.mazeadventure;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.support.annotation.InterpolatorRes;
import android.support.annotation.Nullable;
import android.support.v7.view.menu.MenuAdapter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Stack;

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
    ArrayList<MazeUnit> mazeUnits = new ArrayList<>();
    Handler handler = new Handler();
    MazeRandomManager mazeRandomManager;
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredWidth() != 0 && getMeasuredWidth() != 0 && !isMeasured) {
            this.numHeight = getMeasuredHeight() / POINT_SIZE;
            this.numWidth = getMeasuredWidth() / POINT_SIZE;
            isMeasured = true;
            initPoint();
        }
    }

    private void initPoint() {
        mazeRandomManager = new MazeRandomManager(numWidth, numHeight);
    }

    //外部方法，开始生成迷宫
    public void startGenerate(){
        if(mazeRandomManager == null)return;
        mazeUnits = mazeRandomManager.generate();
        if (mazeUnits.size() > 2) {
//            mazeUnits.get(0).isLeftWallExist = false;
            mazeUnits.get(mazeUnits.size() - 1).isRightWallExist = false;
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                currentStep = 0;
                invalidate();
                if(callback!=null)
                    callback.onGenerate(true);
            }
        },3000);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mazeUnits.size(); i++) {
            MazeUnit mazeUnit = mazeUnits.get(i);
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

    RunningManBean runningManBean;

    public void controlNextStep(int type){
        if(currentStep<0 || currentStep >= mazeUnits.size() )return;
        MazeUnit mazeUnit = mazeUnits.get(currentStep);
        boolean isToWall = false;
        switch (type){
            case ControlManager.CONTROL_LEFT:
                if(mazeUnit.isLeftWallExist){
                    isToWall = true;
                }else
                currentStep = currentStep - 1;
                break;
            case ControlManager.CONTROL_RIGHT:
                if(mazeUnit.isRightWallExist){
                    isToWall = true;
                }else
                currentStep += 1;
                break;
            case ControlManager.CONTROL_TOP:
                if(mazeUnit.isTopWallExist){
                    isToWall = true;
                }else
                currentStep -= numWidth;
                 break;
            case ControlManager.CONTROL_BOTTOM:
                if(mazeUnit.isBottomWallExist){
                    isToWall = true;
                }else
                currentStep += numWidth;
                break;
        }
        if(isToWall)
            VibratorUtil.Vibrate((Activity)context,100);
        Log.d("modetest","currentStep:"+currentStep);
        invalidate();
        if(currentStep > mazeUnits.size()-1){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(callback!=null)
                        callback.onToTheEnd();
                }
            });
        }
    }

    //外部方法，执行自动寻路命令
    //测算轨迹
    public void autoStep() {
        runningManBean = new RunningManBean();
//        runningManBean.currentStep = mazeUnits.get(0).id;
        integerStack.push(mazeUnits.get(0).id);
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
                        currentStep = integerArrayList.get(i)+1;
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

    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int TOP = 3;
    public static final int BOTTOM = 4;
    Stack<Integer> integerStack = new Stack<>();
    boolean isFinished = false;
    public void toNextStep(int currentDirect) {
        if(isFinished)return;
        if (integerStack.peek() == mazeUnits.size()) {
            Log.d(TAG, "成功");
            isFinished = true;
            return;
        }
        Log.d(TAG, integerStack.peek()+" ");
        invalidate();
        if (!isFinished &&!mazeUnits.get(integerStack.peek() - 1).isLeftWallExist && !mazeUnits.get(integerStack.peek() - 1 - 1).isSearched) {
            mazeUnits.get(integerStack.peek() - 1 - 1).isSearched = true;
            integerStack.push(integerStack.peek() - 1);
            Log.d(TAG, "LEFT");
            toNextStep(LEFT);
        }
        if (!isFinished &&!mazeUnits.get(integerStack.peek() - 1).isTopWallExist && !mazeUnits.get(integerStack.peek() - 1 - numWidth).isSearched) {
            mazeUnits.get(integerStack.peek() - 1 - numWidth).isSearched = true;
            integerStack.push(integerStack.peek() - numWidth);
            Log.d(TAG, "TOP");
            toNextStep(TOP);
        }
        if (!isFinished &&!mazeUnits.get(integerStack.peek() - 1).isRightWallExist && !mazeUnits.get(integerStack.peek() - 1 + 1).isSearched) {
            mazeUnits.get(integerStack.peek() - 1 + 1).isSearched = true;
            integerStack.push(integerStack.peek() + 1);
            Log.d(TAG, "RIGHT");
            toNextStep(RIGHT);
        }
        if (!isFinished &&!mazeUnits.get(integerStack.peek() - 1).isBottomWallExist && !mazeUnits.get(integerStack.peek() - 1 + numWidth).isSearched) {
            mazeUnits.get(integerStack.peek() - 1 + numWidth).isSearched = true;
            integerStack.push(integerStack.peek() + numWidth);
            Log.d(TAG, "BOTTOM");
            toNextStep(BOTTOM);
        }
        //回溯路线
        if(isFinished)return;
        integerStack.pop();
    }

    Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface  Callback{
        void onGenerate(boolean isGenerated);
        void onToTheEnd();
    }

}
