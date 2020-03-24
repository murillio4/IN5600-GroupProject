package com.example.groupproject.data.sources.remote;

import com.example.groupproject.data.Constants;
import com.example.groupproject.data.model.Person;
import com.example.groupproject.data.network.request.VolleyRequest;
import com.example.groupproject.data.sources.remote.BaseRemoteSource;

import java.util.Optional;

import io.reactivex.rxjava3.core.Observable;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class PersonRemoteDataSource extends BaseRemoteSource {

    public PersonRemoteDataSource(VolleyRequest volleyRequest) {
        super(volleyRequest);
    }

    public Observable<Optional<Person>> login(String username, String password) {
        return volleyRequest.post(Constants.Api.SignIn, Person.class)
                .addQueryParam("em", username)
                .addQueryParam("ph", password)
                .build()
                .enqueue();
    }
}
