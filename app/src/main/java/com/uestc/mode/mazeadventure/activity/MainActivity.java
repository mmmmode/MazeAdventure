package com.uestc.mode.mazeadventure.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.uestc.mode.mazeadventure.R;

public class MainActivity extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.auto_textview).setOnClickListener(this);
        findViewById(R.id.hand_light_extview).setOnClickListener(this);
        findViewById(R.id.hand_dark_textview).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.auto_textview:
                Intent intent = new Intent(MainActivity.this,MazeAutoActivity.class);
                startActivity(intent);
                break;
            case R.id.hand_light_extview:
                Intent intent1 = new Intent(MainActivity.this,HandControlActivity.class);
                intent1.putExtra("diff",false);
                startActivity(intent1);
                break;
            case R.id.hand_dark_textview:
                Intent intent2 = new Intent(MainActivity.this,HandControlActivity.class);
                intent2.putExtra("diff",true);
                startActivity(intent2);
            default:
                break;
        }

    }
}
