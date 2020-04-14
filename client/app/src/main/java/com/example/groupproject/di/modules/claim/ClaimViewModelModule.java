package com.example.groupproject.di.modules.claim;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.groupproject.di.ViewModelKey;
import com.example.groupproject.ui.factory.ViewModelFactory;
import com.example.groupproject.ui.viewModel.LocationViewModel;
import com.example.groupproject.ui.viewModel.PhotoViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ClaimViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(PhotoViewModel.class)
    protected abstract ViewModel photoViewModel(PhotoViewModel photoViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LocationViewModel.class)
    protected abstract ViewModel locationViewModel(LocationViewModel locationViewModel);
}
