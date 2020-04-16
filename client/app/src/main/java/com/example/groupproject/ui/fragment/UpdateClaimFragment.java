package com.example.groupproject.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.groupproject.R;
import com.example.groupproject.data.Constants;
import com.example.groupproject.data.model.Claim;

import java.util.Objects;

public class UpdateClaimFragment extends CreateClaimFragment {

    private static final String TAG = "UpdateClaimFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle extras = getArguments();
        claim = (Claim)extras.getSerializable(Constants.Serializable.Claim);
        Log.d(TAG, "onCreate: " + claim);
        descriptionEditText.setText(claim.getDescription());

        TextView textView = Objects.requireNonNull(getView()).findViewById(R.id.create_claim_title);
        textView.setText("Update claim " + claim.getId());
    }

    @Override
    protected Claim initClaim() {
        return new Claim(null, null, null, null);
    }

    @Override
    protected void postClaim() {
        claimsViewModel.updateClaim(claim)
                .observe(Objects.requireNonNull(getActivity()), buildResultObserver());
    }
}
