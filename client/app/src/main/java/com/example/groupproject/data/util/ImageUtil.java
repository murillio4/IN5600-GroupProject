package com.example.groupproject.data.util;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageUtil {

    private static final String TAG = "ImageUtil";
    private static final String MIME_TYPE = "image/jpg";
    private static final String MIME_TYPE_SUFFIX = ".jpg";

    private ImageUtil() {}

    // Do we need this?
    public static Uri saveBitmap(
            @NonNull Context context, @NonNull Bitmap bitmap, @NonNull String displayName)
            throws IOException {

        ContentValues contentValues = createContentValues(displayName);
        ContentResolver contentResolver = context.getContentResolver();
        OutputStream outputStream = null;
        Uri imageUri = null;

        try {
            imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            if (imageUri == null) {
                throw new IOException("Failed to create image URI!");
            }

            outputStream = contentResolver.openOutputStream(imageUri);
            if (outputStream == null) {
                throw new IOException("Failed to open output stream!");
            }

            if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)) {
                throw new IOException("Failed to save bitmap");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            if (imageUri != null) {
                contentResolver.delete(imageUri, null, null);
                imageUri = null;
            }
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }

        return imageUri;
    }

    public static File createImageFile(@NonNull Context context) {
        File file = null;

        try {
            file = File.createTempFile(
                    getImageFileName(),
                    ImageUtil.MIME_TYPE_SUFFIX,
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    public static boolean fileExists(@NonNull Context context, Uri uri) {
        ParcelFileDescriptor parcelFileDescriptor = null;
        boolean fileExists = true;

        try {
            parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
        } catch (FileNotFoundException e) {
            fileExists = false;
        } finally {
            if (parcelFileDescriptor != null) {
                try {
                    parcelFileDescriptor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return fileExists;
    }

    // Do we need this?
    public static Bitmap getBitmapFromUri(@NonNull Context context, Uri uri) throws IOException {

        ParcelFileDescriptor parcelFileDescriptor =
                context.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return bitmap;
    }

    private static ContentValues createContentValues(@NonNull String displayName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, ImageUtil.MIME_TYPE);
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
        return contentValues;
    }

    @SuppressLint("SimpleDateFormat")
    private static String getTimeStampString() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    private static String getImageFileName() {
        return "JPEG_" + getTimeStampString() + "_";
    }

    private String destinationDirectory(@NonNull Context context) {
        return context.getCacheDir().getPath() + File.separator + "images";
    }

}
