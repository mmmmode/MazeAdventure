package com.uestc.mode.mazeadventure;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.auto_textview).setOnClickListener(this);
        findViewById(R.id.hand_textview).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.auto_textview:
                Toast.makeText(MainActivity.this,"系统维护中。。。",Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent(MainActivity.this,MazeAutoActivity.class);
//                startActivity(intent);
                break;
            case R.id.hand_textview:
                Intent intent1 = new Intent(MainActivity.this,HandControlActivity.class);
                startActivity(intent1);
            default:
                break;
        }

    }
}
