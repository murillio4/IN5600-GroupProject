package com.example.groupproject.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentManager;

import com.example.groupproject.R;
import com.example.groupproject.data.util.MapUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerDialogFragment;

public class LocationPickerDialogFragment extends DaggerDialogFragment implements OnMapReadyCallback {

    private static final String TAG = "LocationPickerDialogFra";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location_picker_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestPermissions(() -> initSupportMapFragment());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        final String mapTitle = "MAP TITLE";
        final LatLng location = MapUtil.Location.stringLocationToLatLng("0.00000:0.0000");

        CameraPosition cameraPosition = CameraPosition.builder()
                .target(location)
                .zoom(10)
                .bearing(0)
                .build();

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.clear();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, null);
        googleMap.addMarker(new MarkerOptions().position(location).title(mapTitle));
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

    private void initSupportMapFragment() {
        SupportMapFragment mapFragment =
                (SupportMapFragment) getParentFragmentManager()
                        .findFragmentById(R.id.location_picker_dialog_map);
        mapFragment.getMapAsync(this);
    }
}
