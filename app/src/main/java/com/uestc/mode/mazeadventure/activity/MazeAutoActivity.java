package com.uestc.mode.mazeadventure.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.opengl.Visibility;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import com.uestc.mode.mazeadventure.view.MazeView;
import com.uestc.mode.mazeadventure.R;

public class MazeAutoActivity extends Activity implements View.OnClickListener {
    int MAX = 3;
    int count = 0;
    View girlBigView;
    View girlSmallView;
    Handler handler = new Handler();
    boolean isEnd = false;
    boolean isGenerated = false;
    boolean isRunning = false;
    MazeView mazeView;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            MazeAutoActivity.this.time -= 200;
            MazeAutoActivity.this.handler.postDelayed(MazeAutoActivity.this.runnable, MazeAutoActivity.this.time);
            if (MazeAutoActivity.this.time <= 1000) {
                if (MazeAutoActivity.this.girlBigView.getVisibility() == View.VISIBLE) {
                    AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                }
                return;
            }
            AlphaAnimation alphaAnimation2;
            if (MazeAutoActivity.this.girlBigView.getVisibility() == View.VISIBLE) {
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
            MazeAutoActivity.this.girlBigView.startAnimation(alphaAnimation2);
        }
    };
    long time = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automaze);
        this.mazeView = (MazeView) findViewById(R.id.maze_view);
        this.girlSmallView = findViewById(R.id.small_girl);
        this.girlBigView = findViewById(R.id.girl_big);
        this.girlBigView.setVisibility(View.INVISIBLE);
        this.girlSmallView.setVisibility(View.INVISIBLE);
        initListener();
    }

    private void initListener() {
        findViewById(R.id.generate_maze_action_tv).setOnClickListener(this);
        findViewById(R.id.generate_maze_tv).setOnClickListener(this);
        findViewById(R.id.exitTv).setOnClickListener(this);
        this.mazeView.setCallback(new MazeView.Callback() {
            @Override
            public void onGenerate(boolean generated) {
                MazeAutoActivity.this.isGenerated = generated;
                Toast.makeText(MazeAutoActivity.this, "我们之间的距离就像迷宫忽远又忽近", Toast.LENGTH_SHORT).show();
                MazeAutoActivity.this.showSmallGirl();
            }
            @Override
            public void onToTheEnd() {
                MazeAutoActivity.this.isRunning = false;
                MazeAutoActivity.this.isGenerated = false;
                Toast.makeText(MazeAutoActivity.this, "我来了，你在哪？", Toast.LENGTH_SHORT).show();
                MazeAutoActivity.this.dismissSmallGirl();
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
                MazeAutoActivity.this.showBigGirl();
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
                MazeAutoActivity.this.girlBigView.startAnimation(dismissAnim);
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
                MazeAutoActivity mazeAutoActivity = MazeAutoActivity.this;
                mazeAutoActivity.count++;
                if (MazeAutoActivity.this.count <= 2) {
                    MazeAutoActivity.this.girlBigView.startAnimation(showAnim);
                    return;
                }
                MazeAutoActivity.this.findViewById(R.id.generate_maze_action_tv).setVisibility(View.GONE);
                MazeAutoActivity.this.findViewById(R.id.generate_maze_tv).setVisibility(View.GONE);
                MazeAutoActivity.this.findViewById(R.id.exitTv).setVisibility(View.VISIBLE);
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
                        Toast.makeText(this, "足迹尚未填满，我亦不知去向", Toast.LENGTH_LONG).show();
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