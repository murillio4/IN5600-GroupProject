package com.example.groupproject.data.util;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

public class MapUtil {

    private MapUtil() {}

    public static LatLng locationStringToLatLng(@Nullable String locationString) {
        if (locationString == null) {
            return null;
        }

        String[] locationArray = locationString.split(",", 2);
        if (locationArray.length <= 0) {
            return null;
        }

        try {
            double lat = Double.parseDouble(locationArray[0]);
            double lng = Double.parseDouble(locationArray[1]);
            return new LatLng(lat, lng);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String latLngToLocationString(@Nullable LatLng latLng) {
        if (latLng == null) {
            return null;
        }

        return String.format(Locale.getDefault(), "%f,%f", latLng.latitude, latLng.longitude);
    }
}
