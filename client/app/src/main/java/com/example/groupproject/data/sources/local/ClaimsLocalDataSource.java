package com.example.groupproject.data.sources.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.groupproject.data.Constants;
import com.example.groupproject.data.model.Claim;
import com.example.groupproject.data.model.ClaimList;
import com.google.gson.Gson;

public class ClaimsLocalDataSource {

    private Gson gson;
    private SharedPreferences sharedPreferences;

    public ClaimsLocalDataSource(Context context, Gson gson) {
        this.gson = gson;
        this.sharedPreferences = context.getSharedPreferences(
                Constants.SharedPreferences.Name.ClaimList, Context.MODE_PRIVATE);
    }

    public void setClaimList(ClaimList claimList) {
        sharedPreferences.edit()
                .putString(Constants.SharedPreferences.Keys.ClaimList, gson.toJson(claimList))
                .apply();
    }

    public ClaimList getClaimList() {
        return getClaimListFromSharedPreferences();
    }

    public void removeClaimList() {
        sharedPreferences.edit().clear().apply();
    }

    public void addClaim(Claim claim) {
        ClaimList claimList = getClaimListFromSharedPreferences();

        if (claimList == null) {
            return;
        }

        if (!claimList.addClaim(claim)) {
            return;
        }

        setClaimList(claimList);
    }

    public void setClaim(Claim claim) {
        ClaimList claimList = getClaimListFromSharedPreferences();

        if (claimList == null) {
            return;
        }

        if (!claimList.setClaim(claim)) {
            return;
        }

        setClaimList(claimList);
    }

    public Claim getClaim(String id) {
        ClaimList claimList = getClaimListFromSharedPreferences();

        if (claimList == null) {
            return null;
        }

        return claimList.getClaim(id);
    }

    public String getNextClaimId() {
        ClaimList claimList = getClaimListFromSharedPreferences();

        if (claimList == null) {
            return null;
        }

        return claimList.getNextClaimId();
    }

    private String getClaimListStringFromSharedPreferences() {
        return sharedPreferences.getString(Constants.SharedPreferences.Keys.ClaimList, null);
    }

    private ClaimList getClaimListFromSharedPreferences() {
        return gson.fromJson(getClaimListStringFromSharedPreferences(), ClaimList.class);
    }

}
