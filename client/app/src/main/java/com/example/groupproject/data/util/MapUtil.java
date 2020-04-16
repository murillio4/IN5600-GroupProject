package com.example.groupproject.data.util;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

public class MapUtil {

    private MapUtil() {}

    public static class Location {

        private Location() {}

        public static LatLng stringLocationToLatLng(String locationString) {
            String[] locationArray = locationString.split(",", 2);
            if (locationArray.length <= 0) {
                return new LatLng(0.0, 0.0);
            }

            try {
                double lat = Double.parseDouble(locationArray[0]);
                double lng = Double.parseDouble(locationArray[1]);
                return new LatLng(lat, lng);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return new LatLng(0.0, 0.0);
            }
        }

        public static String latLngToLocationString(LatLng latLng) {
            return String.format(Locale.getDefault(), "%f:%f", latLng.latitude, latLng.longitude);
        }
    }
}
