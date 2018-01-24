package com.mark.canvasdraw.VIew;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.mark.canvasdraw.R;

/**
 * Created by marklin on 2018/1/24.
 */

public class DrawBoard extends RelativeLayout {
    public static final String TAG = DrawBoard.class.getSimpleName();

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
    }
}
