package com.example.groupproject.ui.result;

import androidx.annotation.Nullable;

import com.example.groupproject.data.Status;

public class Result<T> {
    @Nullable
    Status status;

    @Nullable
    T data;

    public Result(@Nullable Status status) {
        this.status = status;
    }

    public Result(@Nullable Status status, @Nullable T data) {
        this(status);
        this.data = data;
    }

    public Status getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }
}
