package com.uestc.mode.mazeadventure.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.uestc.mode.mazeadventure.R;
import com.uestc.mode.mazeadventure.manager.AnimAreaManager;
import com.uestc.mode.mazeadventure.manager.DialogManager;
import com.uestc.mode.mazeadventure.settingpref.SettingsPrefs;
import com.uestc.mode.mazeadventure.util.GameParamUtils;

public class MainActivity extends Activity implements View.OnClickListener {
    CheckBox mVibCheckouBox;
    CheckBox mSoundCheckBox;
    MediaPlayer mediaPlayer;
    AnimAreaManager mAnimAreaManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        initManager();
        initData();
    }

    private void initManager() {
        mediaPlayer = MediaPlayer.create(this,R.raw.bgm);
        mediaPlayer.setLooping(true);
        mAnimAreaManager = new AnimAreaManager(this, (FrameLayout) findViewById(R.id.anim_area));
    }

    private void initView() {
        mVibCheckouBox = findViewById(R.id.checkbox);
        mSoundCheckBox = findViewById(R.id.soundbox);
    }

    private void initListener() {
        findViewById(R.id.auto_textview).setOnClickListener(this);
        findViewById(R.id.hand_light_extview).setOnClickListener(this);
        findViewById(R.id.hand_dark_textview).setOnClickListener(this);
        findViewById(R.id.achievement).setOnClickListener(this);
        findViewById(R.id.why_save_queen).setOnClickListener(this);

        mVibCheckouBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GameParamUtils.setVibrate(isChecked);
            }
        });

        mSoundCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GameParamUtils.setIsSoundOpen(isChecked);
                setSoundState(isChecked);
            }
        });
    }


    private void initData() {
        showSoundDialog();
    }

    private void showSoundDialog() {
        if(GameParamUtils.isIsSoundOpen())return;
//        if(!SettingsPrefs.getInstance().isFirstLogin())return;
        DialogManager.showMDialog(this,new DialogManager.OnDialogClickListener(){
            @Override
            public void onPositiveButtonClick() {
                mSoundCheckBox.setChecked(true);
                SettingsPrefs.getInstance().setIsFirstLogin(false);
            }
        });
    }

    private void setSoundState(boolean isOpen){
        if(isOpen){
            mediaPlayer.start();
        }else {
            mediaPlayer.pause();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onResume() {
        super.onResume();
        mVibCheckouBox.setChecked(GameParamUtils.isVibrate());
        mSoundCheckBox.setChecked(GameParamUtils.isIsSoundOpen());
        setTextViewState();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setTextViewState() {
        int checkPoint = SettingsPrefs.getInstance().getCurrentCheckouPoint();
        if(checkPoint  < 1){
            findViewById(R.id.hand_light_extview).setBackground(getResources().getDrawable(R.drawable.shape_click_gray_round_4dp));
        }else {
            findViewById(R.id.hand_light_extview).setBackground(getResources().getDrawable(R.drawable.shape_click_round_4dp));
        }
        if(checkPoint  < 2){
            findViewById(R.id.hand_dark_textview).setBackground(getResources().getDrawable(R.drawable.shape_click_gray_round_4dp));
        }else {
            findViewById(R.id.hand_dark_textview).setBackground(getResources().getDrawable(R.drawable.shape_click_round_4dp));
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent2;
        switch (id){
            case R.id.auto_textview:
                startActivity(new Intent(this, MazeAutoActivity.class));
                break;
            case R.id.hand_dark_textview:
                if(SettingsPrefs.getInstance().getCurrentCheckouPoint() < 2){
                    Toast.makeText(MainActivity.this,"先完成前面的任务啊老哥。没看见按钮灰的吗",Toast.LENGTH_LONG).show();
                    return;
                }
                intent2 = new Intent(this, HandControlActivity.class);
                intent2.putExtra("diff", true);
                startActivity(intent2);
                break;
            case R.id.hand_light_extview:
                if(SettingsPrefs.getInstance().getCurrentCheckouPoint() < 1){
                    Toast.makeText(MainActivity.this,"先完成前面的任务啊老哥。没看见按钮灰的吗",Toast.LENGTH_LONG).show();
                    return;
                }
                intent2 = new Intent(this, HandControlActivity.class);
                intent2.putExtra("diff", false);
                startActivity(intent2);
                break;
            case R.id.why_save_queen:
                DialogManager.showBeijingDialog(this,null);
                break;
            case R.id.achievement:
                Toast.makeText(MainActivity.this,"敬请期待",Toast.LENGTH_SHORT).show();
                break;

        }

    }

}