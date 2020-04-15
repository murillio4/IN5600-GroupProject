package com.example.groupproject.ui.result;

import androidx.annotation.Nullable;

public class Result<T> {
    @Nullable
    private T success;
    @Nullable
    private Integer error;

    private Result(@Nullable T success, @Nullable Integer error) {
        this.success = success;
        this.error = error;
    }

    public static <T> Result<T> success(@Nullable T success) {
        return new Result<>(success, null);
    }

    public static <T> Result<T> error(@Nullable Integer error) {
        return new Result<>(null, error);
    }

    @Nullable
    public T getSuccess() {
        return success;
    }

    @Nullable
    public Integer getError() {
        return error;
    }
}
