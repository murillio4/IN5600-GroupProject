package com.example.groupproject.ui.result;

import android.net.Uri;

import androidx.annotation.Nullable;

public class PhotoResult {
    @Nullable
    private Uri success;
    @Nullable
    private Integer error;

    public PhotoResult(@Nullable Integer error) {
        this.error = error;
    }

    public PhotoResult(@Nullable Uri success) {
        this.success = success;
    }

    @Nullable
    public Uri getSuccess() {
        return success;
    }

    @Nullable
    public Integer getError() {
        return error;
    }
}
