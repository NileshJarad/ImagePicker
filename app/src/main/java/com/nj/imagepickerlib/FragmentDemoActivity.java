package com.nj.imagepickerlib;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class FragmentDemoActivity extends AppCompatActivity {

    private ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_demo);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, new ImageOptionFromFragment())
                .commitAllowingStateLoss();

    }


}
