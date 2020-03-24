package com.example.groupproject.data.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.groupproject.data.Constants;
import com.example.groupproject.data.NetworkBoundResource;
import com.example.groupproject.data.Resource;
import com.example.groupproject.data.network.model.Result;
import com.example.groupproject.data.sources.local.PersonLocalDataSource;
import com.example.groupproject.data.sources.remote.PersonRemoteDataSource;
import com.example.groupproject.data.model.Person;
import com.google.gson.Gson;

import java.util.Optional;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class PersonRepository {
    private PersonRemoteDataSource remoteDataSource;
    private PersonLocalDataSource localDataSource;


    public PersonRepository(PersonRemoteDataSource remoteDataSource, PersonLocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;

    }

    public boolean isLoggedIn() {
        return localDataSource.getUser() != null;
    }

    public void logout() {
        localDataSource.removeUser();
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

    public Person getUser() {
        return localDataSource.getUser();
    }
}
