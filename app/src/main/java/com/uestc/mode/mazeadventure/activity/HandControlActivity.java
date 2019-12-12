package com.uestc.mode.mazeadventure.activity;


import android.app.Activity;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import com.uestc.mode.mazeadventure.manager.ControlManager;
import com.uestc.mode.mazeadventure.view.MazeView;
import com.uestc.mode.mazeadventure.R;

public class HandControlActivity extends Activity implements View.OnClickListener {
    int MAX = 3;
    ControlManager controlManager;
    int count = 0;
    View girlBigView;
    View girlSmallView;
    Handler handler = new Handler();
    boolean isDifficultMode = false;
    boolean isEnd = false;
    boolean isGenerated = false;
    boolean isRunning = false;
    MazeView mazeView;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            HandControlActivity.this.time -= 200;
            HandControlActivity.this.handler.postDelayed(HandControlActivity.this.runnable, HandControlActivity.this.time);
            if (HandControlActivity.this.time <= 1000) {
                if (HandControlActivity.this.girlBigView.getVisibility() == View.VISIBLE) {
                    AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                }
                return;
            }
            AlphaAnimation alphaAnimation2;
            if (HandControlActivity.this.girlBigView.getVisibility() == View.VISIBLE) {
                alphaAnimation2 = new AlphaAnimation(1.0f, 0.0f);
                alphaAnimation2.setDuration(200);
                alphaAnimation2.setFillAfter(true);
                alphaAnimation2.setInterpolator(new AccelerateDecelerateInterpolator());
            } else {
                alphaAnimation2 = new AlphaAnimation(0.0f, 1.0f);
                alphaAnimation2.setDuration(200);
                alphaAnimation2.setFillAfter(true);
                alphaAnimation2.setInterpolator(new AccelerateDecelerateInterpolator());
            }
            HandControlActivity.this.girlBigView.startAnimation(alphaAnimation2);
        }
    };
    long time = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handmaze);
        this.mazeView = (MazeView) findViewById(R.id.maze_view);
        this.girlSmallView = findViewById(R.id.small_girl);
        this.girlBigView = findViewById(R.id.girl_big);
        this.girlBigView.setVisibility(View.INVISIBLE);
        this.girlSmallView.setVisibility(View.INVISIBLE);
        findViewById(R.id.generate_maze_action_tv).setVisibility(View.GONE);
        initListener();
        initData();
        this.isDifficultMode = getIntent().getBooleanExtra("diff", false);
        this.mazeView.isDifficultMode(this.isDifficultMode);
    }

    private void initListener() {
        findViewById(R.id.generate_maze_action_tv).setOnClickListener(this);
        findViewById(R.id.generate_maze_tv).setOnClickListener(this);
        findViewById(R.id.exitTv).setOnClickListener(this);
        this.mazeView.setCallback(new MazeView.Callback() {
            @Override
            public void onGenerate(boolean generated) {
                HandControlActivity.this.isGenerated = generated;
                Toast.makeText(HandControlActivity.this, "星光，懂我，也懂你", Toast.LENGTH_SHORT).show();
                HandControlActivity.this.findViewById(R.id.generate_maze_action_tv).setVisibility(View.GONE);
                HandControlActivity.this.findViewById(R.id.generate_maze_tv).setVisibility(View.GONE);
                HandControlActivity.this.showSmallGirl();
            }
            @Override
            public void onToTheEnd() {
                HandControlActivity.this.isRunning = false;
                HandControlActivity.this.isGenerated = false;
                Toast.makeText(HandControlActivity.this, "我来了，你在哪？", Toast.LENGTH_SHORT).show();
                HandControlActivity.this.dismissSmallGirl();
            }
        });
    }

    public void initData() {
        this.controlManager = new ControlManager(this);
        this.controlManager.setCallback(new ControlManager.Callback() {
            @Override
            public void controlCallback(int controlType) {
                HandControlActivity.this.mazeView.controlNextStep(controlType);
            }
        });
    }

    private void dismissSmallGirl() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        this.girlSmallView.startAnimation(alphaAnimation);
        this.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                HandControlActivity.this.showBigGirl();
            }
        }, 1000);
    }

    private void showBigGirl() {
        final AlphaAnimation showAnim = new AlphaAnimation(0.0f, 1.0f);
        showAnim.setDuration(1000);
        showAnim.setFillAfter(true);
        showAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        final AlphaAnimation dismissAnim = new AlphaAnimation(1.0f, 0.0f);
        dismissAnim.setDuration(1000);
        dismissAnim.setFillAfter(true);
        dismissAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        this.girlBigView.startAnimation(showAnim);
        this.isEnd = true;
        showAnim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                HandControlActivity.this.girlBigView.startAnimation(dismissAnim);
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
                HandControlActivity handControlActivity = HandControlActivity.this;
                handControlActivity.count++;
                if (HandControlActivity.this.count <= 2) {
                    HandControlActivity.this.girlBigView.startAnimation(showAnim);
                    return;
                }
                HandControlActivity.this.findViewById(R.id.generate_maze_action_tv).setVisibility(View.GONE);
                HandControlActivity.this.findViewById(R.id.generate_maze_tv).setVisibility(View.GONE);
                HandControlActivity.this.findViewById(R.id.exitTv).setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void showSmallGirl() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        this.girlSmallView.startAnimation(alphaAnimation);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
            switch (id) {
                case R.id.generate_maze_action_tv:
                    if (!this.isEnd && !this.isRunning) {
                        if (this.isGenerated) {
                            this.isRunning = true;
                            Toast.makeText(this, "记忆随风翻过，足迹逐渐清晰", Toast.LENGTH_LONG).show();
                            this.mazeView.autoStep();
                            break;
                        }
                        Toast.makeText(this, "足迹尚未填满，我亦不知去向", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    return;
                case R.id.generate_maze_tv:
                    if (!this.isEnd && !this.isRunning) {
                        this.isGenerated = false;
                        this.mazeView.startGenerate();
                        Toast.makeText(this, "你站在我对侧，却隔着迷宫般银河", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    return;
                default:
                    onBackPressed();
                    break;
            }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}