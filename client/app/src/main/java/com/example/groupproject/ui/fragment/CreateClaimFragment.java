package com.example.groupproject.ui.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.groupproject.R;
import com.example.groupproject.ui.result.Result;
import com.example.groupproject.ui.viewModel.LocationViewModel;
import com.example.groupproject.ui.viewModel.PhotoViewModel;
import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class CreateClaimFragment extends DaggerFragment implements View.OnClickListener {

    private static final String TAG = "CreateClaimFragment";

    private LatLng location = null;

    @Inject
    PhotoViewModel photoViewModel;

    @Inject
    LocationViewModel locationViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_claim, container, false);

        view.findViewById(R.id.create_claim_add_map_location_button).setOnClickListener(this);
        view.findViewById(R.id.create_claim_add_photo_button).setOnClickListener(this);
        view.findViewById(R.id.create_claim_submit_button).setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Here?");

        photoViewModel.getPhotoResult().observe(this, photoResult -> {
            if (photoResult.getHasBeenHandled()) {
                return;
            }

            Result<Uri> result = photoResult.getContentIfNotHandled();

            if (result.getSuccess() != null) {
                Log.d(TAG, "onCreate: PhotoResult: " + result.getSuccess());
            }

            if (result.getError() != null) {
                Log.d(TAG, "onCreate: PhotoResult: " + result.getError());
            }
        });

        locationViewModel.getLocationResult().observe(this, locationResult -> {
            if (locationResult.getHasBeenHandled()) {
                return;
            }

            Result<LatLng> result = locationResult.getContentIfNotHandled();

            if (result.getSuccess() != null) {
                location = result.getSuccess();
                Log.d(TAG, "onCreate: LocationResult: " + result.getSuccess());
            }

            if (result.getError() != null) {
                Log.d(TAG, "onCreate: LocationResult: " + result.getError());
            }
        });
    }

    private void toClaimListFragment() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    protected void showClaimSuccess() {
        ///
    }

    protected void showClaimFailed() {
        ///
    }

    private void handleAddMapLocationButton() {
        Toast.makeText(getActivity(), "Add Map Location", Toast.LENGTH_SHORT).show();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        new LocationPickerDialogFragment(location).showNow(fm);
    }

    private void handleAddPhotoButton() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        new PhotoDialogFragment().showNow(fm);
    }

    private void handleSubmitButton() {
        toClaimListFragment();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_claim_add_map_location_button:
                handleAddMapLocationButton();
                break;
            case R.id.create_claim_add_photo_button:
                handleAddPhotoButton();
                break;
            case R.id.create_claim_submit_button:
                handleSubmitButton();
                break;
        }
    }
}
