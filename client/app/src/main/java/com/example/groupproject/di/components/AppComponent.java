package com.example.groupproject.di.components;

import android.app.Application;

import com.example.groupproject.AppController;
import com.example.groupproject.di.modules.ActivityBuilderModule;
import com.example.groupproject.di.modules.MiscModule;
import com.example.groupproject.di.modules.NetworkModule;
import com.example.groupproject.di.modules.RepositoryModule;
import com.example.groupproject.di.modules.SourceModule;
import com.example.groupproject.di.modules.ViewModelModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(
    modules = {
        MiscModule.class,
        NetworkModule.class,
        SourceModule.class,
        RepositoryModule.class,
        ViewModelModule.class,
        ActivityBuilderModule.class,
        AndroidSupportInjectionModule.class
    }
)
public interface AppComponent extends AndroidInjector<AppController> {
    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}