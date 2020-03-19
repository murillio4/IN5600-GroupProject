package com.example.groupproject.data.sources;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public abstract class BaseSource {
    private RequestQueue requestQueue;

    public BaseSource(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    protected RequestQueue getRequestQueue() {
        return requestQueue;
    }


}
