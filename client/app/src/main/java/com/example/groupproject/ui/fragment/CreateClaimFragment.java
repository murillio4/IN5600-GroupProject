package com.example.groupproject.ui.fragment;

import android.app.Activity;
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

import androidx.fragment.app.FragmentManager;

import com.example.groupproject.R;
import com.example.groupproject.data.model.Claim;
import com.example.groupproject.data.util.MapUtil;
import com.example.groupproject.ui.result.Result;
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

    private LatLng location = null;
    private Uri photoUri = null;
    private EditText descriptionEditText;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        photoViewModel.getPhotoResult().observe(this, photoResult -> {
            if (photoResult.getHasBeenHandled()) {
                return;
            }

            Result<Uri> result = photoResult.getContentIfNotHandled();

            if (result.getSuccess() != null) {
                photoUri = result.getSuccess();
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

        formViewModel.getDescriptionState().observe(this, descriptionState -> {
            if (descriptionState == null) {
                return;
            }

            descriptionEditText.setEnabled(descriptionState.isDataValid());
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

    private void toClaimListFragment() {
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
    }

    private void handleAddMapLocationButton() {
        Toast.makeText(getActivity(), "Add Map Location", Toast.LENGTH_SHORT).show();
        FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        new LocationPickerDialogFragment(location).showNow(fm);
    }

    private void handleAddPhotoButton() {
        FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        new PhotoDialogFragment().showNow(fm);
    }

    private void handleSubmitButton() {
        Claim claim = buildClaim();

        if (claim == null) {
            Log.d(TAG, "handleSubmitButton: Failed to build claim");
            Toast.makeText(context, "Failed to post claim", Toast.LENGTH_SHORT).show();
            return;
        }

        claimsViewModel.createClaim(claim).observe(Objects.requireNonNull(getActivity()),
                createClaimResult -> {
            switch (createClaimResult.getStatus()) {
                case LOADING:
                    Log.d(TAG, "onCreate: Loading resource");
                    break;
                case ERROR:
                    Log.d(TAG, "onCreate: Failed to create claim");
                    break;
                case SUCCESS:
                    Log.d(TAG, "onCreate: Successfully posted claim");
                    toClaimListFragment();
                    break;
                default:
                    Log.d(TAG, "onCreate: Unknown result");
                    break;
            }
        });
    }

    private Claim buildClaim() {
        String id = claimsViewModel.getNextClaimId();
        if (id == null) {
            Log.d(TAG, "buildClaim: Failed to get next claim id");
            Toast.makeText(context, "Faild to get next claim id", Toast.LENGTH_SHORT).show();
            return null;
        }

        String description = descriptionEditText.getText().toString();
        if (description.length() == 0) {
            Log.d(TAG, "buildClaim: Claim has no description");
            Toast.makeText(context, "Claim has no description", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (location == null) {
            Log.d(TAG, "buildClaim: Location not selected");
            Toast.makeText(context, "Location not selected", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (photoUri == null) {
            Log.d(TAG, "buildClaim: No photo selected");
            Toast.makeText(context, "No photo selected", Toast.LENGTH_SHORT).show();
            return null;
        }

        return new Claim(id, description, photoUri.toString(),
                MapUtil.Location.latLngToLocationString(location));
    }
}
