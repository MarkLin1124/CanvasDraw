package com.mark.canvasdraw;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mark.canvasdraw.VIew.DrawBoard;
import com.mark.canvasdraw.VIew.DrawCanvas;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final int REQUEST_PERMISSION = 1000;

    private Button btnExport;
    private DrawBoard drawBoard;
    private DrawCanvas drawCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawBoard = (DrawBoard) findViewById(R.id.draw_board);
        drawCanvas = drawBoard.getCanvas();

        btnExport = (Button) findViewById(R.id.btn_export);
        btnExport.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (requestExternalStoragePermission()) {
            export(drawCanvas);
        }
    }

    private void export(final DrawCanvas drawCanvas) {
        File f = new File(getCacheDir(), Long.toString(System.currentTimeMillis()) + ".png");
        try {
            f.createNewFile();

            Bitmap bitmap = drawCanvas.getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent();
        intent.setClass(MainActivity.this, ResultActivity.class);
        intent.putExtra(ResultActivity.TAG, f.getAbsolutePath());
        startActivity(intent);
    }

    private boolean requestExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    export(drawCanvas);
                }
                break;
        }
    }
}
