package com.uestc.mode.mazeadventure.activity;


import android.app.Activity;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import com.uestc.mode.mazeadventure.manager.ControlManager;
import com.uestc.mode.mazeadventure.manager.DialogManager;
import com.uestc.mode.mazeadventure.settingpref.SettingsPrefs;
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
    boolean isControlEnd = false;
    boolean isGenerated = false;
    boolean isRunning = false;
    MazeView mazeView;
    TextView currentGuankaTv;
    TextView currentTimeTv;
    TextView currentStepTv;
    private int currenCheckouPoint;
    private int currentTime = 0;
    private int currentStep = 0;
    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            for(;;){
                if(isControlEnd)return;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currentTime++;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        currentTimeTv.setText(getResources().getString(R.string.currentTime,currentTime+""));
                    }
                });
            }
        }
    });
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time -= 200;
            handler.postDelayed(runnable, HandControlActivity.this.time);
            if (time <= 1000) {
                if (girlBigView.getVisibility() == View.VISIBLE) {
                    AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                }
                return;
            }
            AlphaAnimation alphaAnimation2;
            if (girlBigView.getVisibility() == View.VISIBLE) {
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
            girlBigView.startAnimation(alphaAnimation2);
        }
    };
    long time = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handmaze);
        initView();
        initListener();
        initManager();
        initData();
    }

    private void initView() {
        this.mazeView = (MazeView) findViewById(R.id.maze_view);
        this.girlSmallView = findViewById(R.id.small_girl);
        this.girlBigView = findViewById(R.id.girl_big);
        this.girlBigView.setVisibility(View.INVISIBLE);
        this.girlSmallView.setVisibility(View.INVISIBLE);
        this.currentStepTv = findViewById(R.id.tv_current_step);
        this.currentTimeTv = findViewById(R.id.tv_current_time);
        currentGuankaTv = findViewById(R.id.tv_current_guanka);
        findViewById(R.id.generate_maze_action_tv).setVisibility(View.GONE);
    }

    private void initListener() {
        findViewById(R.id.generate_maze_action_tv).setOnClickListener(this);
        findViewById(R.id.generate_maze_tv).setOnClickListener(this);
        findViewById(R.id.exitTv).setOnClickListener(this);
        this.mazeView.setCallback(new MazeView.Callback() {
            @Override
            public void onGenerate(boolean generated) {
                HandControlActivity.this.isGenerated = generated;
                Toast.makeText(HandControlActivity.this, "通往被困公主的道路已经打通", Toast.LENGTH_SHORT).show();
                HandControlActivity.this.findViewById(R.id.generate_maze_action_tv).setVisibility(View.GONE);
                HandControlActivity.this.findViewById(R.id.generate_maze_tv).setVisibility(View.GONE);
                HandControlActivity.this.showSmallGirl();
            }
            @Override
            public void onToTheEnd() {
                HandControlActivity.this.isRunning = false;
                HandControlActivity.this.isGenerated = false;
                isControlEnd = true;
                Toast.makeText(HandControlActivity.this, "卧槽，公主呢？？？", Toast.LENGTH_SHORT).show();
                HandControlActivity.this.dismissSmallGirl();
            }
        });
    }

    private void reset(){
        currentTime = 0;
        currentStep = 0;
    }
    private void initManager() {
        this.controlManager = new ControlManager(this);
        this.controlManager.setCallback(new ControlManager.Callback() {
            @Override
            public void controlCallback(int controlType) {
                mazeView.controlNextStep(controlType);
                if(isControlEnd)return;
                if(currentStep == 0){
                    if(!thread.isAlive())
                    thread.start();
                }
                currentStep++;
                currentStepTv.setText(getResources().getString(R.string.currentStep,currentStep+""));
            }
        });
    }

    public void initData() {
        this.isDifficultMode = getIntent().getBooleanExtra("diff", false);
        this.mazeView.isDifficultMode(this.isDifficultMode);
        currenCheckouPoint = SettingsPrefs.getInstance().getCurrentCheckouPoint();
        currentGuankaTv.setText(getResources().getString(R.string.checkoutpoint,currenCheckouPoint+""));
        currentTimeTv.setText(getResources().getString(R.string.currentTime,currentTime+""));
        currentStepTv.setText(getResources().getString(R.string.currentStep,currentStep+""));
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
                showBigGirl();
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
                isEnd = true;
                HandControlActivity.this.findViewById(R.id.generate_maze_action_tv).setVisibility(View.GONE);
                HandControlActivity.this.findViewById(R.id.generate_maze_tv).setVisibility(View.GONE);
                HandControlActivity.this.findViewById(R.id.exitTv).setVisibility(View.VISIBLE);
                if(SettingsPrefs.getInstance().getCurrentCheckouPoint() == 1){
                    SettingsPrefs.getInstance().setCurrentCheckPoint(2);
                }
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
                            Toast.makeText(this, "看我机器人速通操作", Toast.LENGTH_LONG).show();
                            this.mazeView.autoStep();
                            break;
                        }
                        Toast.makeText(this, "没图呢，先生成一个", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    return;
                case R.id.generate_maze_tv:
                    if (!this.isEnd && !this.isRunning) {
                        this.isGenerated = false;
                        this.mazeView.startGenerate();
                        Toast.makeText(this, "曾经和你一起走过，传说中的迷宫", Toast.LENGTH_SHORT).show();
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
        if(isEnd){
            finish();
            return;
        }
        DialogManager.onBackPressDialog(this, new DialogManager.OnDialogClickListener(){
            @Override
            public void onNagetiveButtonClick() {
                finish();
            }

            @Override
            public void onPositiveButtonClick() {
            }
        });
    }
}