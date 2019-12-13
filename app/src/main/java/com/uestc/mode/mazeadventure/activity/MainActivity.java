package com.uestc.mode.mazeadventure.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
        if(!SettingsPrefs.getInstance().isFirstLogin())return;
        DialogManager.showMDialog(this,new DialogManager.OnDialogClickListener(){
            @Override
            public void onPositiveButtonClick() {
                mVibCheckouBox.setChecked(true);
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
    @Override
    protected void onResume() {
        super.onResume();
        mVibCheckouBox.setChecked(GameParamUtils.isVibrate());
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
                intent2 = new Intent(this, HandControlActivity.class);
                intent2.putExtra("diff", true);
                startActivity(intent2);
                break;
            case R.id.hand_light_extview:
                intent2 = new Intent(this, HandControlActivity.class);
                intent2.putExtra("diff", false);
                startActivity(intent2);
                break;
        }

    }

}