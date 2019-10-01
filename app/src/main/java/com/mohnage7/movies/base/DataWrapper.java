package com.mohnage7.movies.base;

public class DataWrapper<T> {
    private T data;
    private BaseError baseError;

    public DataWrapper(T data, BaseError baseError) {
        this.data = data;
        this.baseError = baseError;
    }

    public T getData() {
        return data;
    }

    public BaseError getBaseError() {
        return baseError;
    }
}
