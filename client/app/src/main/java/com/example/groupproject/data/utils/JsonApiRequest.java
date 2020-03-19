package com.example.groupproject.data.utils;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.example.groupproject.data.Result;

import java.util.Map;

public class JsonApiRequest<T> {
    private final GsonRequest<T> request;
    private MutableLiveData<Result<T>> result;

    private JsonApiRequest(Class<T> responseClass, int method, String url, @Nullable Object requestBody, @Nullable Map<String, String> headers) {
        result = new MutableLiveData<>();

        Listener<T> listener = new Listener<T>() {
            @Override
            public void onResponse(T response) {
                result.setValue(new Result.Success<T>(response));
            }
        };

        ErrorListener errorListener = new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                result.setValue(new Result.Error(error));
            }
        };

        request = new GsonRequest<T>(responseClass, method, url, requestBody, headers, listener, errorListener);
    }

    public LiveData<Result<T>> enqueue(RequestQueue requestQueue) {
        requestQueue.add(request);
        return result;
    }

    public static <T> JsonApiRequest<T> get(Class<T> responseClass, String url, @Nullable Map<String, String> headers) {
        return new JsonApiRequest<T>(responseClass, Method.GET, url, null, headers);
    }

    public static <T> JsonApiRequest<T> post(Class<T> responseClass, String url, @Nullable Object body, @Nullable Map<String, String> headers) {
        return new JsonApiRequest<T>(responseClass, Method.POST, url, body, headers);
    }

    public static <T> JsonApiRequest<T> put(Class<T> responseClass, String url, @Nullable Object body, @Nullable Map<String, String> headers) {
        return new JsonApiRequest<T>(responseClass, Method.PUT, url, body, headers);
    }

}
