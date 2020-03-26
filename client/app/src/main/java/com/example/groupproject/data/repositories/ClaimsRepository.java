package com.example.groupproject.data.repositories;

import androidx.annotation.NonNull;

import com.example.groupproject.data.NetworkBoundResource;
import com.example.groupproject.data.Resource;
import com.example.groupproject.data.model.Claim;
import com.example.groupproject.data.model.ClaimList;
import com.example.groupproject.data.model.Claims;
import com.example.groupproject.data.sources.local.ClaimsLocalDataSource;
import com.example.groupproject.data.sources.remote.ClaimsRemoteDataSource;

import java.util.stream.IntStream;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;

public class ClaimsRepository {
    private static final String TAG = "ClaimRepository";

    private ClaimsRemoteDataSource claimsRemoteDataSource;
    private ClaimsLocalDataSource claimsLocalDataSource;

    public ClaimsRepository(ClaimsRemoteDataSource claimsRemoteDataSource, ClaimsLocalDataSource claimsLocalDataSource) {
        this.claimsRemoteDataSource = claimsRemoteDataSource;
        this.claimsLocalDataSource = claimsLocalDataSource;
    }

    public void clear() {
        // Cleare shared perefernces here
    }

    public Observable<Resource<ClaimList>> getClaims(String id) {
        return new NetworkBoundResource<ClaimList, Claims>() {
            @Override
            protected boolean shouldFetchRemote() {
                return true;
            }

            @Override
            protected void saveRemoteResult(@NonNull Claims item) {
                claimsLocalDataSource.setData(new ClaimList(item));
            }

            @NonNull
            @Override
            protected Flowable<ClaimList> fetchLocal() {
                ClaimList claimList = claimsLocalDataSource.getData();
                return claimList == null
                        ? Flowable.error(new Throwable("No claims stored"))
                        : Flowable.just(claimList);
            }

            @NonNull
            @Override
            protected Observable<Resource<Claims>> fetchRemote() {
                return claimsRemoteDataSource.getClaims(id)
                        .flatMap(remoteResponse ->
                                remoteResponse
                                    .map(claims -> Observable.just(Resource.success(claims)))
                                    .orElseGet(() -> Observable.error(
                                            new Throwable("Error fetching claims")
                                    )));
            }
        }.getAsObservable();
    }
}

