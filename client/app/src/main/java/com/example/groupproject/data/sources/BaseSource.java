package com.example.groupproject.data.sources;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public abstract class BaseSource {

    protected final RequestQueue requestQueue;

    public BaseSource(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }
}
