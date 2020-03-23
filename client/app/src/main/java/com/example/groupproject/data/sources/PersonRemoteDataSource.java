package com.example.groupproject.data.sources;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.groupproject.data.Constants;
import com.example.groupproject.data.network.model.Result;
import com.example.groupproject.data.model.Person;
import com.example.groupproject.data.network.request.GsonRequest;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class PersonRemoteDataSource extends BaseSource {


    public PersonRemoteDataSource(Context context) {
        super(context);
    }

    public LiveData<Result<Person>> login(String username, String password) {
        return GsonRequest.Builder.post(Constants.Api.Base + Constants.Api.SignIn, Person.class)
                .addQueryParam("em", username)
                .addQueryParam("ph", password)
                .build()
                .enqueue(getRequestQueue());
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
