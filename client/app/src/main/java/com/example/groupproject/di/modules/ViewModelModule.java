package com.example.groupproject.di.modules;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.groupproject.di.ViewModelKey;
import com.example.groupproject.ui.factory.ViewModelFactory;
import com.example.groupproject.ui.viewModel.LoginViewModel;
import com.example.groupproject.ui.viewModel.SessionViewModel;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

//inspired by https://github.com/anitaa1990/Dagger2-Sample

@Module
public abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);


    @Binds
    @IntoMap
    @ViewModelKey(SessionViewModel.class)
    protected abstract ViewModel sessionViewModel(SessionViewModel sessionViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    protected abstract ViewModel loginViewModel(LoginViewModel loginViewModel);
}
