package com.example.groupproject.data.util;

import android.os.Bundle;

import com.example.groupproject.data.Constants;
import com.example.groupproject.data.model.Claim;

public class ClaimUtil {

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
}
