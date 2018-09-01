package com.uestc.mode.mazeadventure;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class ControlManager implements View.OnClickListener{
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
    public ControlManager(Activity context){
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

    Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onClick(View view) {
        if (callback == null) return;
        switch (view.getId()){
            case R.id.left_view:
                callback.controlCallback(CONTROL_LEFT);
                break;
            case R.id.right_view:
                callback.controlCallback(CONTROL_RIGHT);
                break;
            case R.id.top_view:
                callback.controlCallback(CONTROL_TOP);
                break;
            case R.id.bottom_view:
                callback.controlCallback(CONTROL_BOTTOM);
                break;
        }
    }

    interface Callback{
        void controlCallback(int controlType);
    }
}
