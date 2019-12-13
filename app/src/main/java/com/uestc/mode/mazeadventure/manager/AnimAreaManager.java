package com.uestc.mode.mazeadventure.manager;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.uestc.mode.mazeadventure.R;
import com.uestc.mode.mazeadventure.bean.TranslationAnimBean;

/**
 * @author mode
 * date: 2019/12/13
 */
public class AnimAreaManager {

    private Context context;
    private FrameLayout root;
    private FrameLayout.LayoutParams layoutParams;
    public AnimAreaManager(Context context,FrameLayout view){
        this.context = context;
        this.root = view;
        layoutParams = new FrameLayout.LayoutParams(200,200);
        layoutParams.gravity = Gravity.LEFT | Gravity.CENTER_HORIZONTAL;
        layoutParams.leftMargin = 200;
        layoutParams.rightMargin = 200;
        init();
    }

    private void init() {
        ImageView queenView = new ImageView(context);
        queenView.setImageDrawable(context.getResources().getDrawable(R.drawable.girl1));
        queenView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        queenView.setLayoutParams(layoutParams);
        TranslationAnimBean translationAnimBean = setTimeBean(-200,500,1200,500,2000);
        TranslateAnimation translateAnimation = getTranslationAnimation(translationAnimBean);
        queenView.startAnimation(translateAnimation);

        root.addView(queenView);
    }

    private TranslationAnimBean setTimeBean(float fromX,float fromY,float toX,float toY,int time){
        TranslationAnimBean translationAnimBean = new TranslationAnimBean(fromX,fromY,toX,toY);
        translationAnimBean.setTime(time);
        return translationAnimBean;
    }

    private TranslateAnimation getTranslationAnimation(TranslationAnimBean translationAnimBean){
        TranslateAnimation translateAnimation = new TranslateAnimation(translationAnimBean.getFromX(),translationAnimBean.getToX(),translationAnimBean.getFromY(),translationAnimBean.getToY());
        translateAnimation.setDuration(translationAnimBean.getTime());
        translateAnimation.setFillAfter(true);
        translateAnimation.setRepeatCount(1);
        return translateAnimation;
    }
}
