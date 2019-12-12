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
import android.widget.Toast;

import com.uestc.mode.mazeadventure.R;
import com.uestc.mode.mazeadventure.util.GameParamUtils;

public class MainActivity extends Activity implements View.OnClickListener {
    CheckBox mVibCheckouBox;
    CheckBox mSoundCheckBox;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.auto_textview).setOnClickListener(this);
        findViewById(R.id.hand_light_extview).setOnClickListener(this);
        findViewById(R.id.hand_dark_textview).setOnClickListener(this);
        mVibCheckouBox = findViewById(R.id.checkbox);
        mVibCheckouBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GameParamUtils.setVibrate(isChecked);
            }
        });
        mSoundCheckBox = findViewById(R.id.soundbox);
        mSoundCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GameParamUtils.setIsSoundOpen(isChecked);
                setSoundState(isChecked);
            }
        });
        mediaPlayer = MediaPlayer.create(this,R.raw.bgm);
        mediaPlayer.setLooping(true);
        showMDialog();
    }

    private void showMDialog() {
        final AlertDialog alertDialog =new  AlertDialog.Builder(this)
                .setIcon(R.drawable.girl1)
                .setTitle("是否打开音效?")
                .setMessage("打开音效可以更加沉浸式体验游戏乐趣")
                .setPositiveButton("听宁的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSoundCheckBox.setChecked(true);
                    }
                })
                .setNegativeButton("室友睡了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
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