package com.mark.canvasdraw.VIew;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import com.mark.canvasdraw.R;

/**
 * Created by marklin on 2018/1/24.
 */

public class CircleView extends android.support.v7.widget.AppCompatImageView {
    public static final String TAG = CircleView.class.getSimpleName();

    private Paint paint = new Paint();
    private Paint backgroundPaint = new Paint();

    private int circleColor = 0;
    private boolean isSelect = false;
    private float radius = 0;

    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        if (ta.hasValue(R.styleable.CircleView_paint)) {
            circleColor = ta.getColor(R.styleable.CircleView_paint, Color.BLACK);
        }
        if (ta.hasValue(R.styleable.CircleView_select)) {
            isSelect = ta.getBoolean(R.styleable.CircleView_select, false);
        }
        if (ta.hasValue(R.styleable.CircleView_radius)) {
            radius = ta.getDimensionPixelSize(R.styleable.CircleView_radius, 0);
        }

        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(circleColor);
        backgroundPaint.setColor(getResources().getColor(R.color.select_background));

        if (isSelect) {
            canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, (canvas.getWidth() / 2), backgroundPaint);
        }

        if (circleColor != 0) {
            float circleRadius = radius == 0 ? (canvas.getWidth() / 2) - getResources().getDimension(R.dimen.circle_padding) : radius;
            canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, circleRadius, paint);
        }
    }

    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
        invalidate();
    }

}
