package com.example.groupproject.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.groupproject.R;
import com.example.groupproject.data.model.Claim;
import com.example.groupproject.data.util.ClaimUtil;
import com.example.groupproject.data.util.MapUtil;
import com.example.groupproject.data.util.PermissionUtil;
import com.example.groupproject.data.util.TransitionUtil;
import com.example.groupproject.ui.viewModel.ClaimsViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class DisplayClaimFragment extends DaggerFragment
        implements OnMapReadyCallback, View.OnClickListener{

    private static final String TAG = "DisplayClaimActivity";

    private Claim claim;

    private ImageView claimImage;

    private String[] permissions = new String[]{
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Inject
    ClaimsViewModel claimsViewModel;

    @Inject
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_claim, container, false);

        view.findViewById(R.id.display_claim_image_view).setOnClickListener(this);
        view.findViewById(R.id.display_claim_edit_button).setOnClickListener(this);

        claimImage = view.findViewById(R.id.display_claim_image_view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if ((this.claim = ClaimUtil.getClaimFromBundle(getArguments())) == null) {
            TransitionUtil.toPreviousFragment(getActivity());
        }

        ((TextView)view.findViewById(R.id.display_claim_id))
                .setText(getString(R.string.claim_number, claim.getId()));
        ((TextView)view.findViewById(R.id.display_claim_description))
                .setText(claim.getDescription());

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
        PermissionUtil.requestPermissions(
                context,
                permissions,
                callback,
                dexterError -> TransitionUtil.toPreviousFragment(getActivity()));
    }

    private void initMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                                            .findFragmentById(R.id.display_claim_map_fragment);

        if (mapFragment == null) {
            TransitionUtil.toPreviousFragment(getActivity());
        } else {
            mapFragment.getMapAsync(this);
        }
    }

    private void initImageView() {
        Glide.with(context)
                .asBitmap()
                .load(claim.getPhotoPath())
                .error(R.drawable.ic_error_red_24dp)
                .into(this.claimImage);
    }

    private void toUpdateClaimFragment() {
        Bundle bundle = ClaimUtil.createBundleFromClaim(this.claim.clone());
        UpdateClaimFragment updateClaimFragment = new UpdateClaimFragment();
        updateClaimFragment.setArguments(bundle);
        TransitionUtil.toNextFragment(getActivity(), TAG, updateClaimFragment);
    }

    private void startViewImageActivity() {
        Log.d(TAG, "startViewImageActivity: " + claim.getPhotoPath());
        Intent intent = new Intent()
                .setAction(Intent.ACTION_VIEW)
                .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                .setDataAndType(Uri.parse(claim.getPhotoPath()), "image/*");
        startActivity(intent);
    }
}
