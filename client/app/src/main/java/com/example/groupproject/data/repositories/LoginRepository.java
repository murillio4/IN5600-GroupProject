package com.example.groupproject.data.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.groupproject.data.Constants;
import com.example.groupproject.data.network.model.Result;
import com.example.groupproject.data.sources.LoginDataSource;
import com.example.groupproject.data.model.Person;
import com.google.gson.Gson;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {
    private static volatile LoginRepository instance;
    private SharedPreferences pref;
    private LoginDataSource dataSource;

    private final Observer<Result<Person>> loginObserver = personResource -> {
        if (personResource.getStatus() == Result.Status.SUCCESS) {
            setLoggedInUser(personResource.getData());
        }
    };

    private Person user = null;

    // private constructor : singleton access
    private LoginRepository(Context context, LoginDataSource dataSource) {
        this.dataSource = dataSource;
        this.pref = context.getSharedPreferences(Constants.SharedPreferences.Name, Context.MODE_PRIVATE);

        String userString = pref.getString(Constants.SharedPreferences.Keys.User, null);
        this.user = userString == null
                ? null
                : new Gson().fromJson(userString, Person.class);
    }

    public static LoginRepository getInstance(Context context, LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(context, dataSource);
        }

        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        pref.edit().clear().apply();
        dataSource.logout();
    }

    private void setLoggedInUser(Person user) {
        this.user = user;

        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.SharedPreferences.Keys.User, new Gson().toJson(user));
        editor.apply();
    }

    public LiveData<Result<Person>> login(String username, String password) {
        // handle login
        LiveData<Result<Person>> result = dataSource.login(username, password);
        result.observeForever(loginObserver);

        return result;
    }

    public Person getUser() {
        return user;
    }
}
