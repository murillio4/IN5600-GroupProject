package com.example.groupproject.data.repositories;

import androidx.annotation.NonNull;

import com.example.groupproject.data.NetworkBoundResource;
import com.example.groupproject.data.Resource;
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
    private SessionRemoteDataSource remoteDataSource;
    private SessionLocalDataSource localDataSource;

    private PublishSubject<Person> personPublishSubject = PublishSubject.create();

    public SessionRepository(SessionRemoteDataSource remoteDataSource, SessionLocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public boolean isLoggedIn() {
        return localDataSource.getUser() != null;
    }

    public void logout() {
        localDataSource.removeUser();
        personPublishSubject.onError(new Throwable("User logged out"));
    }

    public Observable<Resource<Person>> login(String username, String password) {
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
                System.out.println(person.toString());
                return Flowable.just(person);
            }

            @NonNull
            @Override
            protected Observable<Resource<Person>> fetchRemote() {
                return remoteDataSource.login(username, password)
                        .flatMap(remoteResponse ->
                            remoteResponse
                                    .map(person -> Observable.just(Resource.success(person)))
                                    .orElseGet(() -> Observable.error(new Throwable("Error signing in user"))));
            }
        }.getAsObservable();
    }

    public Observable<Person> observeSession() {
        return personPublishSubject;
    }

    public Person getSession() {
        return localDataSource.getUser();
    }
}
