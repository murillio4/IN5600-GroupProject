package com.example.groupproject.data.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {

    private static Gson gson;

    private GsonUtils() {}

    public static Gson getReusableGson() {
        if (gson == null) {
            gson = (new GsonBuilder()).create();
        }

        return gson;
    }

}