package com.example.groupproject.data.util;

import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageUtil {

    private ImageUtil() {}

    public static class Storage {

        private Storage() {}

        public static File createImageFile(@NonNull Context context) throws IOException {
            return File.createTempFile(
                    getImageFileName(),
                    ".jpg",
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            );
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

    }

    public static class Converter {

        private Converter() {}

    }

}
