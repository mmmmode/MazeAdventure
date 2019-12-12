package com.uestc.mode.mazeadventure.manager;

import android.app.Activity;
import android.view.View;

import com.uestc.mode.mazeadventure.R;

public class ControlManager implements View.OnClickListener {
    public static final int CONTROL_BOTTOM = 3;
    public static final int CONTROL_LEFT = 0;
    public static final int CONTROL_RIGHT = 1;
    public static final int CONTROL_TOP = 2;
    View bottomView;
    Callback callback;
    Activity context;
    View leftView;
    View rightView;
    View root;
    View topView;

    public interface Callback {
        void controlCallback(int i);
    }

    public ControlManager(Activity context) {
        this.context = context;
        init();
    }

    public void init() {
        this.root = this.context.findViewById(R.id.controlView);
        this.leftView = this.root.findViewById(R.id.left_view);
        this.rightView = this.root.findViewById(R.id.right_view);
        this.topView = this.root.findViewById(R.id.top_view);
        this.bottomView = this.root.findViewById(R.id.bottom_view);
        this.leftView.setOnClickListener(this);
        this.rightView.setOnClickListener(this);
        this.topView.setOnClickListener(this);
        this.bottomView.setOnClickListener(this);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
    @Override
    public void onClick(View view) {
        if (this.callback != null) {
            int type = -1;
            int id = view.getId();
            if (id == R.id.bottom_view) {
                type = 3;
            } else if (id == R.id.left_view) {
                type = 0;
            } else if (id == R.id.right_view) {
                type = 1;
            } else if (id == R.id.top_view) {
                type = 2;
            }
            if (type != -1) {
                this.callback.controlCallback(type);
            }
        }
    }
}