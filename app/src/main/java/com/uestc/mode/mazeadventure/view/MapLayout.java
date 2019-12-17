package com.uestc.mode.mazeadventure.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.uestc.mode.mazeadventure.R;
import com.uestc.mode.mazeadventure.bean.MazeUnit;
import com.uestc.mode.mazeadventure.bean.TwoDBean;
import com.uestc.mode.mazeadventure.manager.MazeDataCenter;
import com.uestc.mode.mazeadventure.util.CommonUtils;
import com.uestc.mode.mazeadventure.util.GameParamUtils;
import com.uestc.mode.mazeadventure.util.MLog;

/**
 * @author mode
 * date: 2019/12/16
 */
public class MapLayout extends FrameLayout {

    Context mContext;
    private ViewGroup root;
    private ViewGroup mapContainer;
    public MapView mMapView;//地图背景view
    public HeroView heroView;

    public MapLayout(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public MapLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public MapLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        root = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.layout_common_map,this,true);
        mapContainer = root.findViewById(R.id.map_container);
        mMapView = root.findViewById(R.id.map_view);
        initHeroView();
    }

    private void initHeroView(){
        heroView = new HeroView(mContext);
        heroView.setHeroViewSrc(mContext.getResources().getDrawable(R.drawable.run_hero));
        MazeDataCenter.getInstance().setCurrenStep(new TwoDBean(0,0));
        changeCurrentPosition();
        mapContainer.addView(heroView);
        heroView.setVisibility(GONE);
    }

    private FrameLayout.LayoutParams setPositionParam(int marginLeft,int marginTop,int size){
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(size,size);
        layoutParams.gravity = Gravity.LEFT | Gravity.CENTER_HORIZONTAL;
        layoutParams.leftMargin = marginLeft;
        layoutParams.topMargin = marginTop;
        return layoutParams;
    }

    //改变位置
    public void changeCurrentPosition(){
        TwoDBean currentStep = MazeDataCenter.getInstance().getCurrenStep();
        GameParamUtils gameParamUtils = MazeDataCenter.getInstance().getGameParamUtils();
        int cellSize = gameParamUtils.getCellSize();
        int itemSize = cellSize - 10;
        int x = currentStep.x * cellSize + diffX(cellSize);
        int y = currentStep.y * cellSize + diffY();
        LayoutParams layoutParams = setPositionParam(x,y,itemSize);
        heroView.setLayoutParams(layoutParams);
    }

    private int diffX(int cellSize){
        return  CommonUtils.dip2px(mContext,10) + cellSize /2;
    }

    private int diffY(){
        return CommonUtils.dip2px(mContext,10);
    }

    public void refreshMapView(){
        heroView.setVisibility(VISIBLE);
        mMapView.invalidate();
    }
}






