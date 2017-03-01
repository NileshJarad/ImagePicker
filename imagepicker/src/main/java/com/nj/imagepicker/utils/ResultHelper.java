package com.nj.imagepicker.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.nj.imagepicker.result.ImageResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by nileshjarad on 21/02/17.
 */

public class ResultHelper {

    private static final String LOG_TAG = "ResultHelper";


    public static ImageResult prepareResultData(Context context, Intent intent,
                                                IntentUtils intentUtils, DialogConfiguration dialogConfiguration) {
        ImageResult imageResult = new ImageResult();
        Uri uri;
        String path;
        if (intentUtils.getIntentType() == IntentUtils.CAMERA) {
            uri = intentUtils.getCameraUri();
            path = uri.getPath();
        } else {
            uri = intent.getData();
            path = getRealPathFromURI(context, uri);
        }

        imageResult.setUri(uri);
        imageResult.setPath(path);
        imageResult.setBitmap(getBitmap(context, uri, dialogConfiguration));
        return imageResult;
    }

    private static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private static Bitmap getBitmap(Context context, Uri contentUri, DialogConfiguration dialogConfiguration) {
        try {
            if (dialogConfiguration.getImageWidth() == DialogConfiguration.DEFAULT_HEIGHT_WIDTH) {
                return MediaStore.Images.Media.getBitmap(context.getContentResolver(), contentUri);
            } else {
                return getResizedBitmap(context, contentUri, dialogConfiguration);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Bitmap getResizedBitmap(Context context, Uri contentUri, DialogConfiguration dialogConfiguration) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), contentUri);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Bitmap b = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return Bitmap.createScaledBitmap(b, dialogConfiguration.getImageWidth(),
                dialogConfiguration.getImageHeight(), false);
    }
}
