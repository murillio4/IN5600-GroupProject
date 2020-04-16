package com.example.groupproject.data.model;

import com.example.groupproject.data.Constants;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Structure of Claim(s) when fetched from remote source.
 */
public class Claims {

    @SerializedName("id")
    private String id;

    @SerializedName("numberOfClaims")
    private String numberOfClaims;

    @SerializedName("claimId")
    private List<String> claimId = new ArrayList<>();

    @SerializedName("claimDes")
    private List<String> claimDes = new ArrayList<>();

    @SerializedName("claimPhoto")
    private List<String> claimPhoto = new ArrayList<>();

    @SerializedName("claimLocation")
    private List<String> claimLocation = new ArrayList<>();

    public String getId() {
        return id;
    }

    public String getNumberOfClaims() {
        return numberOfClaims;
    }

    public List<String> getClaimId() {
        return claimId;
    }

    public List<String> getClaimDes() {
        return claimDes;
    }

    public List<String> getClaimPhoto() {
        return claimPhoto;
    }

    public List<String> getClaimLocation() {
        return claimLocation;
    }
}
