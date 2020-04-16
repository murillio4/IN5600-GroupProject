package com.example.groupproject.data.repositories;

import androidx.annotation.NonNull;

import com.example.groupproject.data.Constants;
import com.example.groupproject.data.NetworkBoundResource;
import com.example.groupproject.data.Resource;
import com.example.groupproject.data.model.Claim;
import com.example.groupproject.data.model.ClaimList;
import com.example.groupproject.data.model.Claims;
import com.example.groupproject.data.sources.local.ClaimsLocalDataSource;
import com.example.groupproject.data.sources.remote.ClaimsRemoteDataSource;

import java.util.ArrayList;
import java.util.List;
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
        claimsLocalDataSource.removeClaimList();
    }

    public String getNextClaimId() {
        return claimsLocalDataSource.getNextClaimId();
    }

    public Observable<Resource<ClaimList>> getClaims(String id) {
        return buildGetClaimsNetworkBoundResource(id).getAsObservable();
    }

    public Observable<Resource<String>> createClaim(String userId, Claim claim) {
        return buildCreateClaimNetworkBoundResource(userId, claim).getAsObservable();
    }

    public Observable<Resource<String>> updateClaim(String userId, Claim claim) {
        return buildUpdateClaimNetworkBoundResource(userId, claim).getAsObservable();
    }

    private NetworkBoundResource<String, String> buildCreateClaimNetworkBoundResource(String userId, Claim claim) {
        return new NetworkBoundResource<String, String>() {
            @Override
            protected boolean shouldFetchRemote() {
                return true;
            }

            @Override
            protected void saveRemoteResult(@NonNull String item) {
                claimsLocalDataSource.addClaim(claim);
            }

            @NonNull
            @Override
            protected Flowable<String> fetchLocal() {
                return Flowable.just("OK");
            }

            @NonNull
            @Override
            protected Observable<Resource<String>> fetchRemote() {
                return claimsRemoteDataSource.createClaim(
                        userId,
                        claim.getId(),
                        claim.getDescription(),
                        claim.getPhotoPath(),
                        claim.getLocation()
                ).flatMap(remoteResponse -> remoteResponse
                        .map(message -> Observable.just(Resource.success(message)))
                        .orElseGet(() -> Observable.error(new Throwable("Create failed!"))));
            }
        };
    }

    private NetworkBoundResource<String, String> buildUpdateClaimNetworkBoundResource(String userId, Claim claim) {
        return new NetworkBoundResource<String, String>() {
            @Override
            protected boolean shouldFetchRemote() {
                return true;
            }

            @Override
            protected void saveRemoteResult(@NonNull String item) {
                claimsLocalDataSource.setClaim(claim);
            }

            @NonNull
            @Override
            protected Flowable<String> fetchLocal() {
                return Flowable.just("OK");
            }

            @NonNull
            @Override
            protected Observable<Resource<String>> fetchRemote() {
                return claimsRemoteDataSource.updateClaim(
                        userId,
                        claim.getId(),
                        claim.getDescription(),
                        claim.getPhotoPath(),
                        claim.getLocation()
                ).flatMap(remoteResponse -> remoteResponse
                        .map(message -> Observable.just(Resource.success(message)))
                        .orElseGet(() -> Observable.error(new Throwable("Update failed!"))));
            }
        };
    }

    private NetworkBoundResource<ClaimList, Claims> buildGetClaimsNetworkBoundResource(String id) {
        return new NetworkBoundResource<ClaimList, Claims>() {
            @Override
            protected boolean shouldFetchRemote() {
                return true; // ONLY OF NOT CACHED!!!
            }

            @Override
            protected void saveRemoteResult(@NonNull Claims item) {
                String id = item.getId();
                int numberOfClaims = Integer.parseInt(item.getNumberOfClaims());
                List<Claim> claims = new ArrayList<>();
                IntStream.range(0, item.getClaimId().size())
                        .forEach(i -> {
                            if (!item.getClaimId().get(i).equals(Constants.Claim.ITEM_NOT_AVAILABLE)) {
                                claims.add(new Claim(
                                        item.getClaimId().get(i),
                                        item.getClaimDes().get(i),
                                        item.getClaimPhoto().get(i),
                                        item.getClaimLocation().get(i)));
                            }
                        });
                claimsLocalDataSource.setClaimList(new ClaimList(id, numberOfClaims, claims));
            }

            @NonNull
            @Override
            protected Flowable<ClaimList> fetchLocal() {
                ClaimList claimList = claimsLocalDataSource.getClaimList();
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
        };
    }
}

