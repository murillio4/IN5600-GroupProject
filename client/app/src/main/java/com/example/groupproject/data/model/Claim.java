package com.example.groupproject.data.model;

import com.google.gson.annotations.SerializedName;

public class Claim {

    @SerializedName("id")
    private String id;

    @SerializedName("description")
    private String description;

    @SerializedName("photoPath")
    private String photoPath;

    @SerializedName("location")
    private String location;

    public Claim(String id, String description, String photoPath, String location) {
        this.id = id;
        this.description = description;
        this.photoPath = photoPath;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public String getLocation() {
        return location;
    }
}
