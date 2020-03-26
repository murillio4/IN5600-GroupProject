package com.example.groupproject.data.sources.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.groupproject.data.Constants;
import com.example.groupproject.data.model.Person;
import com.google.gson.Gson;

public class SessionLocalDataSource {
    private Gson gson;
    private SharedPreferences pref;
    private Person user;

    public SessionLocalDataSource(Context context, Gson gson) {
        this.gson = gson;
        this.pref = context.getSharedPreferences(Constants.SharedPreferences.Name, Context.MODE_PRIVATE);

        String userString = pref.getString(Constants.SharedPreferences.Keys.User, null);
        this.user = userString == null
                ? null
                : gson.fromJson(userString, Person.class);
    }

    public void setUser(Person user) {
        this.user = user;

        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.SharedPreferences.Keys.User, new Gson().toJson(user));
        editor.apply();
    }

    public Person getUser() {
        return user;
    }

    public void removeUser() {
        pref.edit().clear().apply();
    }
}
