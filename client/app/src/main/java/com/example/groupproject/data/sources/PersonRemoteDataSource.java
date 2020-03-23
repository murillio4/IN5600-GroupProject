package com.example.groupproject.data.sources;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.android.volley.RequestQueue;
import com.example.groupproject.data.Constants;
import com.example.groupproject.data.network.model.Result;
import com.example.groupproject.data.model.Person;
import com.example.groupproject.data.network.request.GsonRequest;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class PersonRemoteDataSource extends BaseSource {


    public PersonRemoteDataSource(RequestQueue requestQueue) {
        super(requestQueue);
    }

    public LiveData<Result<Person>> login(String username, String password) {
        System.out.println("ULJANSDKJNASDKJNSADKJNSAKJDSADKJNASDKNJ");

        return GsonRequest.Builder.post(Constants.Api.Base + Constants.Api.SignIn, Person.class)
                .addQueryParam("em", username)
                .addQueryParam("ph", password)
                .build()
                .enqueue(requestQueue);
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
