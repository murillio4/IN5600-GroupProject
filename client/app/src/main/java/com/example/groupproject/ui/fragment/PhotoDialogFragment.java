package com.example.groupproject.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;

import com.example.groupproject.BuildConfig;
import com.example.groupproject.R;
import com.example.groupproject.data.Constants;
import com.example.groupproject.data.util.ImageUtil;
import com.example.groupproject.data.Result;
import com.example.groupproject.data.util.PermissionUtil;
import com.example.groupproject.ui.abstraction.TaggedDialogFragment;
import com.example.groupproject.ui.viewModel.PhotoViewModel;

import java.io.File;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatDialogFragment;

import static androidx.appcompat.app.AppCompatActivity.RESULT_OK;

public class PhotoDialogFragment extends DaggerAppCompatDialogFragment
        implements TaggedDialogFragment, View.OnClickListener {

    private static final String TAG = "PhotoDialogFragment";

    private Uri imageUri = null;

    @Inject
    PhotoViewModel photoViewModel;

    @Inject
    Context context;

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
        PermissionUtil.requestPermissions(
            getContext(),
            new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            },
            callback,
            dexterError -> dismiss());
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
            if (requestCode == Constants.RequestCode.CAMERA.ordinal()) {
                handleImageCaptureActivityResult(data);
            } else if (requestCode == Constants.RequestCode.GALLERY.ordinal()) {
                handleGalleryActivityResult(data);
            }

            dismiss();
        }
    }

    private void startImageCaptureActivity() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            File imageFile = ImageUtil.createImageFile(context);
            imageUri = null;

            if (imageFile != null) {
                imageUri = FileProvider.getUriForFile(
                        context, BuildConfig.APPLICATION_ID + ".provider", imageFile);
                if (imageUri != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, Constants.RequestCode.CAMERA.ordinal());
                    return;
                }
            }
        }

        Log.d(TAG, "startImageCaptureActivity: Unable to take picture");
        dismiss();
    }

    private void startGalleryActivity() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            startActivityForResult(intent, Constants.RequestCode.GALLERY.ordinal());
        }
    }

    private void handleImageCaptureActivityResult(Intent data) {
        if (imageUri != null) {
            Log.d(TAG, "handleImageCaptureActivityResult: Image: " + imageUri);
            photoViewModel.setPhotoResult(Result.success(imageUri));
        } else {
            Log.d(TAG, "handleImageCaptureActivityResult: Image not created");
            photoViewModel.setPhotoResult(Result.error(R.string.photo_dialog_image_capture_error));
        }
    }

    private void handleGalleryActivityResult(Intent data) {
        imageUri = data.getData();

        if (imageUri != null) {
            Log.d(TAG, "handleGalleryActivityResult: Image: " + imageUri);
            photoViewModel.setPhotoResult(Result.success(imageUri));
        } else {
            Log.d(TAG, "handleGalleryActivityResult: Image not created");
            photoViewModel.setPhotoResult(Result.error(R.string.photo_dialog_gallery_error));
        }
    }
}
