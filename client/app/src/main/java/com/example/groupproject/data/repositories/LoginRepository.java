package com.example.groupproject.data.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.groupproject.data.Constants;
import com.example.groupproject.data.sources.LoginDataSource;
import com.example.groupproject.data.Result;
import com.example.groupproject.data.model.LoggedInUser;
import com.google.gson.Gson;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {
    private static volatile LoginRepository instance;

    private Context context;
    private SharedPreferences pref;
    private LoginDataSource dataSource;

    private LoggedInUser user = null;

    // private constructor : singleton access
    private LoginRepository(Context context, LoginDataSource dataSource) {
        this.dataSource = dataSource;
        this.context = context;
        this.pref = context.getSharedPreferences(Constants.SharedPreferences.Name, Context.MODE_PRIVATE);

        String userString = pref.getString(Constants.SharedPreferences.Keys.User, null);
        this.user = userString == null
                ? null
                : new Gson().fromJson(userString, LoggedInUser.class);
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

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;

        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.SharedPreferences.Keys.User, new Gson().toJson(user));
        editor.apply();
    }

    public Result<LoggedInUser> login(String username, String password) {
        // handle login
        Result<LoggedInUser> result = dataSource.login(username, password);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;
    }

    public LoggedInUser getUser() {
        return user;
    }
}
