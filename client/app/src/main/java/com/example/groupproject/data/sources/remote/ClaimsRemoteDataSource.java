package com.example.groupproject.data.sources.remote;

import com.example.groupproject.data.Constants;
import com.example.groupproject.data.model.Claims;
import com.example.groupproject.data.model.Person;
import com.example.groupproject.data.network.request.VolleyRequest;

import java.util.Optional;

import io.reactivex.rxjava3.core.Observable;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class ClaimsRemoteDataSource extends BaseRemoteSource {

    public ClaimsRemoteDataSource(VolleyRequest volleyRequest) {
        super(volleyRequest);
    }

    public Observable<Optional<Claims>> getClaims(String id) {
        return volleyRequest.get(Constants.Api.GetClaims, Claims.class)
                .addQueryParam("id", id)
                .build()
                .enqueue();
    }
}
