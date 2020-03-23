package com.example.groupproject.di.components;

import android.app.Application;

import com.example.groupproject.AppController;
import com.example.groupproject.di.modules.ActivityBuilderModule;
import com.example.groupproject.di.modules.NetworkModule;
import com.example.groupproject.di.modules.RepositoryModule;
import com.example.groupproject.di.modules.SourceModule;
import com.example.groupproject.di.modules.ViewModelModule;
import com.example.groupproject.ui.factory.ViewModelFactory;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

//        (modules = {
//        ApiModule.class,
//        DbModule.class,
//        ViewModelModule.class,
//        ActivityModule.class,
//        AndroidSupportInjectionModule.class})

@Component(modules = {
        NetworkModule.class,
        SourceModule.class,
        RepositoryModule.class,
        ViewModelModule.class,
        ActivityBuilderModule.class,
        AndroidSupportInjectionModule.class})
@Singleton
public interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(AppController appController);
}