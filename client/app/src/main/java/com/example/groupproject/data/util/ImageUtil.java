package com.example.groupproject.data.util;

import java.io.File;
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
