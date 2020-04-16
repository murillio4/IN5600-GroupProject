package com.example.groupproject.di.modules;

import android.app.Application;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.example.groupproject.data.Constants;
import com.example.groupproject.data.network.request.VolleyRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NetworkModule {
    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    Cache provideCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MB
        return new DiskBasedCache(application.getCacheDir(), cacheSize);
    }

    @Provides
    @Singleton
    Network provideNetwork() {
        return new BasicNetwork(new HurlStack());
    }

    @Provides
    @Singleton
    RequestQueue provideRequestQueue(Cache cache, Network network) {
        RequestQueue requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
        return requestQueue;
    }

    @Provides
    @Singleton
    VolleyRequest provideVolleyRequest(Gson gson, RequestQueue requestQueue) {
        return new VolleyRequest.Builder()
                .setBaseUrl(Constants.Api.Base)
                .setGson(gson)
                .setRequestQueue(requestQueue)
                .build();

    }
}
