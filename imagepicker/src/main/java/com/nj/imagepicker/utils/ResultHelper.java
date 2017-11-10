package com.nj.imagepicker.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import com.nj.imagepicker.result.ImageResult;

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

    private static int getRotationFromCamera(Uri uri) {
        int rotate = 0;
        try {

            ExifInterface exif = new ExifInterface(uri.getPath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                default:
                    rotate = 0;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
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
//                return getResizedBitmap(context, contentUri, dialogConfiguration);

                return scaleDown(context, dialogConfiguration, contentUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Bitmap scaleDown(Context context, DialogConfiguration dialogConfiguration, Uri contentUri) throws IOException {
        Bitmap realImage = MediaStore.Images.Media.getBitmap(context.getContentResolver(), contentUri);
        float ratio = Math.min(
                (float) dialogConfiguration.getImageWidth() / realImage.getWidth(),
                (float) dialogConfiguration.getImageHeight() / realImage.getHeight());
        if (ratio < 1) {// this ensure the only downscale image
            int width = Math.round(ratio * realImage.getWidth());
            int height = Math.round(ratio * realImage.getHeight());
            realImage = Bitmap.createScaledBitmap(realImage, width,
                    height, false);
        }


        return rotate(realImage, getRotationFromCamera(contentUri));

    }

    private static Bitmap rotate(Bitmap bitmap, int degrees) {
        if (bitmap != null && degrees != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(degrees);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }

        return bitmap;
    }

//    private static Bitmap getResizedBitmap(Context context, Uri contentUri, DialogConfiguration dialogConfiguration) throws IOException {
//        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), contentUri);
////        return bitmap;
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        byte[] byteArray = stream.toByteArray();
//        Bitmap b = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//        return Bitmap.createScaledBitmap(b, dialogConfiguration.getImageWidth(),
//                dialogConfiguration.getImageHeight(), false);
//    }

}
