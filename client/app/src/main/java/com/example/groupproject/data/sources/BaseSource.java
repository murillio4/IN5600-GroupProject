package com.example.groupproject.data.sources;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.groupproject.data.network.request.VolleyRequest;

abstract class BaseSource {
    final VolleyRequest volleyRequest;

    BaseSource(VolleyRequest volleyRequest) {
        this.volleyRequest = volleyRequest;
    }
}
