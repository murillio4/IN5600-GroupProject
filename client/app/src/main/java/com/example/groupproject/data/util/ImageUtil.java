package com.example.groupproject.data.util;

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

    private ImageUtil() {}

    public static class Storage {

        private static final String TAG = "ImageUtil.Storage";

        private Storage() {}

        public static Uri saveBitmap(@NonNull Context context, @NonNull Bitmap bitmap,
                              @NonNull String displayName) throws IOException {
            ContentValues contentValues = createContentValues(
                    displayName, "image/jpg", Environment.DIRECTORY_PICTURES);
            ContentResolver contentResolver = context.getContentResolver();

            OutputStream outputStream = null;
            Uri imageUri = null;

            try {
                Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                imageUri = contentResolver.insert(contentUri, contentValues);
                if (imageUri == null) {
                    throw new IOException("Failed to create image URI!");
                }

                outputStream = contentResolver.openOutputStream(imageUri);
                if (outputStream == null) {
                    throw new IOException("Failed to open output stream!");
                }

                // Move to compressor class
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream) == false) {
                    throw new IOException("Failed to save bitmap");
                }
            } catch (IOException e) {
                if (imageUri != null) {
                    contentResolver.delete(imageUri, null, null);
                }
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }

            return imageUri;
        }

        public Uri saveImageFile(@NonNull Context context,
                                 @NonNull File imageFile) throws IOException {
            return null;
        }

        public Uri saveImage(@NonNull Context context, @NonNull Uri imageUri) throws IOException {
            return null;
        }

        public static File createImageFile(@NonNull Context context) throws IOException {
            return File.createTempFile(
                    getImageFileName(),
                    ".jpg",
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            );
        }
        
        public static boolean fileExists(@NonNull Context context, Uri uri) {
            ParcelFileDescriptor parcelFileDescriptor = null;
            boolean fileExists = true;

            try {
                parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
            } catch (FileNotFoundException e) {
                Log.d(TAG, "fileExists: Unable to locate file " + uri);
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

        public static Bitmap getBitmapFromUri(@NonNull Context context, Uri uri) throws IOException {
            ParcelFileDescriptor parcelFileDescriptor =
                    context.getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return bitmap;
        }

        private static ContentValues createContentValues(@NonNull String displayName,
                                                  @NonNull String mimeType,
                                                  @NonNull String relativePath) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath);
            return contentValues;
        }

        private static String getTimeStampString() {
            return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        }

        private static String getImageFileName() {
            return "JPEG_" + getTimeStampString() + "_";
        }

    }

    public static class Compressor {

        private Compressor() {}

        private String destinationDirectory(@NonNull Context context) {
            return context.getCacheDir().getPath() + File.separator + "images";
        }


    }

    public static class Converter {

        private Converter() {}

    }

}
