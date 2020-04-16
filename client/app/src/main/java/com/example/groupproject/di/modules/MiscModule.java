package com.example.groupproject.di.modules;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MiscModule {

    @Provides
    @Singleton
    Context providesContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    LayoutInflater providesLayoutInflater(Context context) {
        return LayoutInflater.from(context);
    }
}
