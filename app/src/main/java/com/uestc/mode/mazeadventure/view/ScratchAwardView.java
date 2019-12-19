package com.uestc.mode.mazeadventure.view;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.uestc.mode.mazeadventure.util.MLog;

public class ScratchAwardView extends View {
    private Paint drawPaint;
    private Bitmap trackBitmap; // 轨迹图片，一会儿要在此图片上画滑动轨迹，然后通过 Xfermode 技术同遮罩层图片融合就能实现涂抹的效果
    private Canvas trackBitmapCanvas;
    private Xfermode xfermode;
    private Paint linePaint;    // 触摸轨迹线画笔
    private Paint transparentPaint;
    private int defaultMaskColor = Color.LTGRAY;
    private Bitmap maskBitmap;  // 遮罩层图片
    private Rect dstRect;
    private Rect srcRect;

    private View hintView;
    private OnAcrossHintViewListener onAcrossHintViewListener;

    private GestureDetector gestureDetector;

    private int autoCleanPercent = 60;

    private boolean isAutoClean = false;

    public ScratchAwardView(Context context) {
        this(context, null, 0);
    }

    public ScratchAwardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScratchAwardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        drawPaint = new Paint();
        drawPaint.setColor(Color.BLACK);
        drawPaint.setFilterBitmap(true);

        linePaint = new Paint();
        linePaint.setColor(Color.BLACK);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeJoin(Paint.Join.ROUND); // 前圆角
        linePaint.setStrokeCap(Paint.Cap.ROUND); // 后圆角
        linePaint.setStrokeWidth(30); // 笔宽

        transparentPaint = new Paint();
        transparentPaint.setColor(Color.TRANSPARENT);
        transparentPaint.setStyle(Paint.Style.STROKE);

        xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT);

        gestureDetector = new GestureDetector(getContext(), new EventHandleListener());
    }

    public void reset(){
        isAutoClean = false;
    }
    public static Rect computeSrcRect(Point sourceSize, Point targetSize, ImageView.ScaleType scaleType) {
        if (scaleType == ImageView.ScaleType.CENTER_INSIDE || scaleType == ImageView.ScaleType.MATRIX || scaleType == ImageView.ScaleType.FIT_XY) {
            return new Rect(0, 0, sourceSize.x, sourceSize.y);
        } else {
            float scale;
            if (Math.abs(sourceSize.x - targetSize.x) < Math.abs(sourceSize.y - targetSize.y)) {
                scale = (float) sourceSize.x / targetSize.x;
                if ((int) (targetSize.y * scale) > sourceSize.y) {
                    scale = (float) sourceSize.y / targetSize.y;
                }
            } else {
                scale = (float) sourceSize.y / targetSize.y;
                if ((int) (targetSize.x * scale) > sourceSize.x) {
                    scale = (float) sourceSize.x / targetSize.x;
                }
            }
            int srcLeft;
            int srcTop;
            int srcWidth = (int) (targetSize.x * scale);
            int srcHeight = (int) (targetSize.y * scale);
            if (scaleType == ImageView.ScaleType.FIT_START) {
                srcLeft = 0;
                srcTop = 0;
            } else if (scaleType == ImageView.ScaleType.FIT_END) {
                if (sourceSize.x > sourceSize.y) {
                    srcLeft = sourceSize.x - srcWidth;
                    srcTop = 0;
                } else {
                    srcLeft = 0;
                    srcTop = sourceSize.y - srcHeight;
                }
            } else {
                if (sourceSize.x > sourceSize.y) {
                    srcLeft = (sourceSize.x - srcWidth) / 2;
                    srcTop = 0;
                } else {
                    srcLeft = 0;
                    srcTop = (sourceSize.y - srcHeight) / 2;
                }
            }
            return new Rect(srcLeft, srcTop, srcLeft + srcWidth, srcTop + srcHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isAutoClean){
            canvas.drawRect(new RectF(0, 0, getWidth(), getHeight()),transparentPaint);
            return;
        }
        int layerCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null,Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(trackBitmap, getPaddingLeft(), getPaddingTop(), drawPaint);
        drawPaint.setXfermode(xfermode);
        canvas.drawBitmap(maskBitmap, srcRect, dstRect, drawPaint);
        drawPaint.setXfermode(null);
        if (!isInEditMode()) {
            canvas.restoreToCount(layerCount);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int width = right - left;
        int height = bottom - top;

        // 初始化轨迹图片，稍后将在此图上绘制触摸轨迹
        if (trackBitmap == null) {
            trackBitmap = Bitmap.createBitmap(width - getPaddingRight() - getPaddingLeft(), height - getPaddingBottom() - getPaddingTop(), Bitmap.Config.ARGB_8888);
            trackBitmapCanvas = new Canvas(trackBitmap);
            // 如果已经创建了但是宽高有变化就重新创建
        } else if (trackBitmap.getWidth() != width - getPaddingRight() - getPaddingLeft() || trackBitmap.getHeight() != height - getPaddingBottom() - getPaddingTop()) {
            trackBitmap.recycle();
            trackBitmap = Bitmap.createBitmap(width - getPaddingRight() - getPaddingLeft(), height - getPaddingBottom() - getPaddingTop(), Bitmap.Config.ARGB_8888);
            trackBitmapCanvas.setBitmap(trackBitmap);
        }

        // 初始化遮罩图片
        if (maskBitmap == null) {
            maskBitmap = Bitmap.createBitmap(width - getPaddingRight() - getPaddingLeft(), height - getPaddingBottom() - getPaddingTop(), Bitmap.Config.ARGB_8888);
            new Canvas(maskBitmap).drawColor(defaultMaskColor);
        }

        // 初始化dst位置
        if (dstRect == null) {
            dstRect = new Rect(getPaddingLeft(), getPaddingTop(), width - getPaddingRight(), height - getPaddingBottom());
        } else {
            dstRect.set(getPaddingLeft(), getPaddingTop(), width - getPaddingRight(), height - getPaddingBottom());
        }

        srcRect = computeSrcRect(new Point(maskBitmap.getWidth(), maskBitmap.getHeight()), new Point(dstRect.width(), dstRect.height()), ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return isEnabled() && gestureDetector.onTouchEvent(event);
    }

    /**
     * 设置遮罩图片
     *
     * @param maskBitmap 遮罩图片
     */
    @SuppressWarnings("unused")
    public void setMaskImage(Bitmap maskBitmap) {
        this.maskBitmap = maskBitmap;
        requestLayout();
    }

    /**
     * 设置遮罩图片
     *
     * @param color 遮罩图片的颜色，稍后将使用此颜色创建一张图片
     */
    @SuppressWarnings("unused")
    public void setMaskImage(int color) {
        this.defaultMaskColor = color;
        if (maskBitmap != null) {
            if (!maskBitmap.isRecycled()) {
                maskBitmap.recycle();
            }
            maskBitmap = null;
        }
        requestLayout();
    }

    /**
     * 设置画笔的宽度
     *
     * @param strokeWidth 画笔的宽度
     */
    @SuppressWarnings("unused")
    public void setStrokeWidth(int strokeWidth) {
        this.linePaint.setStrokeWidth(strokeWidth);
    }

    /**
     * 激活监听划过提示视图的功能
     *
     * @param hintView                 提示视图，当用户划过此视图的时候就会回调监听器，一般来说此视图应该是隐藏在 {@link ScratchAwardView} 之下的中奖提示视图
     * @param onAcrossHintViewListener 当用户划过提示视图的时候就会回调此监听器
     */
    public void enableAcrossMonitor(View hintView, OnAcrossHintViewListener onAcrossHintViewListener) {
        this.hintView = hintView;
        this.onAcrossHintViewListener = onAcrossHintViewListener;
    }

    public int getAutoCleanPercent() {
        return autoCleanPercent;
    }

    public void setAutoCleanPercent(int autoCleanPercent) {
        this.autoCleanPercent = autoCleanPercent;
    }

    public interface OnAcrossHintViewListener {
        void onAcrossHintView(View hintView);
    }

    private class EventHandleListener implements GestureDetector.OnGestureListener {
        private float downX;
        private float downY;
        private float moveX;
        private float moveY;
        private boolean allowAcrossCallback;
        private Rect hintViewGlobalVisibleRect;

        @Override
        public boolean onDown(MotionEvent event) {
            if (getParent() != null) {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
            downX = event.getX();
            downY = event.getY();
            allowAcrossCallback = hintView != null && onAcrossHintViewListener != null;
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (isEnabled() && isClickable()) {
                performClick();
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent event, float distanceX, float distanceY) {
            moveX = event.getX();
            moveY = event.getY();
            trackBitmapCanvas.drawLine(downX, downY, moveX, moveY, linePaint);
            downX = moveX;
            downY = moveY;
            invalidate();

            if (allowAcrossCallback) {
                if (hintViewGlobalVisibleRect == null) {
                    hintViewGlobalVisibleRect = new Rect();
                    hintView.getGlobalVisibleRect(hintViewGlobalVisibleRect);
                }
                if (hintViewGlobalVisibleRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    onAcrossHintViewListener.onAcrossHintView(hintView);
                    allowAcrossCallback = false;
                }
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(isAutoClean)return false;
            int count = computeScratchOutAreaSize();
            MLog.log("count:"+count);
            if(autoCleanPercent <= count){
                isAutoClean = true;
                if(onFinishListener!=null){
                    onFinishListener.onFinish();
                }
                setEnabled(false);
                setClickable(false);
            }
            return false;
        }
    }

    // 计算刮开的面积 遍历判断bitmap中是0的像素点比例（为0的就是透明区域）
    private int computeScratchOutAreaSize() {
        if (trackBitmap == null)
            return 0;
        int[] pixels = new int[trackBitmap.getWidth()
                * trackBitmap.getHeight()];
        int width = getWidth();
        int height = getHeight();
        trackBitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int sum = pixels.length;
        int num = 0;
        for (int pixel : pixels) {
            if (pixel == 0) {
                num++;
            }
        }

        return 100 - num * 100 / sum;
    }
    OnFinishListener onFinishListener;

    public void setOnFinishListener(OnFinishListener onFinishListener) {
        this.onFinishListener = onFinishListener;
    }

    public interface OnFinishListener{
        void onFinish();
    }
}