package com.example.groupproject.di.modules;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.groupproject.di.ViewModelKey;
import com.example.groupproject.di.ViewModelFactory;
import com.example.groupproject.ui.viewModel.ClaimsViewModel;
import com.example.groupproject.ui.viewModel.FormViewModel;
import com.example.groupproject.ui.viewModel.LocationViewModel;
import com.example.groupproject.ui.viewModel.LoginViewModel;
import com.example.groupproject.ui.viewModel.PhotoViewModel;
import com.example.groupproject.ui.viewModel.SessionViewModel;

import dagger.Binds;
import dagger.Module;
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

    @Binds
    @IntoMap
    @ViewModelKey(ClaimsViewModel.class)
    protected abstract ViewModel claimsViewModel(ClaimsViewModel claimsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PhotoViewModel.class)
    protected abstract ViewModel photoViewModel(PhotoViewModel photoViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PhotoViewModel.class)
    protected abstract ViewModel locationViewModel(LocationViewModel locationViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FormViewModel.class)
    protected abstract ViewModel formViewModel(FormViewModel formViewModel);
}
