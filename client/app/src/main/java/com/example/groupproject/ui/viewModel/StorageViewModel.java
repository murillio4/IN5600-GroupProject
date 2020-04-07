package com.example.groupproject.ui.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

public class StorageViewModel extends AndroidViewModel {

    @Inject
    public StorageViewModel(@NonNull Application application) {
        super(application);
    }

    public File createImageFile(File storageDir) throws IOException {
        return File.createTempFile(getImageFileName(), ".jpg", storageDir);
    }

    private String getTimeStampString() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    private String getImageFileName() {
        return "JPEG_" + getTimeStampString() + "_";
    }

}
