package com.example.groupproject.data.sources;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public abstract class BaseSource {
    private static RequestQueue requestQueue;

    public BaseSource(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
            requestQueue.start();
        }
    }

    protected RequestQueue getRequestQueue() {
        return requestQueue;
    }


}
