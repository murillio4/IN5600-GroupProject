package com.example.groupproject.data.sources;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.groupproject.data.Constants;
import com.example.groupproject.data.Result;
import com.example.groupproject.data.model.Person;
import com.example.groupproject.data.model.SignInRequest;
import com.example.groupproject.data.utils.JsonApiRequest;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource extends BaseSource {


    public LoginDataSource(Context context) {
        super(context);
    }

    public LiveData<Result<Person>> login(String username, String password) {
        return JsonApiRequest.post(Person.class, Constants.Api.Base, new SignInRequest(username, password), null)
            .enqueue(getRequestQueue());
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
