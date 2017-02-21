package com.nj.imagepickerlib;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.ImageView;

import com.nj.imagepicker.ImagePicker;
import com.nj.imagepicker.listener.ImageResultListener;
import com.nj.imagepicker.result.ImageResult;
import com.nj.imagepicker.utils.DialogConfiguration;

public class MainActivity extends AppCompatActivity {

    private ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivImage = (ImageView) findViewById(R.id.iv_image);

    }

    public void show(View v) {
        ImagePicker.build(new DialogConfiguration()
                .setTitle("Choose")
                .setOptionOrientation(LinearLayoutCompat.HORIZONTAL), new ImageResultListener() {
            @Override
            public void onImageResult(ImageResult imageResult) {
                ivImage.setImageBitmap(imageResult.getBitmap());
            }
        }).show(getSupportFragmentManager());
    }
}
