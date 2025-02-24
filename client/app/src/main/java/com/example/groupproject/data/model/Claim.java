package com.example.groupproject.data.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Locale;

public class Claim implements Serializable, Cloneable {

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

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public void setLocation(String location) {
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

    public String toString() {
        return String.format(
                Locale.getDefault(), "Claim(%s,%s,%s,%s)", id, description, photoPath, location);
    }

    @NonNull
    @Override
    public Claim clone() {
        try {
            return (Claim) super.clone();
        } catch (CloneNotSupportedException e) {
            return new Claim(id, description, photoPath, location);
        }
    }
}
