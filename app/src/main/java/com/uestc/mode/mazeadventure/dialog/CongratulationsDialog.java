package com.uestc.mode.mazeadventure.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.uestc.mode.mazeadventure.R;
import com.uestc.mode.mazeadventure.manager.MazeDataCenter;
import com.uestc.mode.mazeadventure.util.GameParamUtils;
import com.uestc.mode.mazeadventure.view.ScratchAwardView;

/**
 * @author mode
 * date: 2019/12/19
 */
public class CongratulationsDialog implements View.OnClickListener {
    private AlertDialog alertDialog;
    Context context;
    View root;
    TextView guankaTv;
    TextView currentTimeTv;
    TextView standardTimeTv;
    TextView highTimeTv;
    TextView currentStepTv;
    TextView standardStepTv;
    TextView highStepTv;
    TextView pinggenTv;
    View baoxiangView;
    View guaguaView;
    ImageView baoxiang1;
    ImageView baoxiang2;
    ImageView baoxiang3;
    Button nextButton;
    ScratchAwardView scratchAwardView;
    public CongratulationsDialog(Context context) {
        alertDialog = new AlertDialog.Builder(context)
        .setCancelable(true).create();
        this.context = context;
        root = LayoutInflater.from(context).inflate(R.layout.dialog_congratulations,null);
        init();
    }

    private void init() {
        currentStepTv = root.findViewById(R.id.tv_current_step);
        standardStepTv = root.findViewById(R.id.tv_stantart_step);
        highStepTv = root.findViewById(R.id.tv_high_step);
        currentTimeTv = root.findViewById(R.id.tv_current_time);
        standardTimeTv = root.findViewById(R.id.tv_stantart_time);
        highTimeTv = root.findViewById(R.id.tv_high_time);
        baoxiang1 = root.findViewById(R.id.baoxiang1);
        baoxiang2 = root.findViewById(R.id.baoxiang2);
        baoxiang3 = root.findViewById(R.id.baoxiang3);
        baoxiangView = root.findViewById(R.id.fl_baoxiang);
        guaguaView = root.findViewById(R.id.fl_result);
        nextButton = root.findViewById(R.id.to_next_checkpoint);
        pinggenTv = root.findViewById(R.id.tv_pingfen);
        baoxiang1.setOnClickListener(this);
        baoxiang2.setOnClickListener(this);
        baoxiang3.setOnClickListener(this);
        guankaTv = root.findViewById(R.id.tv_checkpoint);
        scratchAwardView = root.findViewById(R.id.scratchAwardView);
    }

    public void show(){
        alertDialog.show();
        alertDialog.setContentView(root);
        setText();
    }

    public void setText(){
        GameParamUtils gameParamUtils = new GameParamUtils();
        currentTimeTv.setText(context.getResources().getString(R.string.currentTime,MazeDataCenter.getInstance().getTime()));
        standardTimeTv.setText(context.getResources().getString(R.string.result_stantard_time,30));
        highTimeTv.setText(context.getResources().getString(R.string.result_high_time,20));
        currentStepTv.setText(context.getResources().getString(R.string.currentStep,MazeDataCenter.getInstance().getStepCount()));
        standardStepTv.setText(context.getResources().getString(R.string.result_stantard_step,50));
        highStepTv.setText(context.getResources().getString(R.string.result_high_step,40));
        guankaTv.setText(context.getResources().getString(R.string.checkoutpoint,gameParamUtils.getCheckoutpoint()));
        pinggenTv.setText(context.getResources().getString(R.string.result_tongguan,"sss"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.baoxiang1:
                baoxiangchoose(1);
                break;
            case R.id.baoxiang2:
                baoxiangchoose(2);
                break;
            case R.id.baoxiang3:
                baoxiangchoose(3);
                break;
        }
    }

    private void baoxiangchoose(int i) {
        baoxiangView.setVisibility(View.GONE);
        guaguaView.setVisibility(View.VISIBLE);
    }
}
