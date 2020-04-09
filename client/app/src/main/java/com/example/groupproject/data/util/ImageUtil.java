package com.example.groupproject.data.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageUtil {

    public static File createImageFile(File storageDir) throws IOException {
        return File.createTempFile(getImageFileName(), ".jpg", storageDir);
    }

    private static String getTimeStampString() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    private static String getImageFileName() {
        return "JPEG_" + getTimeStampString() + "_";
    }
}
