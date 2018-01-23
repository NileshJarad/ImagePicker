package com.nj.imagepicker;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nj.imagepicker.listener.ImageMultiResultListener;
import com.nj.imagepicker.listener.ImageResultListener;
import com.nj.imagepicker.result.ImageResult;
import com.nj.imagepicker.utils.DialogConfiguration;
import com.nj.imagepicker.utils.IntentUtils;
import com.nj.imagepicker.utils.ResultHelper;


/**
 * Created by nileshjarad on 21/02/17.
 * Image picker dialog to show the image choose options
 */

public class ImagePicker extends DialogFragment implements View.OnClickListener {

    private static final String LOG_TAG = "ImagePicker";
    private static final String ARG_SETUP_CONFIGURATION = "dialog_configuration";
    private static final String TAG_FOR_DIALOG_FRAGMENT = "tag_for_dialog";
    private DialogConfiguration dialogConfiguration;
    private CardView cvOption;
    private TextView tvTitle;
    private LinearLayout llOptionHolder;
    private AppCompatButton buttonCancel;
    private AppCompatButton buttonCamera;
    private AppCompatButton buttonGallery;
    private IntentUtils intentUtils;
    private LinearLayout llOptionContainer;
    private ImageResultListener callback;
    private ImageMultiResultListener callbackMultiImage;
    private boolean isMultiSelect;

    private static ImagePicker newInstance(DialogConfiguration dialogConfiguration) {
        Bundle args = new Bundle();
        ImagePicker fragment = new ImagePicker();
        args.putSerializable(ARG_SETUP_CONFIGURATION, dialogConfiguration);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        initializeDialog();
        return inflater.inflate(R.layout.dialog_image_picker, null, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        setupConfiguration();

    }

    /**
     * Function finds view in layout and set listener
     */
    private void findViews(View view) {
        cvOption = (CardView) view.findViewById(R.id.cv_options);
        llOptionContainer = (LinearLayout) view.findViewById(R.id.ll_option_container);
        llOptionHolder = (LinearLayout) view.findViewById(R.id.ll_buttons_holder);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        buttonCamera = (AppCompatButton) view.findViewById(R.id.button_camera);
        buttonGallery = (AppCompatButton) view.findViewById(R.id.button_gallery);
        buttonCancel = (AppCompatButton) view.findViewById(R.id.button_cancel);
        buttonCancel.setOnClickListener(this);
        buttonCamera.setOnClickListener(this);
        buttonGallery.setOnClickListener(this);
    }

    /**
     * Set the configuration about color, size, icon gravity, options orientation.
     * see {@link DialogConfiguration} for more configuration option
     */
    private void setupConfiguration() {
        tvTitle.setText(dialogConfiguration.getTitle());
        tvTitle.setTextColor(dialogConfiguration.getTitleTextColor());

        llOptionHolder.setOrientation(dialogConfiguration.getOptionOrientation() == LinearLayoutCompat.HORIZONTAL
                ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);

        cvOption.setCardBackgroundColor(dialogConfiguration.getBackgroundColor());

        buttonCancel.setText(dialogConfiguration.getNegativeText());
        buttonCancel.setTextColor(dialogConfiguration.getNegativeTextColor());

        buttonGallery.setTextColor(dialogConfiguration.getOptionsTextColor());
        buttonCamera.setTextColor(dialogConfiguration.getOptionsTextColor());

    }

    /**
     * setup initial dialog and get configuration from arguments
     */
    private void initializeDialog() {
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            setCancelable(false);
        }
        this.dialogConfiguration = (DialogConfiguration) getArguments().getSerializable(ARG_SETUP_CONFIGURATION);
        this.intentUtils = new IntentUtils(getActivity());
    }

    public static ImagePicker build(DialogConfiguration dialogConfiguration, ImageResultListener callback) {
        ImagePicker dialog = newInstance(dialogConfiguration);
        dialog.setImageSuccessListener(callback);
        return dialog;
    }


    public static ImagePicker build(DialogConfiguration dialogConfiguration, ImageMultiResultListener callback) {
        ImagePicker dialog = newInstance(dialogConfiguration);
        dialog.setImageMultiSuccessListener(callback);
        return dialog;
    }

    public static ImagePicker build(ImageResultListener callback) {
        return build(new DialogConfiguration(), callback);
    }

    public ImagePicker show(FragmentManager fragmentManager) {
        super.show(fragmentManager, TAG_FOR_DIALOG_FRAGMENT);
        return this;
    }

    @Override
    public void onClick(View v) {
        if (v == buttonCancel) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismissAllowingStateLoss();
                }
            }, 200);
        } else if (v == buttonCamera) {
            launchCameraIntent();
        } else if (v == buttonGallery) {
            launchGalleryIntent();
        }
    }

    private void launchGalleryIntent() {
        if (intentUtils.requestReadExternalStoragePermissions(this)) {
            intentUtils.launchImagePickIntent(this, IntentUtils.GALLERY, isMultiSelect);
        }
    }

    private void launchCameraIntent() {
        if (intentUtils.requestCameraPermissions(this)) {
            intentUtils.launchImagePickIntent(this, IntentUtils.CAMERA, isMultiSelect);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dismissAllowingStateLoss();
        if (resultCode == Activity.RESULT_OK && requestCode == IntentUtils.REQUEST_CODE_CAMERA_PERMISSION) {


            if (callback != null) {
                ImageResult imageResult = ResultHelper.prepareResultData(getActivity(), data, intentUtils
                        , dialogConfiguration);
                callback.onImageResult(imageResult);
            } else {
                // multi image listener event
                callbackMultiImage.onImageResult(ResultHelper.prepareMultiResultData(getActivity(), data, intentUtils
                        , dialogConfiguration));
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        boolean granted = true;

        for (Integer i : grantResults)
            granted = granted && i == PackageManager.PERMISSION_GRANTED;

        if (granted) {
            if (requestCode == IntentUtils.REQUEST_CODE_CAMERA_PERMISSION) {
                launchCameraIntent();
            } else if (requestCode == IntentUtils.REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION) {
                launchGalleryIntent();
            }
        } else {
            dismissAllowingStateLoss();
        }
    }

    public void setImageSuccessListener(ImageResultListener callback) {
        this.callback = callback;
        this.callbackMultiImage = null;
        this.isMultiSelect = false;
    }

    public void setImageMultiSuccessListener(ImageMultiResultListener callbackMultiImage) {
        this.callbackMultiImage = callbackMultiImage;
        this.callback = null;
        this.isMultiSelect = true;
    }
}
