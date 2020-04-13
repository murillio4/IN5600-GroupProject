package com.example.groupproject.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.groupproject.R;
import com.example.groupproject.data.util.MapUtil;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.android.support.DaggerDialogFragment;

import static android.app.Activity.RESULT_OK;

public class LocationPickerDialogFragment extends DaggerDialogFragment
        implements OnMapReadyCallback, View.OnClickListener,
        MaterialSearchBar.OnSearchActionListener, TextWatcher,
        SuggestionsAdapter.OnItemViewClickListener {

    private static final String TAG = "LocationPickerDialogFra";
    private static final int USE_LOCATION_REQUEST = 51;
    private static final int DEFAULT_BEARING = 0;
    private static final int DEFAULT_ZOOM = 15;

    private FusedLocationProviderClient fusedLocationProviderClient = null;
    private List<AutocompletePrediction> predictionList = null;
    private SupportMapFragment supportMapFragment = null;
    private MaterialSearchBar materialSearchBar = null;
    private LocationCallback locationCallback = null;
    private Location lastKnownLocation = null;
    private PlacesClient placesClient = null;
    private GoogleMap googleMap = null;

    @Inject
    Context context;

    @Inject
    AutocompleteSessionToken autocompleteSessionToken;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_picker_dialog, container, false);
        view.findViewById(R.id.location_picker_dialog_button).setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestPermissions(() -> {
            initSupportMapFragment();
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
            Places.initialize(context, getString(R.string.google_maps_api_key));
            placesClient = Places.createClient(context);

            materialSearchBar = view.findViewById(R.id.location_picker_dialog_search_bar);
            materialSearchBar.setOnSearchActionListener(this);
            materialSearchBar.addTextChangeListener(this);
            materialSearchBar.setSuggestionsClickListener(this);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeSupportMapFragment();
    }

    @SuppressLint("ResourceType")
    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(() -> {
            if (materialSearchBar.isSuggestionsVisible()) {
                materialSearchBar.clearSuggestions();
            }
            if (materialSearchBar.isSearchOpened()) {
                materialSearchBar.closeSearch();
            }
            return false;
        });

        View mapView = supportMapFragment.getView();
        if (mapView != null && mapView.findViewById(1) != null) {
            View locationButton = ((View) mapView.findViewById(1).getParent()).findViewById(2);
            RelativeLayout.LayoutParams layoutParams =
                    (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 180);
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest locationSettingsRequest =
                new LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build();
        SettingsClient settingsClient = LocationServices.getSettingsClient(context);
        Task<LocationSettingsResponse> task =
                settingsClient.checkLocationSettings(locationSettingsRequest);

        task.addOnSuccessListener(getActivity(), locationSettingsResponse -> getDeviceLocation());
        task.addOnFailureListener(getActivity(), e -> {
           if (e instanceof ResolvableApiException)  {
               ResolvableApiException resolvableApiException = (ResolvableApiException) e;
               try {
                   resolvableApiException.startResolutionForResult(getActivity(), 51);
               } catch (IntentSender.SendIntentException sendIntentException) {
                   sendIntentException.printStackTrace();
               }
           }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.location_picker_dialog_button:
                Toast.makeText(context, "Location Selected", Toast.LENGTH_SHORT);
                break;
            default:
                dismiss();
        }
    }

    @Override
    public void onSearchStateChanged(boolean enabled) { }

    @Override
    public void onSearchConfirmed(CharSequence text) { }

    @Override
    public void onButtonClicked(int buttonCode) {
        switch (buttonCode) {
            case MaterialSearchBar.BUTTON_BACK:
                materialSearchBar.closeSearch();
                break;
            case MaterialSearchBar.BUTTON_SPEECH:
            case MaterialSearchBar.BUTTON_NAVIGATION:
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        FindAutocompletePredictionsRequest predictionsRequest =
                FindAutocompletePredictionsRequest.builder()
                .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(autocompleteSessionToken)
                .setQuery(s.toString())
                .build();
        placesClient.findAutocompletePredictions(predictionsRequest).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FindAutocompletePredictionsResponse predictionsResponse =
                        task.getResult();
                if (predictionsResponse != null) {
                    predictionList = predictionsResponse.getAutocompletePredictions();
                    List<String> suggestionList = predictionList.stream()
                            .map(prediction -> prediction.getFullText(null).toString())
                            .collect(Collectors.toList());
                    materialSearchBar.updateLastSuggestions(suggestionList);

                    if (!materialSearchBar.isSuggestionsVisible()) {
                        materialSearchBar.showSuggestionsList();
                    }
                } else {
                    Log.i(TAG, "onTextChanged: Task unsuccessful");
                }
            }
        });
    }

    @Override
    public void afterTextChanged(Editable s) { }

    @Override
    public void OnItemClickListener(int position, View v) {
        if (position >= predictionList.size()) {
            return;
        }

        AutocompletePrediction selectedPrediction = predictionList.get(position);
        String suggestion = materialSearchBar.getLastSuggestions().get(position).toString();
        materialSearchBar.setText(suggestion);

        // Look over
        Handler.createAsync(Looper.getMainLooper())
                .postDelayed(() -> { materialSearchBar.clearSuggestions(); }, 1000);

        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(materialSearchBar.getWindowToken(),
                    InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        final String placeId = selectedPrediction.getPlaceId();
        List<Place.Field> placeFields = Arrays.asList(Place.Field.LAT_LNG);
        FetchPlaceRequest fetchPlaceRequest =
                FetchPlaceRequest.builder(placeId, placeFields).build();

        placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener(fetchPlaceResponse -> {
            Place place = fetchPlaceResponse.getPlace();
            LatLng latLngPlace = place.getLatLng();
            if (latLngPlace != null) {
                googleMap.moveCamera(
                        CameraUpdateFactory.newCameraPosition(buildCameraPosition(latLngPlace)));
            }
            Log.i(TAG, "OnItemClickListener: Found place " + place.getName());

        }).addOnFailureListener(Throwable::printStackTrace);
    }

    @Override
    public void OnItemDeleteListener(int position, View v) { }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == USE_LOCATION_REQUEST) {
            if (resultCode == RESULT_OK) {
                getDeviceLocation();
            }
        }
    }

    public void showNow(FragmentManager fragmentManager) {
        showNow(fragmentManager, TAG);
    }

    private void requestPermissions(Runnable callback) {
        Dexter.withContext(getContext())
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            callback.run();
                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            Log.d(TAG, "onPermissionsChecked: isAnyPermissionPermanentlyDenied?");
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list,
                                                                   PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .withErrorListener(error -> dismiss())
                .onSameThread()
                .check();
    }

    private CameraPosition buildCameraPosition(LatLng location) {
        return CameraPosition.builder()
                .target(location)
                .zoom(DEFAULT_ZOOM)
                .bearing(DEFAULT_BEARING)
                .build();
    }

    private void getDeviceLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                lastKnownLocation = task.getResult();
                if (lastKnownLocation != null) {
                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                            buildCameraPosition(new LatLng(lastKnownLocation.getLatitude(),
                                    lastKnownLocation.getLongitude()))));
                } else {
                    final LocationRequest locationRequest = LocationRequest.create();
                    locationRequest.setInterval(10000);
                    locationRequest.setFastestInterval(5000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    locationCallback = new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            if (locationResult == null) {
                                return;
                            }
                            lastKnownLocation = locationResult.getLastLocation();
                            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                                    buildCameraPosition(new LatLng(lastKnownLocation.getLatitude(),
                                            lastKnownLocation.getLongitude()))));
                            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                        }
                    };
                    fusedLocationProviderClient.requestLocationUpdates(
                            locationRequest, locationCallback, null);
                }
            } else {
                Toast.makeText(context, "Unable to fetch last location", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initSupportMapFragment() {
        if (supportMapFragment == null) {
            supportMapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                    .findFragmentById(R.id.location_picker_dialog_map);
            supportMapFragment.getMapAsync(this);
        }
    }

    private void removeSupportMapFragment() {
        if (supportMapFragment != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction().remove(supportMapFragment).commit();
        }
    }
}
