package com.example.groupproject.data.util;

import android.Manifest;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterBuilder;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

public class PermissionUtil {

    private static final String TAG = "PermissionUtil";

    public static void requestPermissions(Context context, @NonNull String[] permissions,
                                         @NonNull Runnable successCallback,
                                         @Nullable PermissionRequestErrorListener errorCallback) {

        Dexter.withContext(context)
            .withPermissions(permissions)
            .withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport report) {
                    if (report.areAllPermissionsGranted()) {
                        Log.i(TAG, "requestPermission: All permissions granted");
                        successCallback.run();
                    }
                    if (report.isAnyPermissionPermanentlyDenied()) {
                        Log.d(TAG, "requestPermission: Some permissions permanently denied");
                    }
                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                               PermissionToken token) {
                    token.continuePermissionRequest();
                }
            }).withErrorListener(error -> {
                Log.d(TAG, "requestPermission: Failed to request permission");
                if (errorCallback != null)
                    errorCallback.onError(error);
            })
                .onSameThread()
                .check();
    }
}
