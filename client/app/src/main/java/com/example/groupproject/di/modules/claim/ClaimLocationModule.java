package com.example.groupproject.di.modules.claim;

import android.content.Context;
import android.location.Geocoder;
import android.view.LayoutInflater;

import com.example.groupproject.R;
import com.example.groupproject.ui.adapter.LocationSuggestionsAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Locale;

import dagger.Module;
import dagger.Provides;

@Module
public class ClaimLocationModule {

    @Provides
    AutocompleteSessionToken provideAutocompleteSessionToken() {
        return AutocompleteSessionToken.newInstance();
    }

    @Provides
    FusedLocationProviderClient providesFusedLocationProviderClient(Context context) {
        return LocationServices.getFusedLocationProviderClient(context);
    }

    @Provides
    PlacesClient providesPlacesClient(Context context) {
        String apiKey = context.getResources().getString(R.string.google_maps_api_key);
        Places.initialize(context, apiKey);
        return Places.createClient(context);
    }

    @Provides
    LocationRequest providesLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        return locationRequest;
    }

    @Provides
    LocationSettingsRequest providesLocationSettingsRequest(LocationRequest locationRequest) {
        return new LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build();
    }

    @Provides
    SettingsClient providesSettingsClient(Context context) {
        return LocationServices.getSettingsClient(context);
    }

    @Provides
    LocationSuggestionsAdapter providesLocationSuggestionsAdapter(LayoutInflater layoutInflater) {
        return new LocationSuggestionsAdapter(layoutInflater);
    }

    @Provides
    Geocoder providesGeocoder(Context context) {
        return new Geocoder(context, Locale.getDefault());
    }
}
