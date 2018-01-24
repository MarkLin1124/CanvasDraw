package com.mark.canvasdraw;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * Created by marklin on 2018/1/24.
 */

public class ResultActivity extends AppCompatActivity {
    public static final String TAG = ResultActivity.class.getSimpleName();

    private String filePath = "";
    private ImageView imgCanvas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        imgCanvas = (ImageView) findViewById(R.id.img_canvas);
        filePath = getIntent().getExtras().getString(ResultActivity.TAG, "");
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        imgCanvas.setImageBitmap(bitmap);
    }
}
