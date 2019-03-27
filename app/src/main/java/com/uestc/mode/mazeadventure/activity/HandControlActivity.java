package com.uestc.mode.mazeadventure.activity;


import android.app.Activity;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import com.uestc.mode.mazeadventure.manager.HandControlManager;
import com.uestc.mode.mazeadventure.view.MazeView;
import com.uestc.mode.mazeadventure.R;

public class HandControlActivity extends Activity implements View.OnClickListener {
    MazeView mazeView;
    Handler handler = new Handler();
    boolean isGenerated = false;
    boolean isRunning = false;
    boolean isEnd = false;
    boolean isDifficultMode = false;
    View girlSmallView;
    View girlBigView;

    HandControlManager controlManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handmaze);
        mazeView = findViewById(R.id.maze_view);
        girlSmallView = findViewById(R.id.small_girl);
        girlBigView = findViewById(R.id.girl_big);
        girlBigView.setVisibility(View.INVISIBLE);
        girlSmallView.setVisibility(View.INVISIBLE);

        findViewById(R.id.generate_maze_action_tv).setVisibility(View.GONE);
        initListener();
        initData();
        isDifficultMode = getIntent().getBooleanExtra("diff",false);
        mazeView.isDifficultMode(isDifficultMode);
    }

    private void initListener() {
        findViewById(R.id.generate_maze_action_tv).setOnClickListener(this);
        findViewById(R.id.generate_maze_tv).setOnClickListener(this);
        findViewById(R.id.exitTv).setOnClickListener(this);
        mazeView.setCallback(new MazeView.Callback() {
            @Override
            public void onGenerate(final boolean generated) {
                isGenerated = generated;
                Toast.makeText(HandControlActivity.this, "星光，懂我，也懂你", Toast.LENGTH_SHORT).show();
                findViewById(R.id.generate_maze_action_tv).setVisibility(View.GONE);
                findViewById(R.id.generate_maze_tv).setVisibility(View.GONE);
                showSmallGirl();
            }

            @Override
            public void onToTheEnd() {
                isRunning = false;
                isGenerated = false;
                Toast.makeText(HandControlActivity.this, "我来了，你在哪？", Toast.LENGTH_SHORT).show();
                dismissSmallGirl();
            }
        });
    }

    public void initData(){
        controlManager = new HandControlManager(this);
        controlManager.setCallback(new HandControlManager.Callback() {
            @Override
            public void controlCallback(int controlType) {
//                switch (controlType){
//                    case HandControlManager.CONTROL_LEFT:
//                        break;
//                    case HandControlManager.CONTROL_RIGHT:
//                        break;
//                    case HandControlManager.CONTROL_BOTTOM:
//                        break;
//                    case HandControlManager.CONTROL_TOP:
//                        break;
//                }
                mazeView.controlNextStep(controlType);
            }
        });
    }
    private void dismissSmallGirl() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1,0);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        girlSmallView.startAnimation(alphaAnimation);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showBigGirl();
            }
        },1000);
    }

    long time = 2000;
    int count = 0;
    int MAX = 3;
    private void showBigGirl(){
        final AlphaAnimation showAnim = new AlphaAnimation(0,1);
        showAnim.setDuration(1000);
        showAnim.setFillAfter(true);
        showAnim.setInterpolator(new AccelerateDecelerateInterpolator());

        final AlphaAnimation dismissAnim = new AlphaAnimation(1,0);
        dismissAnim.setDuration(1000);
        dismissAnim.setFillAfter(true);
        dismissAnim.setInterpolator(new AccelerateDecelerateInterpolator());

        girlBigView.startAnimation(showAnim);
        isEnd = true;
        showAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                girlBigView.startAnimation(dismissAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        dismissAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                count++;
                if(count<=2){
                    girlBigView.startAnimation(showAnim);
                }else {

                    findViewById(R.id.generate_maze_action_tv).setVisibility(View.GONE);
                    findViewById(R.id.generate_maze_tv).setVisibility(View.GONE);
                    findViewById(R.id.exitTv).setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time = time - 200;
            handler.postDelayed(runnable,time);
            AlphaAnimation alphaAnimation;
            if(time <= 1000){
                if(girlBigView.getVisibility() == View.VISIBLE){
                    alphaAnimation = new AlphaAnimation(1,0);
                }
                return;
            }

            if(girlBigView.getVisibility() == View.VISIBLE){
                alphaAnimation = new AlphaAnimation(1,0);
                alphaAnimation.setDuration(200);
                alphaAnimation.setFillAfter(true);
                alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            }else {
                alphaAnimation = new AlphaAnimation(0,1);
                alphaAnimation.setDuration(200);
                alphaAnimation.setFillAfter(true);
                alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            }
            girlBigView.startAnimation(alphaAnimation);
        }
    };
    private void showSmallGirl() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        girlSmallView.startAnimation(alphaAnimation);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.generate_maze_tv:
                if(isEnd)return;
                if (isRunning) return;
                isGenerated = false;
                mazeView.startGenerate();
                Toast.makeText(HandControlActivity.this, "你站在我对侧，却隔着迷宫般银河", Toast.LENGTH_SHORT).show();
                break;
            case R.id.generate_maze_action_tv:
                if(isEnd)return;
                if (isRunning) return;
                if (!isGenerated) {
                    Toast.makeText(HandControlActivity.this, "足迹尚未填满，我亦不知去向", Toast.LENGTH_SHORT).show();
                    return;
                }
                isRunning = true;
                Toast.makeText(HandControlActivity.this, "记忆随风翻过，足迹逐渐清晰", Toast.LENGTH_LONG).show();
                mazeView.autoStep();
                break;
            case R.id.exitTv:
                onBackPressed();
                break;
            default:
                break;
        }
    }
}
