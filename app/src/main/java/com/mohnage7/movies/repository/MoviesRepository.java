package com.mohnage7.movies.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.mohnage7.movies.base.BaseError;
import com.mohnage7.movies.base.DataWrapper;
import com.mohnage7.movies.base.ErrorBody;
import com.mohnage7.movies.model.Movie;
import com.mohnage7.movies.model.MoviesResponse;
import com.mohnage7.movies.service.RestApiService;
import com.mohnage7.movies.utils.Constants;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class MoviesRepository {

    private RestApiService apiService;
    private MutableLiveData<DataWrapper<List<Movie>>> mutableLiveData = new MutableLiveData<>();
    private BaseError mBaseError;

    @Inject
    public MoviesRepository(RestApiService apiService) {
        this.apiService = apiService;
    }

    public MutableLiveData<DataWrapper<List<Movie>>> getArticles(String filter) {
        Call<MoviesResponse> call = apiService.getPopularMovies(filter);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                MoviesResponse mResponse = response.body();
                if (mResponse != null && mResponse.getMovieList() != null) {
                    mutableLiveData.setValue(new DataWrapper<>(mResponse.getMovieList(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                mBaseError = getError(t);
                mutableLiveData.setValue(new DataWrapper<>(null, mBaseError));
            }
        });
        return mutableLiveData;
    }

    private BaseError getError(Throwable throwable) {
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
