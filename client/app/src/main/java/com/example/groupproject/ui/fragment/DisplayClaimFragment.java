package com.example.groupproject.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groupproject.R;
import com.example.groupproject.data.Constants;
import com.example.groupproject.data.model.Claim;
import com.example.groupproject.ui.viewModel.ClaimsViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileDescriptor;
import java.io.IOException;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class DisplayClaimFragment extends DaggerFragment implements OnMapReadyCallback {
    private static final String TAG = "DisplayClaimActivity";

    @Inject
    ClaimsViewModel claimsViewModel;

    private Claim claim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_display_claim, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle extras = getArguments();
        this.claim = (Claim) extras.getSerializable(Constants.Serializable.Claim);

        initIdTextView();
        initMapFragment();
        initImageButton();
        initDescriptionTitleTextView();
        initDescriptionTextView();
        initEditButton();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        final String mapTitle = "Claim #" + claim.getId();
        final LatLng location = stringLocationToLatLng(claim.getLocation());

        CameraPosition cameraPosition = CameraPosition.builder()
                .target(location)
                .zoom(10)
                .bearing(0)
                .build();

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.clear();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, null);
        map.addMarker(new MarkerOptions().position(location).title(mapTitle));
    }

    private LatLng stringLocationToLatLng(String locationString) {
        String[] locationArray = claim.getLocation().split(",", 2);
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

    private void initIdTextView() {
        final TextView idTextView = getView().findViewById(R.id.view_claim_id);
        idTextView.setText("Claim #" + claim.getId());
    }

    private void initMapFragment() {
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.view_claim_map_fragment);
        mapFragment.getMapAsync(this);
    }

    // From https://developer.android.com/training/data-storage/shared/documents-files#open
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getActivity().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    private BitmapDrawable getBitmapDrawableFromUri(Uri uri) throws IOException {
        return new BitmapDrawable(null, getBitmapFromUri(uri));
    }

    private void initImageButton() {
        final Button imageButton = getView().findViewById(R.id.view_claim_image_button);

        try {
            imageButton.setBackground(getBitmapDrawableFromUri(Uri.parse(claim.getPhotoPath())));
        } catch(IOException e) {
            // Handle ..
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "View image", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(claim.getPhotoPath()), "image/*");
                startActivity(intent);
            }
        });
    }

    private void initDescriptionTitleTextView() {
        final TextView descriptionTitleTextView =
                getView().findViewById(R.id.view_claim_description_title);
    }

    private void initDescriptionTextView() {
        final TextView descriptionTextView = getView().findViewById(R.id.view_claim_description);
        descriptionTextView.setText(claim.getDescription());
    }

    private void initEditButton() {
        final Button editButton = getView().findViewById(R.id.view_claim_edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Edit claim", Toast.LENGTH_LONG).show();
                Bundle extras = new Bundle();
                extras.putSerializable(Constants.Serializable.Claim, claim);
                UpdateClaimFragment updateClaimFragment = new UpdateClaimFragment();
                updateClaimFragment.setArguments(extras);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_fragment_container, updateClaimFragment)
                        .addToBackStack(TAG)
                        .commit();
            }
        });
    }
}
