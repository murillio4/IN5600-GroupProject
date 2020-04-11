package com.example.groupproject.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.groupproject.BuildConfig;
import com.example.groupproject.R;
import com.example.groupproject.data.Constants;
import com.example.groupproject.data.util.ImageUtil;
import com.example.groupproject.ui.viewModel.PhotoViewModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterBuilder;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatDialogFragment;

import static android.app.Activity.RESULT_OK;

public class PhotoDialogFragment extends DaggerAppCompatDialogFragment implements View.OnClickListener {

    private static final String TAG = "PhotoDialogFragment";

    @Inject
    PhotoViewModel photoViewModel;

    @Inject
    Context context;

    private String imageFileAbsolutePath = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_photo_dialog, container, false);

        view.findViewById(R.id.photo_dialog_gallery_btn).setOnClickListener(this);
        view.findViewById(R.id.photo_dialog_photo_btn).setOnClickListener(this);
        view.findViewById(R.id.photo_dialog_cancel_btn).setOnClickListener(this);

        return view;
    }

    public void showNow(FragmentManager fragmentManager) {
        showNow(fragmentManager, TAG);
    }

    private void requestStoragePermission(Runnable callback) {
        Log.i(TAG, "requestStoragePermission");
        Dexter.withContext(getContext())
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        //Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Log.i(TAG, "requestStoragePermission: All permissions granted");
                            callback.run();
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Log.d(TAG, "onPermissionsChecked: Some permissions permanently denied");
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                                   PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .withErrorListener(error -> dismiss())
                .onSameThread()
                .check();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photo_dialog_gallery_btn:
                requestStoragePermission(() -> {
                    Log.i(TAG, "onClick: photo_dialog_gallery_btn");
                    startGalleryActivity();
                });
                break;
            case R.id.photo_dialog_photo_btn:
                requestStoragePermission(() -> {
                    Log.i(TAG, "onClick: photo_dialog_photo_btn");
                    startImageCaptureActivity();
                });
                break;
            case R.id.photo_dialog_cancel_btn:
                dismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.REQUEST_CODE.CAMERA.ordinal()) {
                handleImageCaptureActivityResult(data);
            } else if (requestCode == Constants.REQUEST_CODE.GALLERY.ordinal()) {
                handleGalleryActivityResult(data);
            } else {
                // Handle ???
            }
        } else {
            // Handle ???
        }
    }

    private void startImageCaptureActivity() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            Log.d(TAG, "startImageCaptureActivity: Image Capture");
            try {
                File imageFile = ImageUtil.Storage.createImageFile(context);
                imageFileAbsolutePath = null;

                if (imageFile != null) {
                    Uri imageUri = FileProvider.getUriForFile(
                            context, BuildConfig.APPLICATION_ID + ".provider", imageFile);
                    imageFileAbsolutePath = imageFile.getAbsolutePath();
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, Constants.REQUEST_CODE.CAMERA.ordinal());
                }
            } catch (IOException e) {
                Log.e(TAG, "startImageCaptureActivity: Error creating image file", e);
            }
        } else {
            Log.d(TAG, "startImageCaptureActivity: No packet manager available");
        }
    }

    private void startGalleryActivity() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            startActivityForResult(intent, Constants.REQUEST_CODE.GALLERY.ordinal());
        }
    }

    private void handleImageCaptureActivityResult(Intent data) {
        if (imageFileAbsolutePath != null) {
            Log.d(TAG, "handleImageCaptureActivityResult: Image: " + imageFileAbsolutePath);
        }
    }

    private void handleGalleryActivityResult(Intent data) {
        Uri imageUri = data.getData();
        imageFileAbsolutePath = imageUri.getPath(); // NOT CORRECT ?
        Log.d(TAG, "handleGalleryActivityResult: Image: " + imageFileAbsolutePath);
    }


}
