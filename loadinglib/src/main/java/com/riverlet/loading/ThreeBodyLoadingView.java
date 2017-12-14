package com.riverlet.loading;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Author riverlet.liu
 * Email: riverlet.liu@mopo.com
 * Date: 2017/12/5.
 * Despribe:没错，三体粉
 */

public class ThreeBodyLoadingView extends View {
    private static final String TAG = "ThreeBodyLoadingView";
    private Body[] originalBodys = new Body[3];
    private Body[] bodys = new Body[3];
    private ObjectAnimator animator;
    private float rate;
    private int movingDistance;
    private int radiusRange;
    private int[] drawOrders = new int[3];

    public ThreeBodyLoadingView(Context context) {
        super(context);
        init(null);
    }

    public ThreeBodyLoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ThreeBodyLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ThreeBodyLoadingView);
        int color01 = typedArray.getColor(R.styleable.ThreeBodyLoadingView_firstColor, 0xFFFF0000);
        int color02 = typedArray.getColor(R.styleable.ThreeBodyLoadingView_secondColor, 0xFF1C86EE);
        int color03 = typedArray.getColor(R.styleable.ThreeBodyLoadingView_thirdColor, 0xFFEE9A00);

        int[] colors = new int[]{color01, color02, color03};
        for (int i = 0; i < bodys.length; i++) {
            bodys[i] = new Body();
            bodys[i].paint = new Paint();
            bodys[i].paint.setAntiAlias(true);
            bodys[i].paint.setStyle(Paint.Style.FILL);
            bodys[i].paint.setColor(colors[i]);
            originalBodys[i] = new Body();
        }

        animator = ObjectAnimator.ofFloat(this, "rate", 0.0f, 3.0f);
        animator.setDuration(1500);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);
        animator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        computeOriginalPosition(getMeasuredWidth(), getMeasuredHeight());
    }

    private void computeOriginalPosition(int width, int height) {
        int cellWith = width / 6;
        if (cellWith > height * 1.25f) {
            cellWith = height;
        }

        originalBodys[1].centerX = width / 2;
        originalBodys[1].centerY = height / 2;
        originalBodys[1].radius = cellWith / 2;

        originalBodys[0].centerX = bodys[1].centerX - cellWith * 2;
        originalBodys[0].centerY = bodys[1].centerY;
        originalBodys[0].radius = cellWith / 2;

        originalBodys[2].centerX = bodys[1].centerX + cellWith * 2;
        originalBodys[2].centerY = bodys[1].centerY;
        originalBodys[2].radius = cellWith / 2;

        movingDistance = bodys[2].centerX - bodys[0].centerX;
        radiusRange = cellWith / 4;

        bodys[0].centerY = originalBodys[0].centerY;
        bodys[1].centerY = originalBodys[1].centerY;
        bodys[2].centerY = originalBodys[2].centerY;
        computePosition();
    }

    private void computePosition() {
        if (rate <= 1.0f) {
            bodys[0].centerX = (int) (originalBodys[0].centerX + movingDistance * 0.5f * rate);
            bodys[0].radius = (int) (originalBodys[0].radius + radiusRange * rate);

            bodys[1].centerX = (int) (originalBodys[1].centerX + movingDistance * 0.5f * rate);
            bodys[1].radius = (int) (originalBodys[1].radius + radiusRange * (1 - rate));

            bodys[2].centerX = (int) (originalBodys[2].centerX - movingDistance * rate);
            bodys[2].radius = originalBodys[2].radius;

            drawOrders[0] = 2;
            drawOrders[1] = 1;
            drawOrders[2] = 0;
        } else if (rate > 1.0f && rate <= 2.0f) {
//            bodys[0].centerX = (int) (originalBodys[0].centerX + movingDistance * 0.5f + movingDistance * 0.5f * (rate - 1));
//            合并运算，减少计算量
            bodys[0].centerX = (int) (originalBodys[0].centerX + movingDistance * 0.5f * rate);
            bodys[0].radius = (int) (originalBodys[0].radius + radiusRange * (2 - rate));

//            bodys[1].centerX = (int) (originalBodys[1].centerX + movingDistance * 0.5f - movingDistance * (rate - 1));
//            合并运算，减少计算量
            bodys[1].centerX = (int) (originalBodys[1].centerX + movingDistance * (1.5f - rate));
            bodys[1].radius = originalBodys[1].radius;

//            bodys[2].centerX = (int) (originalBodys[2].centerX - movingDistance + movingDistance * 0.5f * (rate - 1));
//            合并运算，减少计算量
            bodys[2].centerX = (int) (originalBodys[2].centerX - movingDistance * (1.5f - 0.5f * rate));
            bodys[2].radius = (int) (originalBodys[2].radius + radiusRange * (rate - 1));

            drawOrders[0] = 1;
            drawOrders[1] = 0;
            drawOrders[2] = 2;
        } else if (rate > 2.0f && rate <= 3.0f) {
//            bodys[0].centerX = (int) (originalBodys[0].centerX + movingDistance - movingDistance * (rate - 2));
//            合并运算，减少计算量
            bodys[0].centerX = (int) (originalBodys[0].centerX + movingDistance * (3 - rate));
            bodys[0].radius = originalBodys[0].radius;

//            bodys[1].centerX = (int) (originalBodys[1].centerX - movingDistance * 0.5f + movingDistance * 0.5f * (rate - 2));
//            合并运算，减少计算量
            bodys[1].centerX = (int) (originalBodys[1].centerX - movingDistance * 0.5f * (3 - rate));
            bodys[1].radius = (int) (originalBodys[1].radius + radiusRange * (rate - 2));

//            bodys[2].centerX = (int) (originalBodys[2].centerX - movingDistance * 0.5f + movingDistance * 0.5f * (rate - 2));
//            合并运算，减少计算量
            bodys[2].centerX = (int) (originalBodys[2].centerX - movingDistance * 0.5f * (3 - rate));
            bodys[2].radius = (int) (originalBodys[2].radius + radiusRange - radiusRange * (rate - 2));

            drawOrders[0] = 0;
            drawOrders[1] = 1;
            drawOrders[2] = 2;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < drawOrders.length; i++) {
            canvas.drawCircle(bodys[drawOrders[i]].centerX, bodys[drawOrders[i]].centerY, bodys[drawOrders[i]].radius, bodys[drawOrders[i]].paint);
        }
        Log.d(TAG, originalBodys[2].centerX + "..............." + bodys[2].centerX);
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
        refresh();
    }

    private void refresh() {
        computePosition();
        invalidate();
    }

    private void animStart() {
        if (animator != null && !animator.isStarted()) {
            animator.start();
        }
    }

    private void animStop() {
        if (animator != null && animator.isStarted()) {
            animator.cancel();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        animStart();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        animStop();
    }

    private static class Body {
        Paint paint;
        int centerX;
        int centerY;
        int radius;
    }

}
