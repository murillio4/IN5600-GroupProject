package com.example.groupproject.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.example.groupproject.R;
import com.example.groupproject.data.util.MapUtil;
import com.example.groupproject.data.util.PermissionUtil;
import com.example.groupproject.ui.abstraction.TaggedDialogFragment;
import com.example.groupproject.ui.adapter.LocationSuggestionsAdapter;
import com.example.groupproject.data.Result;
import com.example.groupproject.ui.viewModel.LocationViewModel;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.android.support.DaggerDialogFragment;

import static android.app.Activity.RESULT_OK;

public class LocationPickerDialogFragment extends DaggerDialogFragment
        implements TaggedDialogFragment, View.OnClickListener,
        MaterialSearchBar.OnSearchActionListener, TextWatcher,
        SuggestionsAdapter.OnItemViewClickListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMapClickListener,
        OnMapReadyCallback {

    private static final String TAG = "LocationPickerDialogFra";
    private static final int USE_LOCATION_REQUEST = 51;

    private List<AutocompletePrediction> predictionList = null;
    private SupportMapFragment supportMapFragment = null;
    private MaterialSearchBar materialSearchBar = null;
    private GoogleMap googleMap = null;
    private LatLng marker;

    private String[] permissions = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Inject
    Context context;
    @Inject
    LocationViewModel locationViewModel;

    @Inject
    Geocoder geocoder;
    @Inject
    AutocompleteSessionToken autocompleteSessionToken;
    @Inject
    LocationSuggestionsAdapter locationSuggestionsAdapter;
    @Inject
    PlacesClient placesClient;
    @Inject
    FusedLocationProviderClient fusedLocationProviderClient;
    @Inject
    LocationRequest locationRequest;
    @Inject
    LocationSettingsRequest locationSettingsRequest;
    @Inject
    SettingsClient settingsClient;

    public LocationPickerDialogFragment(@Nullable LatLng marker) {
        super();
        this.marker = marker;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_picker_dialog, container, false);
        view.findViewById(R.id.location_picker_dialog_btn).setOnClickListener(this);
        return view;
    }

    @Override
    public int getTheme() {
        return R.style.FullScreenDialog;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestPermissions(() -> {
            initSupportMapFragment();
            initMaterialSearchBar();
        });
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
        removeSupportMapFragment();
    }

    @SuppressLint("ResourceType")
    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(this);

        googleMap.setOnMapClickListener(this);

        View mapView = supportMapFragment.getView();
        if (mapView != null && mapView.findViewById(1) != null) {
            View locationButton = ((View) mapView.findViewById(1).getParent()).findViewById(2);
            RelativeLayout.LayoutParams layoutParams =
                    (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 180);
        }

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

        if (marker != null) {
            try {
                String address = getAddressFromLatLang(marker);
                if (address != null) {
                    materialSearchBar.setPlaceHolder(address);
                    setMarker(marker, address);
                } else {
                    setMarker(marker, marker.toString());
                }
            } catch (IOException e) {
                setMarker(marker, marker.toString());
            }

        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        if (materialSearchBar.isSuggestionsVisible()) {
            materialSearchBar.clearSuggestions();
        }
        if (materialSearchBar.isSearchOpened()) {
            materialSearchBar.closeSearch();
        }
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        try {
            String address = getAddressFromLatLang(latLng);
            if (address != null) {
                materialSearchBar.setPlaceHolder(address);
                setMarker(latLng, address);
            } else {
                setMarker(latLng, latLng.toString());
            }
        } catch (IOException e) {
            setMarker(latLng, latLng.toString());
        }
        marker = latLng;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.location_picker_dialog_btn:
                if (marker != null) {
                    Log.i(TAG, "onClick:");
                    locationViewModel.setLocationResult(Result.success(marker));
                    dismiss();
                } else {
                    Toast.makeText(context, R.string.location_picker_dialog_no_selected, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.mt_clear:
                materialSearchBar.onClick(v);
                materialSearchBar.clearSuggestions();
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
        Log.i(TAG, "onButtonClicked: " + buttonCode);
        switch (buttonCode) {
            case MaterialSearchBar.BUTTON_BACK:
                materialSearchBar.closeSearch();
                Log.i(TAG, "onButtonClicked: BUTTON_BACK");
                break;
            case MaterialSearchBar.BUTTON_NAVIGATION:
                dismiss();
                break;
            case MaterialSearchBar.BUTTON_SPEECH:
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

        new Handler().postDelayed(() -> materialSearchBar.clearSuggestions(), 1000);

        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(materialSearchBar.getWindowToken(),
                    InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        final String placeId = selectedPrediction.getPlaceId();
        List<Place.Field> placeFields = Collections.singletonList(Place.Field.LAT_LNG);
        FetchPlaceRequest fetchPlaceRequest =
                FetchPlaceRequest.builder(placeId, placeFields).build();

        placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener(fetchPlaceResponse -> {
            Place place = fetchPlaceResponse.getPlace();
            LatLng latLngPlace = place.getLatLng();
            if (latLngPlace != null) {
                setMarker(latLngPlace, place.getName());
                moveCamera(latLngPlace);
                marker = latLngPlace;
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

        PermissionUtil.requestPermissions(
            context,
            permissions,
            callback,
            dexterError -> dismiss());
    }

    private void getDeviceLocation() {
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(getActivity(), location -> {
                        if (location != null) {
                            moveCamera(location);
                        } else {
                            listenForLocationUpdate();
                        }
                }).addOnFailureListener(getActivity(), e -> {
                    Toast.makeText(context, R.string.location_picker_dialog_curr_location_error, Toast.LENGTH_LONG).show();
                });
    }

    private void listenForLocationUpdate() {
        fusedLocationProviderClient.requestLocationUpdates(
                locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        if (locationResult == null) {
                            return;
                        }
                        moveCamera(locationResult.getLastLocation());
                        fusedLocationProviderClient.removeLocationUpdates(this);
                    }
                }, null);
    }

    private void moveCamera(LatLng latLng) {
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                MapUtil.buildCameraPosition(latLng)));
    }

    private void moveCamera(Location location) {
        moveCamera(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    private void setMarker(LatLng latLng, String title) {
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions().position(latLng).title(title));
    }

    private void initMaterialSearchBar() {
        materialSearchBar = getView().findViewById(R.id.location_picker_dialog_search_bar);
        materialSearchBar.setOnSearchActionListener(this);
        materialSearchBar.addTextChangeListener(this);
        locationSuggestionsAdapter.setListener(this);
        materialSearchBar.setCustomSuggestionAdapter(locationSuggestionsAdapter);
        materialSearchBar.findViewById(R.id.mt_clear).setOnClickListener(this);
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

    private String getAddressFromLatLang(LatLng latLng) throws IOException {

        try {
            Address address = geocoder.getFromLocation(
                    latLng.latitude,
                    latLng.longitude,
                    1).get(0);
            return address.getAddressLine(0);
        } catch (IndexOutOfBoundsException e) {
            Log.d(TAG, "getAddressFromLatLang: No address line for ("
                    + latLng.latitude + "," + latLng.longitude);
            return null;
        }
    }
}
