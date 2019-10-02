package com.mohnage7.movies.base;


import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class BaseRepository {
    public BaseError getError(Throwable throwable) {
        BaseError baseError = new BaseError();
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            ResponseBody body = httpException.response().errorBody();
            Gson gson = new Gson();
            TypeAdapter<ErrorBody> adapter = gson.getAdapter(ErrorBody.class);
            try {
                if (body != null) {
                    ErrorBody errorBody = adapter.fromJson(body.string());
                    baseError.setErrorMessage(errorBody.getMessage());
                    baseError.setErrorCode(httpException.code());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            baseError.setErrorMessage(throwable.getMessage());
            baseError.setErrorCode(0);
        }

        return baseError;
    }
}
