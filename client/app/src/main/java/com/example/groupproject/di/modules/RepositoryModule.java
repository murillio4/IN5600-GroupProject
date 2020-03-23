package com.example.groupproject.di.modules;

import android.app.Application;

import com.example.groupproject.data.repositories.PersonRepository;
import com.example.groupproject.data.sources.PersonRemoteDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    PersonRepository providePersonRepository(Application application, PersonRemoteDataSource personRemoteDataSource) {
        return new PersonRepository(application.getApplicationContext(), personRemoteDataSource);
    }
}
