package com.example.groupproject.data.model;

import com.example.groupproject.data.Constants;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Structure of Claims(s) when stored and used by client.
 */
public class ClaimList extends BaseModel {

    @SerializedName("id")
    private String id;

    @SerializedName("claims")
    private List<Claim> claims = new ArrayList<>();

    public ClaimList(Claims remoteClaims) {
        this.id = remoteClaims.getId();
        // Check lengths of lists from remteClaims for proper zip
        IntStream.range(0, remoteClaims.getClaimId().size())
                .forEach(i -> claims.add(new Claim(
                            remoteClaims.getClaimId().get(i),
                            remoteClaims.getClaimDes().get(i),
                            remoteClaims.getClaimPhoto().get(i),
                            remoteClaims.getClaimLocation().get(i))));
    }

    public String getKey() {
        return Constants.SharedPreferences.Keys.ClaimList;
    }

    public String getId() {
        return id;
    }

    public List<Claim> getClaims() {
        return  claims;
    }

    public void setId(String id) {
       this.id = id;
    }

    public void addClaim(Claim claim) {
       this.claims.add(claim);
    }
}
