package com.uestc.mode.mazeadventure.manager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.uestc.mode.mazeadventure.R;
import com.uestc.mode.mazeadventure.callback.HandControlCallback;

/**
 * 控制手柄的管理类
 */
public class HandControlManager implements View.OnClickListener,HandControlCallback{
    public static final int CONTROL_LEFT = 0;
    public static final int CONTROL_RIGHT = 1;
    public static final int CONTROL_TOP = 2;
    public static final int CONTROL_BOTTOM = 3;
    View leftView;
    View rightView;
    View topView;
    View bottomView;
    View root;
    Activity context;
    public HandControlManager(Activity context){
        this.context = context;
        init();
    }

    public void init(){
        root = context.findViewById(R.id.controlView);
        leftView = root.findViewById(R.id.left_view);
        rightView = root.findViewById(R.id.right_view);
        topView = root.findViewById(R.id.top_view);
        bottomView = root.findViewById(R.id.bottom_view);
        leftView.setOnClickListener(this);
        rightView.setOnClickListener(this);
        topView.setOnClickListener(this);
        bottomView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (callback == null) return;
        int type = -1;
        switch (view.getId()){
            case R.id.left_view:
                type = (CONTROL_LEFT);
                break;
            case R.id.right_view:
                type = (CONTROL_RIGHT);
                break;
            case R.id.top_view:
                type = (CONTROL_TOP);
                break;
            case R.id.bottom_view:
                type = (CONTROL_BOTTOM);
                break;
        }
        if(type != -1)
        callback.controlCallback(type);
    }

    @Override
    public void onControlType(int TYPE) {

    }
}
