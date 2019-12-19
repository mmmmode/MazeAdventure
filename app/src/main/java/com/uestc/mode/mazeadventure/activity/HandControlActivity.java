package com.uestc.mode.mazeadventure.activity;


import android.app.Activity;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;

import com.uestc.mode.mazeadventure.dialog.DialogManager;
import com.uestc.mode.mazeadventure.manager.GameManager;
import com.uestc.mode.mazeadventure.manager.MazeDataCenter;
import com.uestc.mode.mazeadventure.util.GameParamUtils;
import com.uestc.mode.mazeadventure.R;

public class HandControlActivity extends Activity implements View.OnClickListener {
    Handler handler = new Handler();
    int mGameMode;//当前游戏模式
    GameManager mGameManager = new GameManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handmaze);
        initData();
        mGameManager.init();
        generate();
    }


    public void initData() {
        mGameMode = getIntent().getIntExtra(GameParamUtils.SET_MODE, GameParamUtils.TEACH_MODE);
        mGameManager.setGameMode(mGameMode);
    }


    @Override
    public void onBackPressed() {
        if(mGameManager.isGameEnd){
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

    public void generate(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                mGameManager.generateMaze();
            }
        });
    }
    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        MazeDataCenter.getInstance().reset();
        super.onDestroy();
    }
}