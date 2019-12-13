package com.uestc.mode.mazeadventure.manager;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.uestc.mode.mazeadventure.ApplicationEx;
import com.uestc.mode.mazeadventure.R;
import com.uestc.mode.mazeadventure.bean.TranslationAnimBean;

import java.util.Random;

/**
 * @author mode
 * date: 2019/12/13
 */
public class AnimAreaManager {
    ImageView queenView;
    ImageView bossView;
    ImageView heroView;
    private Context context;
    private FrameLayout root;
    private FrameLayout.LayoutParams layoutParams;
    int widthPixels;
    int heightPixels;
    int randomY;
    int queenFromX;
    int queenToX;
    int bossFromX;
    int bosstoX;
    int heroFromX;
    int heroTox;
    boolean leftToRight = true;
    int time = 5000;
    static Handler handler = new Handler();
    public AnimAreaManager(Context context,FrameLayout view){
        this.context = context;
        this.root = view;
        layoutParams = new FrameLayout.LayoutParams(200,340);
        layoutParams.gravity = Gravity.LEFT | Gravity.CENTER_HORIZONTAL;
        layoutParams.leftMargin = 200;
        layoutParams.rightMargin = 200;
        DisplayMetrics outMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        widthPixels = outMetrics.widthPixels;
        heightPixels = outMetrics.heightPixels;
        initView();
        startAnim();
    }

    private void initView() {
        queenView = new ImageView(context);
        queenView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        queenView.setImageDrawable(context.getResources().getDrawable(R.drawable.run_queen));
        queenView.setLayoutParams(layoutParams);
        bossView = new ImageView(context);
        bossView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        bossView.setImageDrawable(context.getResources().getDrawable(R.drawable.run_boss));
        bossView.setLayoutParams(layoutParams);
        heroView = new ImageView(context);
        heroView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        heroView.setImageDrawable(context.getResources().getDrawable(R.drawable.run_hero));
        heroView.setLayoutParams(layoutParams);
        root.addView(queenView);
        root.addView(bossView);
        root.addView(heroView);
    }

    private void randomValue(){
        randomY = new Random().nextInt(heightPixels -600) + 300;
        if(leftToRight){
            queenFromX = -300;
            queenToX = widthPixels+1000;
            bossFromX = queenFromX - 300;
            bosstoX = queenToX - 300;
            heroFromX = bossFromX - 400;
            heroTox = bosstoX;
        }else {
            queenFromX = widthPixels + 200;
            queenToX = -1200;
            bossFromX = queenFromX + 300;
            bosstoX = queenToX + 300;
            heroFromX = bossFromX + 400;
            heroTox = bosstoX;
        }
    }
    private void startAnim() {
        randomValue();
        if(leftToRight){
            queenView.setImageDrawable(context.getResources().getDrawable(R.drawable.run_queen));
            bossView.setImageDrawable(context.getResources().getDrawable(R.drawable.run_boss));
            heroView.setImageDrawable(context.getResources().getDrawable(R.drawable.run_hero));
        }else {
            queenView.setImageDrawable(context.getResources().getDrawable(R.drawable.run_queen_2));
            bossView.setImageDrawable(context.getResources().getDrawable(R.drawable.run_boss_2));
            heroView.setImageDrawable(context.getResources().getDrawable(R.drawable.run_hero_2));
        }
        TranslationAnimBean queenAnimBean = setTimeBean(queenFromX,randomY,queenToX,randomY,time);
        TranslationAnimBean bossAnimBean = setTimeBean(bossFromX,randomY,bosstoX,randomY,time);
        TranslationAnimBean heroAnimBean = setTimeBean(heroFromX,randomY,heroTox,randomY,time);
        TranslateAnimation queenTransAnimation = getTranslationAnimation(queenAnimBean);
        queenTransAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        leftToRight = !leftToRight;
                        startAnim();
                    }
                },1000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        queenView.startAnimation(queenTransAnimation);
        bossView.startAnimation(getTranslationAnimation(bossAnimBean));
        heroView.startAnimation(getTranslationAnimation(heroAnimBean));

    }

    private TranslationAnimBean setTimeBean(float fromX,float fromY,float toX,float toY,int time){
        TranslationAnimBean translationAnimBean = new TranslationAnimBean(fromX,fromY,toX,toY);
        translationAnimBean.setTime(time);
        return translationAnimBean;
    }

    private TranslateAnimation getTranslationAnimation(TranslationAnimBean translationAnimBean){
        TranslateAnimation translateAnimation = new TranslateAnimation(translationAnimBean.getFromX(),translationAnimBean.getToX(),translationAnimBean.getFromY(),translationAnimBean.getToY());
        translateAnimation.setDuration(translationAnimBean.getTime());
        translateAnimation.setFillBefore(true);
        translateAnimation.setFillAfter(true);
        return translateAnimation;
    }
}
