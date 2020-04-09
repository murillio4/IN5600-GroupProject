package com.example.groupproject.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.groupproject.R;
import com.example.groupproject.ui.viewModel.PhotoViewModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterBuilder;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatDialogFragment;

public class PhotoDialogFragment extends DaggerAppCompatDialogFragment implements View.OnClickListener {
    private static final String TAG = "PhotoDialogFragment";
    
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
        Dexter.withContext(getContext())
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Log.i(TAG, "requestStoragePermission: permissions granted");
                            callback.run();
                        }
                        // check for permanent denial of any permission
                        // if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //showSettingsDialog();
                        //}
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
                });
                break;
            case R.id.photo_dialog_photo_btn:
                requestStoragePermission(() -> {
                    Log.i(TAG, "onClick: photo_dialog_photo_btn");
                });
                break;
            case R.id.photo_dialog_cancel_btn:
                dismiss();
        }
    }
}
