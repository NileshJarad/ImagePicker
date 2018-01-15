package com.nj.imagepicker.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.nj.imagepicker.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nileshjarad on 21/02/17.
 */

public class IntentUtils {

    private static final String LOG_TAG = IntentUtils.class.getSimpleName();

    public static final int CAMERA = 0;
    public static final int GALLERY = 1;
    public static final int REQUEST_CODE = 1000;
    private Activity activity;
    private File photoFile;
    private int intentType;
    private Uri photoURI;


    public IntentUtils(Activity activity) {
        this.activity = activity;
    }


    public void launchImagePickIntent(Fragment listener, int intentType, boolean isMultiSelect) {
        listener.startActivityForResult(getIntentWithPackage(intentType, isMultiSelect), REQUEST_CODE);
    }

    private Intent getIntentWithPackage(int intentType, boolean isMultiSelect) {
        this.intentType = intentType;
        Intent intent = intentType == GALLERY ? getGalleryIntent(isMultiSelect) : getCameraIntent();
        List<ResolveInfo> resInfo = activity.getPackageManager()
                .queryIntentActivities(intent,
                        PackageManager.MATCH_SYSTEM_ONLY);

        if (!resInfo.isEmpty()) {
            String packageName = resInfo.get(0).activityInfo.packageName;
            if (intentType == CAMERA) {
                activity.grantUriPermission(packageName, photoURI,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            intent.setPackage(packageName);
        }

        return intent;
    }


    private Intent getGalleryIntent(boolean isMultiSelect) {
        Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intentGallery.setType("image/*");
        if (isMultiSelect) {
            // set this only if multi select intent listener is attached
            intentGallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intentGallery.setAction(Intent.ACTION_GET_CONTENT);
        }
        return intentGallery;
    }


    /**
     * Camera intent function
     */
    public Intent getCameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        // Create the File where the photo should go
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
            Log.e(LOG_TAG, "getCameraIntent: IOException:" + ex.toString());
        }

        if (photoFile != null) {
            photoURI = FileProvider.getUriForFile(activity,
                    activity.getPackageName() + activity.getString(R.string.provider),
                    photoFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        }

        return cameraIntent;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getFilesDir();
        File image = null;
        image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private String[] getCameraPermissions() {
        return new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    /**
     * request permission to use camera and write files if not already not granted
     */
    public boolean requestCameraPermissions(Fragment listener) {
        List<String> list = new ArrayList<>();

        for (String permission : getCameraPermissions())
            if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED)
                list.add(permission);

        if (list.isEmpty())
            return true;

        listener.requestPermissions(list.toArray(new String[list.size()]), REQUEST_CODE);
        return false;
    }

    int getIntentType() {
        return intentType;
    }

    Uri getCameraUri() {
        return Uri.fromFile(photoFile);
    }

}
