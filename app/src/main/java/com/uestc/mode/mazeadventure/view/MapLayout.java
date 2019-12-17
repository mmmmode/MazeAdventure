package com.uestc.mode.mazeadventure.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.uestc.mode.mazeadventure.R;
import com.uestc.mode.mazeadventure.bean.MazeUnit;
import com.uestc.mode.mazeadventure.bean.TwoDBean;

/**
 * @author mode
 * date: 2019/12/16
 */
public class MapLayout extends FrameLayout {

    Context context;
    private View root;
    public MapView mMapView;//地图背景view

    public MazeUnit[][] mazeUnits;

    public void setMazeUnits(MazeUnit[][] mazeUnits){
        this.mazeUnits = mazeUnits;
        mMapView.setMazeUnits(mazeUnits);
    }

    public MapLayout(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public MapLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public MapLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        root = LayoutInflater.from(context).inflate(R.layout.layout_common_map,this,true);
        mMapView = root.findViewById(R.id.map_view);
    }

    public void changeCurrentPosition(TwoDBean currentStep){

    }
}
