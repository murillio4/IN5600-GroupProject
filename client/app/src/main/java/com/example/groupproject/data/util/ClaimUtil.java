package com.example.groupproject.data.util;

import android.os.Bundle;
import android.util.Log;

import com.example.groupproject.data.Constants;
import com.example.groupproject.data.model.Claim;

public class ClaimUtil {

    private static final String TAG = "ClaimUtil";

    private ClaimUtil() {}

    public static Claim getClaimFromBundle(Bundle bundle) {
        if (bundle == null) {
            return null;
        }

        return (Claim) bundle.getSerializable(Constants.Serializable.Claim);
    }

    public static Bundle createBundleFromClaim(Claim claim) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.Serializable.Claim, claim);
        return bundle;
    }

    public static boolean verifyClaim(Claim claim) {
        if (claim == null) {
            Log.d(TAG, "verifyClaim: Claim was null");
            return false;
        }

        if (claim.getId() == null) {
            Log.d(TAG, "buildClaim: Failed to get next claim id");
            return true;
        }

        if (claim.getDescription() == null || claim.getDescription().length() == 0) {
            Log.d(TAG, "buildClaim: Claim has no description");
            return true;
        }

        if (claim.getLocation() == null) {
            Log.d(TAG, "buildClaim: Location not selected");
            return true;
        }

        if (claim.getPhotoPath() == null) {
            Log.d(TAG, "buildClaim: No photo selected");
            return false;
        }

        return true;
    }
}
