package com.nj.imagepicker.result;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by nileshjarad on 21/02/17.
 */

public class ImageResult {
    private Bitmap bitmap;
    private Uri uri;
    private String path;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
