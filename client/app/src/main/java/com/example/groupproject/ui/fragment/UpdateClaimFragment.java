package com.example.groupproject.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.groupproject.R;
import com.example.groupproject.data.Constants;
import com.example.groupproject.data.model.Claim;
import com.example.groupproject.data.util.TransitionUtil;

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
        if (extras == null) {
            unexpectedError();
            return;
        }

        claim = (Claim)extras.getSerializable(Constants.Serializable.Claim);

        if (claim == null) {
            unexpectedError();
            return;
        }

        descriptionEditText.setText(claim.getDescription());

        TextView textView = view.findViewById(R.id.create_claim_title);
        textView.setText(getString(R.string.update_claim_title, claim.getId()));
    }

    @Override
    protected Claim initClaim() {
        return new Claim(null, null, null, null);
    }

    @Override
    protected void postClaim() {
        claimsViewModel.updateClaim(claim).observe(getActivity(), updateClaimResult -> {
            if (updateClaimResult == null) {
                return;
            }

            if (updateClaimResult.getError() != null) {
                Log.d(TAG, "onViewCreated: Failed to update claim" + claim.getId());
            } else if (updateClaimResult.getSuccess() != null) {
                Toast.makeText(context, R.string.update_claim_success, Toast.LENGTH_SHORT).show();
                TransitionUtil.toPreviousFragment(getActivity());
                TransitionUtil.toPreviousFragment(getActivity());
            }
        });
    }

    private void unexpectedError() {
        Toast.makeText(context, R.string.unexpected_error, Toast.LENGTH_SHORT).show();
        TransitionUtil.toPreviousFragment(getActivity());
    }
}
