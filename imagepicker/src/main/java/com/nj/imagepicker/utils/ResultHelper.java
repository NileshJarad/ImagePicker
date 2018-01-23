package com.nj.imagepicker.utils;

import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.nj.imagepicker.result.ImageResult;

import java.io.IOException;
import java.util.ArrayList;

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
            path = getPath(context, uri);
        }

        imageResult.setUri(uri);
        imageResult.setPath(path);
        imageResult.setBitmap(getBitmap(context, uri, dialogConfiguration));
        return imageResult;
    }

    public static ArrayList<ImageResult> prepareMultiResultData(Context context, Intent intent,
                                                                IntentUtils intentUtils, DialogConfiguration dialogConfiguration) {
        ArrayList<ImageResult> imageResults = new ArrayList<>();


        if (intentUtils.getIntentType() == IntentUtils.CAMERA) {
            Uri uri = intentUtils.getCameraUri();
            String path = uri.getPath();
            ImageResult imageResult = new ImageResult();
            imageResult.setUri(uri);
            imageResult.setPath(path);
            imageResult.setBitmap(getBitmap(context, uri, dialogConfiguration));
            imageResults.add(imageResult);

        } else {
            if (intent.getClipData() != null) {
                ClipData mClipData = intent.getClipData();
                for (int i = 0; i < mClipData.getItemCount(); i++) {
                    ClipData.Item item = mClipData.getItemAt(i);
                    Uri uri = item.getUri();
                    String path = getPath(context, uri);
                    ImageResult imageResult = new ImageResult();
                    imageResult.setUri(uri);
                    imageResult.setPath(path);
                    imageResult.setBitmap(getBitmap(context, uri, dialogConfiguration));
                    imageResults.add(imageResult);
                }
            } else if (intent.getData() != null) {
                Uri uri = intent.getData();
                String path = getPath(context, uri);
                ImageResult imageResult = new ImageResult();
                imageResult.setUri(uri);
                imageResult.setPath(path);
                imageResult.setBitmap(getBitmap(context, uri, dialogConfiguration));
                imageResults.add(imageResult);
            }
        }


        return imageResults;
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


    // new methods added for getting path
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                String id = DocumentsContract.getDocumentId(uri);
                id = id.replaceAll("[^a-zA-Z0-9]]", "");

                if (!TextUtils.isEmpty(id)) {
                    if (id.startsWith("raw:")) {
                        return id.replaceFirst("raw:", "");
                    }
                }
                Uri contentUri = uri;
                try {
                    contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                } catch (Exception e) {

                }

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static boolean isGoogleDrivePhoto(Uri uri){
        return "com.google.android.apps.docs.storage".equals(uri.getAuthority());
    }
}
