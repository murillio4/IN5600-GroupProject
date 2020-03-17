package com.example.groupproject.data.model;

import com.google.gson.annotations.SerializedName;

public class SignInRequest {
    @SerializedName("em")
    private String username;

    @SerializedName("ph")
    private String password;

    public SignInRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
