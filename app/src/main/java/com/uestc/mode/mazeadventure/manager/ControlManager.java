package com.uestc.mode.mazeadventure.manager;

import android.app.Activity;
import android.view.View;

import com.uestc.mode.mazeadventure.R;

/**
 * 遥控器控制
 */
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

    private void init() {
        this.root = context.findViewById(R.id.controlView);
        this.leftView = root.findViewById(R.id.left_view);
        this.rightView = root.findViewById(R.id.right_view);
        this.topView = root.findViewById(R.id.top_view);
        this.bottomView = root.findViewById(R.id.bottom_view);
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
                type = CONTROL_BOTTOM;
            } else if (id == R.id.left_view) {
                type = CONTROL_LEFT;
            } else if (id == R.id.right_view) {
                type = CONTROL_RIGHT;
            } else if (id == R.id.top_view) {
                type = CONTROL_TOP;
            }
            if (type != -1) {
                this.callback.controlCallback(type);
            }
        }
    }
}