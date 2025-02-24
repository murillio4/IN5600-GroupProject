package com.example.groupproject.data.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.groupproject.data.network.model.NetworkBoundResource;
import com.example.groupproject.data.network.model.Resource;
import com.example.groupproject.data.sources.local.SessionLocalDataSource;
import com.example.groupproject.data.sources.remote.SessionRemoteDataSource;
import com.example.groupproject.data.model.Person;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class SessionRepository {
    private static final String TAG = "SessionRepository";

    private SessionRemoteDataSource remoteDataSource;
    private SessionLocalDataSource localDataSource;

    private PublishSubject<Person> personPublishSubject = PublishSubject.create();

    public SessionRepository(SessionRemoteDataSource remoteDataSource, SessionLocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public Person getSession() {
        return localDataSource.getUser();
    }

    public Observable<Person> getSessionObserver() {
        return personPublishSubject;
    }

    public Observable<Resource<Person>> login(String username, String password) {
        return buildLoginNetworkBoundResource(username, password).getAsObservable();
    }

    public void logout() {
        Log.i(TAG, "stopSession: ");
        localDataSource.removeUser();
        personPublishSubject.onError(new Throwable("User logged out"));
        personPublishSubject = PublishSubject.create();
    }

    private NetworkBoundResource<Person, Person> buildLoginNetworkBoundResource(
            String username, String password) {
        return new NetworkBoundResource<Person, Person>() {

            @Override
            protected boolean shouldFetchRemote() {
                return true;
            }

            @Override
            protected void saveRemoteResult(@NonNull Person item) {
                localDataSource.setUser(item);
                personPublishSubject.onNext(item);
            }

            @NonNull
            @Override
            protected Flowable<Person> fetchLocal() {
                Person person = localDataSource.getUser();
                if (person == null) {
                    return Flowable.error(new Throwable("No user"));
                }
                return Flowable.just(person);
            }

            @NonNull
            @Override
            protected Observable<Resource<Person>> fetchRemote() {
                return remoteDataSource.login(username, password)
                        .flatMap(remoteResponse ->
                                remoteResponse
                                        .map(person -> Observable.just(Resource.success(person)))
                                        .orElseGet(() -> Observable.error(
                                                new Throwable("Error signing in user"))));
            }
        };
    }
}
