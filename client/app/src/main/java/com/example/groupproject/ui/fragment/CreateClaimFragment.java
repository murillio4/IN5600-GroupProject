package com.example.groupproject.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;

import com.example.groupproject.R;
import com.example.groupproject.data.network.model.Resource;
import com.example.groupproject.data.model.Claim;
import com.example.groupproject.data.util.ClaimUtil;
import com.example.groupproject.data.util.MapUtil;
import com.example.groupproject.data.Result;
import com.example.groupproject.data.util.TransitionUtil;
import com.example.groupproject.ui.viewModel.ClaimsViewModel;
import com.example.groupproject.ui.viewModel.FormViewModel;
import com.example.groupproject.ui.viewModel.LocationViewModel;
import com.example.groupproject.ui.viewModel.PhotoViewModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class CreateClaimFragment extends DaggerFragment
        implements View.OnClickListener, TextWatcher {

    private static final String TAG = "CreateClaimFragment";

    protected Claim claim;

    EditText descriptionEditText;

    @Inject
    PhotoViewModel photoViewModel;

    @Inject
    LocationViewModel locationViewModel;

    @Inject
    FormViewModel formViewModel;

    @Inject
    ClaimsViewModel claimsViewModel;

    @Inject
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_claim, container, false);

        descriptionEditText = view.findViewById(R.id.create_claim_description_input);
        descriptionEditText.addTextChangedListener(this);

        view.findViewById(R.id.create_claim_add_map_location_button).setOnClickListener(this);
        view.findViewById(R.id.create_claim_add_photo_button).setOnClickListener(this);
        view.findViewById(R.id.create_claim_submit_button).setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        claim = initClaim();
        if (claim == null) {
            Log.d(TAG, "onCreate: Fail to create initial claim");
        }

        photoViewModel.getPhotoResult().observe(getViewLifecycleOwner(), photoResult -> {
            if (photoResult.getHasBeenHandled()) {
                return;
            }

            Result<Uri> result = photoResult.getContentIfNotHandled();

            if (result.getSuccess() != null) {
                claim.setPhotoPath(result.getSuccess().toString());
                Log.d(TAG, "onCreate: PhotoResult: " + result.getSuccess());
            }

            if (result.getError() != null) {
                Log.d(TAG, "onCreate: PhotoResult: " + result.getError());
            }
        });

        locationViewModel.getLocationResult().observe(getViewLifecycleOwner(), locationResult -> {
            if (locationResult.getHasBeenHandled()) {
                return;
            }

            Result<LatLng> result = locationResult.getContentIfNotHandled();

            if (result.getSuccess() != null) {
                claim.setLocation(MapUtil.latLngToLocationString(result.getSuccess()));
                Log.d(TAG, "onCreate: LocationResult: " + result.getSuccess());
            }

            if (result.getError() != null) {
                Log.d(TAG, "onCreate: LocationResult: " + result.getError());
            }
        });

        formViewModel.getDescriptionState().observe(getViewLifecycleOwner(), descriptionState -> {
            if (descriptionState == null) {
                return;
            }

            descriptionEditText.setEnabled(descriptionState.isDataValid());
            claim.setDescription(descriptionEditText.getText().toString());
            if (descriptionState.getData() != null) {
                descriptionEditText.setError(getString(descriptionState.getData()));
            }
        });
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }

    @Override
    public void afterTextChanged(Editable s) {
        formViewModel.descriptionDataChanged(descriptionEditText.getText().toString());
    }

    protected Claim initClaim() {
        String id = claimsViewModel.getNextClaimId();
        if (id == null) {
            return null;
        }

        return new Claim(id, null, null, null);
    }

    protected void postClaim() {
        claimsViewModel.createClaim(claim).observe(getActivity(), createClaimResult -> {
            if (createClaimResult == null) {
                return;
            }

            if (createClaimResult.getError() != null) {
                Log.d(TAG, "onViewCreated: Failed to create claim" + claim.getId());
            } else if (createClaimResult.getSuccess() != null) {
                TransitionUtil.toPreviousFragment(getActivity());
            }
        });
    }

    private void handleAddMapLocationButton() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        new LocationPickerDialogFragment(
                MapUtil.locationStringToLatLng(claim.getLocation())).showNow(fm);
    }

    private void handleAddPhotoButton() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        new PhotoDialogFragment().showNow(fm);
    }

    private void handleSubmitButton() {
        if (!ClaimUtil.verifyClaim(claim)) {
            Log.d(TAG, "handleSubmitButton: Claim is missing fields");
            Toast.makeText(context, "Failed to post claim", Toast.LENGTH_SHORT).show();
            return;
        }

        postClaim();
    }
}
