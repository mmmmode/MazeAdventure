package com.uestc.mode.mazeadventure.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.uestc.mode.mazeadventure.R;

/**
 * @author mode
 * date: 2019/12/17
 */
public class HeroView extends FrameLayout {

    Context mContext;
    View root;
    ImageView mHeroView;
    public HeroView(Context context) {
        super(context);
        init(context);
    }

    public HeroView( Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public HeroView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        root = LayoutInflater.from(mContext).inflate(R.layout.layout_hero,this,true);
        mHeroView = root.findViewById(R.id.item_hero_view);
    }

    public void setHeroViewSrc(Drawable drawable){
        mHeroView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        if(drawable!=null){
            mHeroView.setImageDrawable(drawable);
        }
    }
}
