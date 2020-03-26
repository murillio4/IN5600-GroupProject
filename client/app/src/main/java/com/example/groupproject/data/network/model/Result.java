package com.example.groupproject.data.network.model;

import androidx.annotation.Nullable;

public class Result<T> {
    public enum Status {
        SUCCESS,
        ERROR
    }
    @Nullable
    private final Status status;
    @Nullable
    private final T data;
    @Nullable
    private final Exception error;

    private Result(Status status, T data, Exception error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public Exception getError() {
        return error;
    }

    public Status getStatus() {
        return status;
    }

    public static <T> Result<T> error(Exception error) {
        return new Result(Status.ERROR, null, error);
    }

    public static <T> Result<T> success(T data) {
        return new Result<T>(Status.SUCCESS, data, null);
    }
}
