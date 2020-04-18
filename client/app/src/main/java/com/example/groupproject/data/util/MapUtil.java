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
        if (locationArray.length != 2) {
            return null;
        }

        LatLng latLng = null;
        try {
            latLng = new LatLng(Double.parseDouble(locationArray[0]),
                                Double.parseDouble(locationArray[1]));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return latLng;
    }

    public static String latLngToLocationString(@Nullable LatLng latLng) {
        if (latLng == null) {
            return null;
        }

        return String.format(Locale.getDefault(), "%f,%f", latLng.latitude, latLng.longitude);
    }
}
