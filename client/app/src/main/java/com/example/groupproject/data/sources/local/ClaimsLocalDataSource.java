package com.example.groupproject.data.sources.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.groupproject.data.Constants;
import com.example.groupproject.data.model.BaseModel;
import com.example.groupproject.data.model.ClaimList;
import com.example.groupproject.data.model.Claims;
import com.google.gson.Gson;

public class ClaimsLocalDataSource extends BaseLocalDataSource {

    ClaimList claimList;

    public ClaimsLocalDataSource(Context context, Gson gson) {
        super(context, gson, Constants.SharedPreferences.Name.ClaimList);

        String claimsString = sharedPreferences.getString(
                Constants.SharedPreferences.Keys.ClaimList, null);
        claimList = claimsString == null
                ? null
                : gson.fromJson(claimsString, ClaimList.class);
    }

    public <T extends BaseModel> void setData(T data) {
        this.claimList = (ClaimList)data;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SharedPreferences.Keys.ClaimList, new Gson().toJson(data));
        editor.apply();
    }

    public <T extends BaseModel> T getData() {
        return (T)claimList;
    }

    public <T extends BaseModel> void removeData(T data) {
        sharedPreferences.edit().clear().apply();
    }
}
