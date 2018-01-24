package com.mark.canvasdraw.VIew;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by marklin on 2018/1/23.
 */

public class DrawCanvas extends View {
    public static final String TAG = DrawCanvas.class.getSimpleName();

    private float mSignatureWidth = 8f;
    private Bitmap mSignature = null;

    private static final boolean GESTURE_RENDERING_ANTIALIAS = true;
    private static final boolean DITHER_FLAG = true;

    private Paint mPaint = new Paint();
    private Path mPath = new Path();

    private final Rect mInvalidRect = new Rect();

    private float mX;
    private float mY;

    private float mCurveEndX;
    private float mCurveEndY;

    private int mInvalidateExtraBorder = 10;
    private ArrayList<Bitmap> bitmapList = new ArrayList<>();

    public DrawCanvas(Context context) {
        super(context);
        init();
    }

    public DrawCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawCanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DrawCanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setWillNotDraw(false);

        mPaint.setAntiAlias(GESTURE_RENDERING_ANTIALIAS);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mSignatureWidth);
        mPaint.setDither(DITHER_FLAG);
        mPath.reset();
    }

    public void initPaint(float paintWidth, int paintColor) {
        mPaint.setColor(paintColor);
        mPaint.setStrokeWidth(paintWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bitmapList.size() > 0) {
            canvas.drawBitmap(bitmapList.get(bitmapList.size() - 1), null, new Rect(0, 0, getWidth(), getHeight()), null);
        }
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        processEvent(event);
        return true;
    }

    private boolean processEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDown(event);
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                Rect rect = touchMove(event);
                if (rect != null) {
                    invalidate(rect);
                }
                return true;
            case MotionEvent.ACTION_UP:
                touchUp();
                return true;
        }

        return false;

    }

    private void touchUp() {
        final Bitmap bmp = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        if (bitmapList.size() > 0) {
            c.drawBitmap(bitmapList.get(bitmapList.size() - 1), null, new Rect(0, 0, getWidth(), getHeight()), null);
        }

        c.drawPath(mPath, mPaint);
        bitmapList.add(bmp);

        clear();
    }

    private Rect touchMove(MotionEvent event) {
        Rect areaToRefresh = null;

        final float x = event.getX();
        final float y = event.getY();

        final float previousX = mX;
        final float previousY = mY;

        areaToRefresh = mInvalidRect;

        // start with the curve end
        final int border = mInvalidateExtraBorder;
        areaToRefresh.set((int) mCurveEndX - border, (int) mCurveEndY - border, (int) mCurveEndX + border, (int) mCurveEndY + border);

        float cX = mCurveEndX = (x + previousX) / 2;
        float cY = mCurveEndY = (y + previousY) / 2;

        mPath.quadTo(previousX, previousY, cX, cY);

        // union with the control point of the new curve
        areaToRefresh.union((int) previousX - border, (int) previousY - border, (int) previousX + border, (int) previousY + border);

        // union with the end point of the new curve
        areaToRefresh.union((int) cX - border, (int) cY - border, (int) cX + border, (int) cY + border);

        mX = x;
        mY = y;

        return areaToRefresh;

    }

    private void touchDown(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        mX = x;
        mY = y;
        mPath.moveTo(x, y);

        final int border = mInvalidateExtraBorder;
        mInvalidRect.set((int) x - border, (int) y - border, (int) x + border, (int) y + border);

        mCurveEndX = x;
        mCurveEndY = y;

    }

    public void clear() {
        mSignature = null;
        mPath.rewind();
        invalidate();
    }

    public void setSignatureBitmap(Bitmap signature) {
        mSignature = signature;
        invalidate();
    }

    public Bitmap getBitmap() {
        if (bitmapList.size() == 0) {
            return null;
        } else {
            return bitmapList.get(bitmapList.size() - 1);
        }
    }

    public Bitmap getSignatureBitmap() {
        if (mSignature != null) {
            return mSignature;
        } else if (mPath.isEmpty()) {
            return null;
        } else {
            Bitmap bmp = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bmp);
            c.drawPath(mPath, mPaint);
            return bmp;
        }
    }

    public void setSignatureWidth(float width) {
        mSignatureWidth = width;
        mPaint.setStrokeWidth(mSignatureWidth);
        invalidate();
    }

    public float getSignatureWidth() {
        return mPaint.getStrokeWidth();
    }

    /**
     * @return the byte array representing the signature as a PNG file format
     */
    public byte[] getSignaturePNG() {
        return getSignatureBytes(Bitmap.CompressFormat.PNG, 0);
    }

    /**
     * @param quality Hint to the compressor, 0-100. 0 meaning compress for small
     *                size, 100 meaning compress for max quality.
     * @return the byte array representing the signature as a JPEG file format
     */
    public byte[] getSignatureJPEG(int quality) {
        return getSignatureBytes(Bitmap.CompressFormat.JPEG, quality);
    }

    private byte[] getSignatureBytes(Bitmap.CompressFormat format, int quality) {
        Bitmap bmp = getSignatureBitmap();
        if (bmp == null) {
            return null;
        } else {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            getSignatureBitmap().compress(format, quality, stream);

            return stream.toByteArray();
        }
    }

    public Paint getPaint() {
        return mPaint;
    }

    public void undoCanvas() {
        if (bitmapList.size() > 0) {
            bitmapList.remove(bitmapList.size() - 1);
        }

        invalidate();
    }
}
