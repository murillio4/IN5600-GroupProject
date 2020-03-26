package com.example.groupproject.data.sources.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.groupproject.data.Constants;
import com.example.groupproject.data.model.BaseModel;
import com.google.gson.Gson;

public abstract class BaseLocalDataSource {
    protected Gson gson;
    protected SharedPreferences sharedPreferences;

    public BaseLocalDataSource(Context context, Gson gson, String sharedPreferenceName) {
        this.gson = gson;
        this.sharedPreferences = context.getSharedPreferences(
                sharedPreferenceName, Context.MODE_PRIVATE);
    }

    public abstract <T extends BaseModel> void setData(T data);

    public abstract <T extends BaseModel> T getData();

    public abstract <T extends BaseModel> void removeData(T data);
}
