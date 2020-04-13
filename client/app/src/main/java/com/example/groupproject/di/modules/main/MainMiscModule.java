package com.example.groupproject.di.modules.main;


import android.content.Context;

import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterBuilder.Permission;

import dagger.Module;
import dagger.Provides;

@Module
public class MainMiscModule {

    @Provides
    Permission provideFragmentManager(Context context) {
        return Dexter.withContext(context);
    }

    @Provides
    AutocompleteSessionToken provideAutocompleteSessionToken() {
        return AutocompleteSessionToken.newInstance();
    }
}
