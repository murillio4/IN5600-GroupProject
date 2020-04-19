package com.example.groupproject.data;

import androidx.annotation.Nullable;

public class State<T> {

    @Nullable
    private T data;

    private boolean isDataValid;

    public State(@Nullable T data) {
        this.data = data;
        this.isDataValid = false;
    }

    public State(boolean isDataValid) {
        this.data = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    public T getData() {
        return data;
    }

    public boolean isDataValid() {
        return isDataValid;
    }

}
