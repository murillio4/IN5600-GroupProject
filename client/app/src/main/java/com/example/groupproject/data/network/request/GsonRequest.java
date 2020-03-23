package com.example.groupproject.data.network.request;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.example.groupproject.data.utils.GsonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;


public class GsonRequest<T> extends BaseRequest<T> {
    private static final String PROTOCOL_CHARSET = "utf-8";
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    private final Gson gson;
    private final Class<T> responseClass;

    private GsonRequest(int method, String url, Map<String, String> headers, Map<String, Object> bodyParams, @Nullable String body, Class<T> responseClass, Gson gson, RequestQueue requestQueue) {
        super(method, url, headers, bodyParams, body, requestQueue);
        this.gson = gson;
        this.responseClass = responseClass;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return this.createSuccessResponse(response, gson.fromJson(json, responseClass));
        } catch (UnsupportedEncodingException | JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    public static class Builder<T> extends BaseRequest.Builder<T> {
        private Class<T> responseClass;
        private Gson gson;

        Builder(int method, String url, Class<T> responseClass, Gson gson) {
            super(method, url);
            this.responseClass = responseClass;
            this.gson = gson;
        }

        @Override
        public Builder<T> setRequestQueue(RequestQueue requestQueue) {
            super.setRequestQueue(requestQueue);
            return this;
        }

        @Override
        public Builder<T> addHeader(String key, String value) {
            super.addHeader(key, value);
            return this;
        }

        @Override
        public Builder<T> addBodyParam(String key, Object value) {
            super.addBodyParam(key, value);
            return this;
        }

        @Override
        public Builder<T> addQueryParam(String key, String value) {
            super.addQueryParam(key, value);
            return this;
        }

        @Override
        public Builder<T> setBody(String body) {
            super.setBody(body);
            return this;
        }

        public Builder<T> setBody(Object requestBody) {
            super.setBody(gson.toJson(requestBody));
            return this;
        }

        public GsonRequest<T> build() {
            return new GsonRequest<T>(method, buildUrl(), headers, bodyParams, body, responseClass, gson, requestQueue);
        }

        public static <T> Builder<T> get(String url, Class<T> responseClass, Gson gson) {
            return new Builder<T>(Method.GET, url, responseClass, gson);
        }

        public static <T> Builder<T> post(String url, Class<T> responseClass, Gson gson) {
            return new Builder<T>(Method.POST, url, responseClass, gson);
        }

        public static <T> Builder<T> put(String url, Class<T> responseClass, Gson gson) {
            return new Builder<T>(Method.PUT, url, responseClass, gson);
        }
    }
}