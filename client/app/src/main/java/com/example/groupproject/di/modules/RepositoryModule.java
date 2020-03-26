package com.example.groupproject.di.modules;

import com.example.groupproject.data.repositories.SessionRepository;
import com.example.groupproject.data.sources.local.SessionLocalDataSource;
import com.example.groupproject.data.sources.remote.SessionRemoteDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    SessionRepository providePersonRepository(SessionRemoteDataSource sessionRemoteDataSource, SessionLocalDataSource sessionLocalDataSource) {
        return new SessionRepository(sessionRemoteDataSource, sessionLocalDataSource);
    }
}
