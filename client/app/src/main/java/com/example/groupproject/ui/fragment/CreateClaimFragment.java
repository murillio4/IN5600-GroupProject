package com.example.groupproject.ui.fragment;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;

import com.example.groupproject.R;

import java.util.Objects;

import dagger.android.support.DaggerFragment;

public class CreateClaimFragment extends DaggerFragment {

    private static final String TAG = "CreateClaimFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_claim, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initDescriptionInput();
        initAddMapLocationButton();
        initAddPhotoButton();
        initSubmitButton();
    }

    protected void toClaimListFragment() {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment_container, new ClaimListFragment());
        fragmentTransaction.commit();
    }

    protected void showClaimSuccess() {
        ///
    }

    protected void showClaimFailed() {
        ///
    }

    protected void initDescriptionInput() {
        final EditText editText = getView().findViewById(R.id.create_claim_description_input);
    }

    protected void initAddMapLocationButton() {
        getView().findViewById(R.id.create_claim_add_map_location_button)
                .setOnClickListener(v -> {
                    Toast.makeText(getActivity(), "Add Map Location", Toast.LENGTH_SHORT)
                            .show();
                    // Do some magic here ...
                });
    }

    protected void initAddPhotoButton() {
        getView().findViewById(R.id.create_claim_add_photo_button)
                .setOnClickListener(v -> {
                    Toast.makeText(getActivity(), "Add Photo", Toast.LENGTH_SHORT)
                            .show();
                    // Do some magic here ...
                });
    }

    protected void initSubmitButton() {
        getView().findViewById(R.id.create_claim_submit_button)
                .setOnClickListener(v -> {
                    Toast.makeText(getActivity(), "Submit", Toast.LENGTH_SHORT)
                            .show();
                    // Do some magic here ...
                    toClaimListFragment();
                });
    }
}
