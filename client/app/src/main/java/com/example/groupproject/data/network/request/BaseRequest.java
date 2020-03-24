package com.example.groupproject.data.network.request;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.AsyncSubject;

public class BaseRequest<T> extends Request<T> {

    private final Map<String, String> headers;

    private final Map<String, Object> bodyParams;

    private String body;

    private AsyncSubject<Optional<T>> result = AsyncSubject.create();

    private RequestQueue requestQueue;

    protected BaseRequest(int method, String url, Map<String, String> headers, Map<String, Object> bodyParams, @Nullable String body, RequestQueue requestQueue) {
        super(method, url, null);
        this.headers = headers;
        this.bodyParams = bodyParams;
        this.body = body;
        this.requestQueue = requestQueue;
    }

    @Override
    protected com.android.volley.Response<T> parseNetworkResponse(NetworkResponse response) {
        return createSuccessResponse(response, null);
    }

    protected com.android.volley.Response<T> createSuccessResponse(NetworkResponse response, T result) {
        return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(@Nullable T response) {
        result.onNext(Optional.ofNullable(response));
        result.onComplete();
    }

    @Override
    public void deliverError(VolleyError error) {
        result.onError(error);
        result.onComplete();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }

    @Override
    public byte[] getBody() {
        if (body == null && !bodyParams.isEmpty()) {
            body = (new JSONObject(bodyParams)).toString();
        }

        return body != null ? body.getBytes() : null;
    }

    public Observable<Optional<T>> enqueue() {
        requestQueue.add(this);
        return result;
    }

    public static class Builder<T> {
        final Map<String, String> headers = new HashMap<>();

        final Map<String, String> queryParams = new HashMap<>();

        final Map<String, Object> bodyParams  = new HashMap<>();

        String body;

        String url;

        int method;

        RequestQueue requestQueue;

        Builder(int method, String url) {
            this.url = url;
            this.method = method;
        }

        String buildUrl() {
            String url = this.url;
            if(!queryParams.isEmpty()) {
                Uri.Builder uriBuilder = Uri.parse(url).buildUpon();
                queryParams.forEach(uriBuilder::appendQueryParameter);
                url = uriBuilder.build().toString();
            }

            return url;
        }

        public Builder<T> setRequestQueue(RequestQueue requestQueue) {
            this.requestQueue = requestQueue;
            return this;
        }

        public Builder<T> addHeader(String key, String value) {
            if (value != null) { headers.put(key, value); }
            return this;
        }

        public Builder<T> addBodyParam(String key, Object value) {
            if (value != null) { bodyParams.put(key, value); }
            return this;
        }

        public Builder<T> addQueryParam(String key, String value) {
            if (value != null) { queryParams.put(key, value); }
            return this;
        }

        public Builder<T> setBody(String body) {
            this.body = body;
            return this;
        }

        public BaseRequest<T> build() {
            return new BaseRequest<T>(method, buildUrl(), headers, bodyParams, body, requestQueue);
        }

        public static <T> Builder<T> get(String url) {
            return new Builder<T>(Method.GET, url);
        }

        public static <T> Builder<T> post(String url) {
            return new Builder<T>(Method.POST, url);
        }

        public static <T> Builder<T> put(String url) {
            return new Builder<T>(Method.PUT, url);
        }
    }
}
