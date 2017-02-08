package com.mkd.zappos.love.ilovezappos.ui.customwidget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import static com.mkd.zappos.love.ilovezappos.R.styleable.CircleButton;
import static com.mkd.zappos.love.ilovezappos.R.styleable.CircleButton_cb_color;
import static com.mkd.zappos.love.ilovezappos.R.styleable.CircleButton_cb_pressedRingWidth;

/**
 * Created by mkdin on 03-02-2017.
 */

public class CustomSearchButton extends ImageView {

    private static final int PRESSED_COLOR_LIGHTUP = 255 / 25;
    private static final int PRESSED_RING_ALPHA = 75;
    private static final int UNPRESSED_ALPHA = 0;
    private static final int DEFAULT_PRESSED_RING_WIDTH_DIP = 2;
    private static final int ANIMATION_TIME_ID = android.R.integer.config_shortAnimTime;

    private int centerY;
    private int centerX;
    private int outerRadius;
    private int pressedRingRadius;

    private Paint focusPaint;

    private float animationProgress;

    private int pressedRingWidth;
    private int defaultColor = Color.BLACK;
    private ObjectAnimator pressedAnimator;

    public CustomSearchButton(Context context) {
        super(context);
        init(context, null);
    }

    public CustomSearchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomSearchButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);

        if (pressed) {
            showPressedRing();
        } else {
            hidePressedRing();
            focusPaint.setAlpha(UNPRESSED_ALPHA);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(centerX, centerY, pressedRingRadius + animationProgress, focusPaint);
        // canvas.drawCircle(centerX, centerY, outerRadius - pressedRingWidth, circlePaint);
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
        outerRadius = Math.min(w, h) / 2;
        pressedRingRadius = outerRadius - pressedRingWidth - pressedRingWidth / 2;
    }


    public void setColor(int color) {
        this.defaultColor = color;

        focusPaint.setColor(defaultColor);
        focusPaint.setAlpha(UNPRESSED_ALPHA);

        this.invalidate();
    }

    private void hidePressedRing() {
        pressedAnimator.setFloatValues(pressedRingWidth, 0f);
        pressedAnimator.start();

    }

    private void showPressedRing() {
        focusPaint.setAlpha(PRESSED_RING_ALPHA);
        pressedAnimator.setFloatValues(animationProgress, pressedRingWidth);
        pressedAnimator.start();
    }

    private void init(Context context, AttributeSet attrs) {
        this.setFocusable(true);
        this.setScaleType(ScaleType.CENTER_INSIDE);
        setClickable(true);


        focusPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        focusPaint.setStyle(Paint.Style.FILL);

        pressedRingWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_PRESSED_RING_WIDTH_DIP, getResources()
                .getDisplayMetrics());

        int color = Color.BLACK;
       if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, CircleButton);
            color = a.getColor(CircleButton_cb_color, color);
            pressedRingWidth = (int) a.getDimension(CircleButton_cb_pressedRingWidth, pressedRingWidth);
            a.recycle();
        }

        setColor(color);

        focusPaint.setStrokeWidth(pressedRingWidth);
        final int pressedAnimationTime = getResources().getInteger(ANIMATION_TIME_ID);
        pressedAnimator = ObjectAnimator.ofFloat(this, "animationProgress", 0f, 0f);
        pressedAnimator.setDuration(pressedAnimationTime);
    }
}
