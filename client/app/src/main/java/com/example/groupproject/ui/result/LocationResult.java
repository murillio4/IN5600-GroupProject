package com.example.groupproject.ui.result;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

public class LocationResult {
    @Nullable
    private LatLng success;
    @Nullable
    private Integer error;

    public LocationResult(@Nullable Integer error) {
        this.error = error;
    }

    public LocationResult(@Nullable LatLng success) {
        this.success = success;
    }

    @Nullable
    public LatLng getSuccess() {
        return success;
    }

    @Nullable
    public Integer getError() {
        return error;
    }
}
