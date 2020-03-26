package com.example.groupproject.data.network.request;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.google.gson.Gson;

public class VolleyRequest {
    private String baseUrl;
    private Gson gson;
    private RequestQueue requestQueue;

    VolleyRequest(String baseUrl, Gson gson, RequestQueue requestQueue) {
        this.baseUrl = baseUrl;
        this.gson = gson;
        this.requestQueue = requestQueue;
    }

    private String generateUrl(String url) {
        return this.baseUrl + url;
    }

    public <T> GsonRequest.Builder<T> get(String url, Class<T> responseClass) {
        return new GsonRequest.Builder<T>(Request.Method.GET, generateUrl(url), responseClass, gson).setRequestQueue(requestQueue);
    }

    public <T> GsonRequest.Builder<T> post(String url, Class<T> responseClass) {
        return new GsonRequest.Builder<T>(Request.Method.POST, generateUrl(url), responseClass, gson).setRequestQueue(requestQueue);
    }

    public <T> GsonRequest.Builder<T> put(String url, Class<T> responseClass) {
        return new GsonRequest.Builder<T>(Request.Method.PUT, generateUrl(url), responseClass, gson).setRequestQueue(requestQueue);
    }

    public static class Builder {
        private String baseUrl;
        private Gson gson;
        private RequestQueue requestQueue;

        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder setGson(Gson gson) {
            this.gson = gson;
            return this;
        }

        public Builder setRequestQueue(RequestQueue requestQueue) {
            this.requestQueue = requestQueue;
            return this;
        }

        public VolleyRequest build() {
            return new VolleyRequest(baseUrl, gson, requestQueue);
        }
    }
}
