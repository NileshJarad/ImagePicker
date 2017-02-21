package com.nj.imagepicker.utils;

import android.support.annotation.IntDef;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;

import java.io.Serializable;

/**
 * Created by nileshjarad on 21/02/17.
 * Configuration for text appearance, size, color background, orientation, gravity
 */

public class DialogConfiguration implements Serializable {

    private String title;

    @IntDef({Gravity.START, Gravity.BOTTOM, Gravity.END, Gravity.TOP})
    public @interface IconGravity {
    }

    @IconGravity
    private int iconGravity;

    @LinearLayoutCompat.OrientationMode
    private int optionOrientation;


    public String getTitle() {
        return title;
    }

    public DialogConfiguration setTitle(String title) {
        this.title = title;
        return this;
    }

    @IconGravity
    public int getIconGravity() {
        return iconGravity;
    }

    private DialogConfiguration setIconGravity(@IconGravity int iconGravity) {
        this.iconGravity = iconGravity;
        return this;
    }

    @LinearLayoutCompat.OrientationMode
    public int getOptionOrientation() {
        return optionOrientation;
    }

    public DialogConfiguration setOptionOrientation(@LinearLayoutCompat.OrientationMode int optionOrientation) {
        this.optionOrientation = optionOrientation;
        return this;
    }

    // default configuration setup
    public DialogConfiguration() {
        setTitle("Choose option").setIconGravity(Gravity.END).setOptionOrientation(LinearLayoutCompat.HORIZONTAL);
    }


}

