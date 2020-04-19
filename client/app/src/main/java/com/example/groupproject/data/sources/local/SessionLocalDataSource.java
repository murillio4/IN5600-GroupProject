package com.example.groupproject.data.sources.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.groupproject.data.Constants;
import com.example.groupproject.data.model.Person;
import com.google.gson.Gson;

public class SessionLocalDataSource {

    private Gson gson;
    private SharedPreferences sharedPreferences;

    public SessionLocalDataSource(Context context, Gson gson) {
        this.gson = gson;
        this.sharedPreferences = context.getSharedPreferences(
                Constants.SharedPreferences.Name.Person, Context.MODE_PRIVATE);
    }

    public void setUser(Person user) {
        sharedPreferences.edit()
                .putString(Constants.SharedPreferences.Keys.Person, gson.toJson(user))
                .apply();
    }

    public Person getUser() {
        return getUserFromSharedPreferences();
    }

    public void removeUser() {
        sharedPreferences.edit().clear().apply();
    }

    private String getUserStringFromSharedPreferences() {
        return sharedPreferences.getString(Constants.SharedPreferences.Keys.Person, null);
    }

    private Person getUserFromSharedPreferences() {
        return gson.fromJson(getUserStringFromSharedPreferences(), Person.class);
    }
}
