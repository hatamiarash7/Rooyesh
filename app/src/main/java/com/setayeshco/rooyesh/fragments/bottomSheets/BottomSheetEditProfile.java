package com.setayeshco.rooyesh.fragments.bottomSheets;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.setayeshco.rooyesh.R;
import com.setayeshco.rooyesh.activities.images.PickerBuilder;
import com.setayeshco.rooyesh.app.AppConstants;
import com.setayeshco.rooyesh.helpers.AppHelper;
import com.setayeshco.rooyesh.helpers.Files.FilesManager;
import com.setayeshco.rooyesh.helpers.PermissionHandler;
import com.setayeshco.rooyesh.presenters.users.EditProfilePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * Created by abderrahimelimame on 6/9/16.
 * Email : abderrahim.elimame@gmail.com
 */

public class BottomSheetEditProfile extends BottomSheetDialogFragment {

    private View mView;
    @BindView(R.id.cameraBtn)
    FrameLayout cameraBtn;
    @BindView(R.id.galleryBtn)
    FrameLayout galleryBtn;
    private EditProfilePresenter mEditProfilePresenter;
    private Uri mProcessingPhotoUri;

    @Override
    public void onStart() {
        super.onStart();


    }


    private void setGalleryBtn() {
        if (PermissionHandler.checkPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            AppHelper.LogCat("Read data permission already granted.");

            dismiss();
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            getActivity().startActivityForResult(Intent.createChooser(intent, getActivity().getString(R.string.select_picture)), AppConstants.SELECT_PROFILE_PICTURE);

       /*     if (AppHelper.isAndroid6()) {
                dismiss();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image*//*");
                getActivity().startActivityForResult(Intent.createChooser(intent, getActivity().getString(R.string.select_picture)), AppConstants.SELECT_PROFILE_PICTURE);
            } else {
                dismiss();
                new PickerBuilder(getActivity(), PickerBuilder.SELECT_FROM_GALLERY)
                        .setOnImageReceivedListener(imageUri -> {
                            Intent data = new Intent();
                            data.setData(imageUri);
                            AppHelper.LogCat("new image SELECT_FROM_GALLERY" + imageUri);
                            mEditProfilePresenter.onActivityResult(getActivity(), AppConstants.SELECT_PROFILE_PICTURE, RESULT_OK, data);

                        })
                        .setImageName(getActivity().getString(R.string.app_name))
                        .setImageFolderName(getActivity().getString(R.string.app_name))
                        .setCropScreenColor(R.color.colorPrimary)
                        .withTimeStamp(false)
                        .setOnPermissionRefusedListener(() -> {
                            PermissionHandler.requestPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
                        })
                        .start();
            }*/
        } else {
            AppHelper.LogCat("Please request Read data permission.");
            PermissionHandler.requestPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        }

    }

    protected void sendToExternalApp() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mProcessingPhotoUri = FilesManager.getImageFile(getActivity());
        AppHelper.LogCat("mProcessingPhotoUri " + mProcessingPhotoUri);
        if (mProcessingPhotoUri != null)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mProcessingPhotoUri);
        getActivity().startActivityForResult(cameraIntent, AppConstants.SELECT_PROFILE_CAMERA);
    }

    private void setCameraBtn() {
        if (PermissionHandler.checkPermission(getActivity(), Manifest.permission.CAMERA)) {
            AppHelper.LogCat("camera permission already granted.");

           /* dismiss();
            sendToExternalApp();*/

           if (AppHelper.isAndroid6()) {
                dismiss();
                sendToExternalApp();
            } else {
                dismiss();
                new PickerBuilder(getActivity(), PickerBuilder.SELECT_FROM_CAMERA)
                        .setOnImageReceivedListener(imageUri -> {
                            AppHelper.LogCat("new image SELECT_FROM_CAMERA " + imageUri);
                            Intent data = new Intent();
                            data.setData(imageUri);
                            mEditProfilePresenter.onActivityResult(getActivity(), AppConstants.SELECT_PROFILE_CAMERA, RESULT_OK, data);

                        })
                        .setImageName(getActivity().getString(R.string.app_name))
                        .setImageFolderName(getActivity().getString(R.string.app_name))
                        .setCropScreenColor(R.color.colorPrimary)
                        .withTimeStamp(false)
                        .setOnPermissionRefusedListener(() -> {
                            PermissionHandler.requestPermission(getActivity(), Manifest.permission.CAMERA);
                        })
                        .start();
            }
        } else {
            AppHelper.LogCat("Please request camera  permission.");
            PermissionHandler.requestPermission(getActivity(), Manifest.permission.CAMERA);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.content_bottom_sheet, container, false);
        ButterKnife.bind(this, mView);
        mEditProfilePresenter = new EditProfilePresenter();
        galleryBtn.setOnClickListener(v -> setGalleryBtn());
        cameraBtn.setOnClickListener(v -> setCameraBtn());
        return mView;
    }

    @Override
    public void onViewCreated(View contentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(contentView, savedInstanceState);
        initView();
    }

    public void initView() {

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.content_bottom_sheet, null);
        dialog.setContentView(contentView);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        int height = ((View) contentView.getParent()).getHeight() / 2;
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
            ((BottomSheetBehavior) behavior).setPeekHeight(height);
            ((BottomSheetBehavior) behavior).setHideable(true);
        }

    }


    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {

            switch (newState) {
                case BottomSheetBehavior.STATE_DRAGGING:
                    AppHelper.LogCat("state Dragging");
                    break;

                case BottomSheetBehavior.STATE_SETTLING:
                    AppHelper.LogCat("state Settling");
                    break;

                case BottomSheetBehavior.STATE_COLLAPSED:
                    AppHelper.LogCat("state Collapsed");

                    break;

                case BottomSheetBehavior.STATE_HIDDEN:
                    dismiss();
                    break;
                case BottomSheetBehavior.STATE_EXPANDED:
                    AppHelper.LogCat("state expended");

                    break;
            }


        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            AppHelper.LogCat("onSlide");
            bottomSheet.setNestedScrollingEnabled(false);
        }
    };

}