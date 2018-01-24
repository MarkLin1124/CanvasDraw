package com.mark.canvasdraw.VIew;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.mark.canvasdraw.R;

import java.util.ArrayList;

/**
 * Created by marklin on 2018/1/24.
 */

public class DrawBoard extends RelativeLayout implements View.OnClickListener {
    public static final String TAG = DrawBoard.class.getSimpleName();

    private CircleView circleRed, circleOrange, circleYellow, circleGreen, circleBlue, circleDarkBlue, circlePurple, circleBlack, circleWhite, circleBrown;
    private ArrayList<CircleView> colorList = new ArrayList<>();

    private CircleView paintSmall, paintNormal, paintLarge;
    private ArrayList<CircleView> sizeList = new ArrayList<>();

    private CircleView circlePaint, circleEraser, circleUndo;
    private ArrayList<CircleView> useList = new ArrayList<>();

    private DrawCanvas mCanvas;
    private int selectColor = 0;

    public DrawBoard(Context context) {
        super(context);
        initView(context);
    }

    public DrawBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DrawBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public DrawBoard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.draw_board, this, true);
        mCanvas = (DrawCanvas) findViewById(R.id.canvas);

        //default red
        circleRed = (CircleView) findViewById(R.id.circle_red);
        circleRed.setOnClickListener(this);
        circleRed.setTag(R.color.red);
        circleRed.setSelect(true);
        colorList.add(circleRed);

        circleOrange = (CircleView) findViewById(R.id.circle_orange);
        circleOrange.setOnClickListener(this);
        circleOrange.setTag(R.color.orange);
        colorList.add(circleOrange);

        circleYellow = (CircleView) findViewById(R.id.circle_yellow);
        circleYellow.setOnClickListener(this);
        circleYellow.setTag(R.color.yellow);
        colorList.add(circleYellow);

        circleGreen = (CircleView) findViewById(R.id.circle_green);
        circleGreen.setOnClickListener(this);
        circleGreen.setTag(R.color.green);
        colorList.add(circleGreen);

        circleBlue = (CircleView) findViewById(R.id.circle_blue);
        circleBlue.setOnClickListener(this);
        circleBlue.setTag(R.color.blue);
        colorList.add(circleBlue);

        circleDarkBlue = (CircleView) findViewById(R.id.circle_dark_blue);
        circleDarkBlue.setOnClickListener(this);
        circleDarkBlue.setTag(R.color.dark_blue);
        colorList.add(circleDarkBlue);

        circlePurple = (CircleView) findViewById(R.id.circle_purple);
        circlePurple.setOnClickListener(this);
        circlePurple.setTag(R.color.purple);
        colorList.add(circlePurple);

        circleBlack = (CircleView) findViewById(R.id.circle_black);
        circleBlack.setOnClickListener(this);
        circleBlack.setTag(android.R.color.black);
        colorList.add(circleBlack);

        circleWhite = (CircleView) findViewById(R.id.circle_white);
        circleWhite.setOnClickListener(this);
        circleWhite.setTag(android.R.color.white);
        colorList.add(circleWhite);

        circleBrown = (CircleView) findViewById(R.id.circle_brown);
        circleBrown.setOnClickListener(this);
        circleBrown.setTag(R.color.brown);
        colorList.add(circleBrown);

        //default small
        paintSmall = (CircleView) findViewById(R.id.paint_small);
        paintSmall.setOnClickListener(this);
        paintSmall.setSelect(true);
        paintSmall.setTag(R.dimen.circle_radius_small);
        sizeList.add(paintSmall);

        paintNormal = (CircleView) findViewById(R.id.paint_normal);
        paintNormal.setOnClickListener(this);
        paintNormal.setTag(R.dimen.circle_radius_normal);
        sizeList.add(paintNormal);

        paintLarge = (CircleView) findViewById(R.id.paint_large);
        paintLarge.setOnClickListener(this);
        paintLarge.setTag(R.dimen.circle_radius_large);
        sizeList.add(paintLarge);

        //default draw paint
        circlePaint = (CircleView) findViewById(R.id.paint);
        circlePaint.setOnClickListener(this);
        circlePaint.setSelect(true);
        useList.add(circlePaint);

        circleEraser = (CircleView) findViewById(R.id.eraser);
        circleEraser.setOnClickListener(this);
        useList.add(circleEraser);

        circleUndo = (CircleView) findViewById(R.id.undo);
        circleUndo.setOnClickListener(this);

        //default paint color
        mCanvas.initPaint(getResources().getDimension((int) paintSmall.getTag()), getResources().getColor((int) circleRed.getTag()));
        selectColor = getResources().getColor((int) circleRed.getTag());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.circle_red:
            case R.id.circle_orange:
            case R.id.circle_yellow:
            case R.id.circle_green:
            case R.id.circle_blue:
            case R.id.circle_dark_blue:
            case R.id.circle_purple:
            case R.id.circle_black:
            case R.id.circle_white:
            case R.id.circle_brown:
                onColorSelect((CircleView) view);
                break;
            case R.id.paint_small:
            case R.id.paint_normal:
            case R.id.paint_large:
                onPaintSelect((CircleView) view);
                break;
            case R.id.paint:
                onUseSelect((CircleView) view);
                mCanvas.getPaint().setColor(selectColor);
                break;
            case R.id.eraser:
                onUseSelect((CircleView) view);
                mCanvas.getPaint().setColor(getResources().getColor(R.color.board_background));
                break;
            case R.id.undo:
                mCanvas.undoCanvas();
                break;
        }
    }

    private void onColorSelect(CircleView circleView) {
        if (colorList.size() == 0) {
            return;
        }

        for (CircleView view : colorList) {
            if (view.getId() == circleView.getId()) {
                view.setSelect(true);
                selectColor = getResources().getColor((int) circleView.getTag());
                mCanvas.getPaint().setColor(selectColor);
            } else {
                view.setSelect(false);
            }
        }
    }

    private void onPaintSelect(CircleView circleView) {
        if (sizeList.size() == 0) {
            return;
        }

        for (CircleView view : sizeList) {
            if (view.getId() == circleView.getId()) {
                view.setSelect(true);
                mCanvas.getPaint().setStrokeWidth((getResources().getDimension((int) circleView.getTag())));
            } else {
                view.setSelect(false);
            }
        }
    }

    private void onUseSelect(CircleView circleView) {
        if (useList.size() == 0) {
            return;
        }

        for (CircleView view : useList) {
            if (view.getId() == circleView.getId()) {
                view.setSelect(true);
            } else {
                view.setSelect(false);
            }
        }
    }
}
