package com.example.groupproject.data.model;

import com.example.groupproject.data.Constants;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.io.Serializable;

/**
 * Structure of Claims(s) when stored and used by client.
 */
public class ClaimList implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("numberOfClaims")
    private int numberOfClaims;

    @SerializedName("claims")
    private List<Claim> claims = new ArrayList<>();

    public ClaimList(String id, int numberOfClaims, List<Claim> claims) {
        this.id = id;
        this.numberOfClaims = numberOfClaims;
        this.claims = claims;
    }

    public String getId() {
        return id;
    }

    public List<Claim> getClaims() {
        return  claims;
    }

    public boolean addClaim(Claim claim) {
        if (numberOfClaims < Constants.Claim.CLAIM_MAX_COUNT) {
            this.claims.add(claim);
            ++numberOfClaims;
            return true;
        } else {
            return false;
        }
    }

    public boolean setClaim(Claim claim) {
        for (int i = 0; i < claims.size(); ++i) {
            if (claims.get(i).getId().equals(claim.getId())) {
                return claims.set(i, claim) != null;
            }
        }

        return false;
    }

    public Claim getClaim(String id) {
        for (Claim claim : claims) {
            if (claim.getId().equals(id)) {
                return claim;
            }
        }
        return null;
    }

    public String getNextClaimId() {
        if (claims.size() < Constants.Claim.CLAIM_MAX_COUNT) {
            return String.format(Locale.getDefault(), "%d", claims.size());
        } else {
            return null;
        }
    }
}
