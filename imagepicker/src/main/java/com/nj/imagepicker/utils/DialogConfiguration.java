package com.nj.imagepicker.utils;

import android.graphics.Color;
import android.support.annotation.IntDef;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;

import java.io.Serializable;

/**
 * Created by nileshjarad on 21/02/17.
 * Configuration for text appearance, size, color background, orientation, gravity
 */

public class DialogConfiguration implements Serializable {

    public static final int DEFAULT_HEIGHT_WIDTH = -99;
    private String title;

    @IntDef({Gravity.START, Gravity.BOTTOM, Gravity.END, Gravity.TOP})
    public @interface IconGravity {
    }

    @IconGravity
    private int iconGravity;

    @LinearLayoutCompat.OrientationMode
    private int optionOrientation;
    private int backgroundColor;
    private String negativeText;
    private int negativeTextColor;
    private int optionsTextColor;
    private int titleTextColor;
    private int imageWidth = 720;
    private int imageHeight= 1280;


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


    public int getBackgroundColor() {
        return backgroundColor;
    }

    public DialogConfiguration setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public String getNegativeText() {
        return negativeText;
    }

    public DialogConfiguration setNegativeText(String negativeText) {
        this.negativeText = negativeText;
        return this;
    }

    public int getNegativeTextColor() {
        return negativeTextColor;
    }

    public DialogConfiguration setNegativeTextColor(int negativeTextColor) {
        this.negativeTextColor = negativeTextColor;
        return this;
    }

    public int getOptionsTextColor() {
        return optionsTextColor;
    }

    public DialogConfiguration setOptionsTextColor(int optionsTextColor) {
        this.optionsTextColor = optionsTextColor;
        return this;
    }

    public int getTitleTextColor() {
        return titleTextColor;
    }

    public DialogConfiguration setTitleTextColor(int titleTextColor) {
        this.titleTextColor = titleTextColor;
        return this;
    }

    public DialogConfiguration setResultImageDimension(int imageWidth, int imageHeight) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        return this;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    // default configuration setup
    public DialogConfiguration() {
        setTitle("Choose option")
                .setIconGravity(Gravity.END)
                .setOptionOrientation(LinearLayoutCompat.HORIZONTAL)
                .setBackgroundColor(Color.WHITE)
                .setNegativeText("Cancel")
                .setNegativeTextColor(Color.BLACK)
                .setTitleTextColor(Color.BLACK)
                .setOptionsTextColor(Color.BLACK)
                .setResultImageDimension(DEFAULT_HEIGHT_WIDTH, DEFAULT_HEIGHT_WIDTH);
    }


}

