package com.example.groupproject.di.modules;

import android.app.Application;

import com.example.groupproject.data.repositories.PersonRepository;
import com.example.groupproject.data.sources.local.PersonLocalDataSource;
import com.example.groupproject.data.sources.remote.PersonRemoteDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    PersonRepository providePersonRepository(PersonRemoteDataSource personRemoteDataSource, PersonLocalDataSource personLocalDataSource) {
        return new PersonRepository(personRemoteDataSource, personLocalDataSource);
    }
}
