package com.example.groupproject.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.groupproject.R;
import com.example.groupproject.data.model.Claim;
import com.example.groupproject.data.util.ClaimUtil;
import com.example.groupproject.data.util.ImageUtil;
import com.example.groupproject.data.util.MapUtil;
import com.example.groupproject.ui.viewModel.ClaimsViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class DisplayClaimFragment extends DaggerFragment
        implements OnMapReadyCallback, View.OnClickListener{

    private static final String TAG = "DisplayClaimActivity";

    @Inject
    ClaimsViewModel claimsViewModel;

    @Inject
    Context context;

    private Claim claim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_claim, container, false);

        view.findViewById(R.id.display_claim_image_view).setOnClickListener(this);
        view.findViewById(R.id.display_claim_edit_button).setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if ((this.claim = ClaimUtil.getClaimFromBundle(getArguments())) == null) {
            toPreviousFragment();
        }

        ((TextView)view.findViewById(R.id.display_claim_id)).setText("Claim #" + claim.getId());
        ((TextView)view.findViewById(R.id.display_claim_description)).setText(claim.getDescription());

        requestPermissions(() -> {
            initMapFragment();
            initImageView();
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        final LatLng latLng = MapUtil.locationStringToLatLng(claim.getLocation());

        map.clear();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (latLng != null) {
            map.animateCamera(CameraUpdateFactory.newCameraPosition(
                    MapUtil.buildCameraPosition(latLng)), 1000, null);
            map.addMarker(new MarkerOptions().position(latLng).title(claim.getId()));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.display_claim_image_view:
                startViewImageActivity();
                break;
            case R.id.display_claim_edit_button:
                toUpdateClaimFragment();
                break;
            default:
                break;
        }

    }

    private void requestPermissions(Runnable callback) {
        Dexter.withContext(getContext())
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE
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
                .withErrorListener(error -> {
                    Log.d(TAG, "requestPermissions: Failed to request permissions");
                    toPreviousFragment();
                })
                .onSameThread()
                .check();
    }

    private void initMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                                            .findFragmentById(R.id.display_claim_map_fragment);

        if (mapFragment == null) {
            toPreviousFragment();
        } else {
            mapFragment.getMapAsync(this);
        }
    }

    private void initImageView() {
        Bitmap bitmap = ImageUtil.getBitmapFromUri(context, Uri.parse(claim.getPhotoPath()));
        if (bitmap != null) {
            ((ImageView)getView().findViewById(R.id.display_claim_image_view)).setImageBitmap(bitmap);
        }
    }

    private void toPreviousFragment() {
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
    }

    private void toUpdateClaimFragment() {
        Bundle bundle = ClaimUtil.createBundleFromClaim(this.claim);
        UpdateClaimFragment updateClaimFragment = createDisplayClaimFragment(bundle);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, updateClaimFragment)
                .addToBackStack(TAG)
                .commit();

    }

    private void startViewImageActivity() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(claim.getPhotoPath()), "image/*");
        startActivity(intent);
    }

    private UpdateClaimFragment createDisplayClaimFragment(Bundle extras) {
        UpdateClaimFragment updateClaimFragment = new UpdateClaimFragment();
        updateClaimFragment.setArguments(extras);
        return updateClaimFragment;
    }
}
