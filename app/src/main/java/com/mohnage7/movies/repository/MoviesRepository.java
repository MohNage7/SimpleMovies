package com.mohnage7.movies.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mohnage7.movies.base.BaseError;
import com.mohnage7.movies.base.BaseRepository;
import com.mohnage7.movies.base.DataWrapper;
import com.mohnage7.movies.model.Movie;
import com.mohnage7.movies.model.MoviesResponse;
import com.mohnage7.movies.model.VideosResponse;
import com.mohnage7.movies.model.Video;
import com.mohnage7.movies.service.RestApiService;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesRepository extends BaseRepository {

    private RestApiService apiService;
    private MutableLiveData<DataWrapper<List<Movie>>> mutableMovieLiveData = new MutableLiveData<>();
    private MutableLiveData<DataWrapper<List<Movie>>> mutableSearchMovieLiveData = new MutableLiveData<>();
    private MutableLiveData<DataWrapper<List<Video>>> mutableVideosLiveData = new MutableLiveData<>();
    private BaseError mBaseError;

    @Inject
    public MoviesRepository(RestApiService apiService) {
        this.apiService = apiService;
    }

    public MutableLiveData<DataWrapper<List<Movie>>> getMovies(String filter) {
        Call<MoviesResponse> call = apiService.getPopularMovies(filter);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                MoviesResponse mResponse = response.body();
                if (mResponse != null && mResponse.getMovieList() != null) {
                    mutableMovieLiveData.setValue(new DataWrapper<>(mResponse.getMovieList(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                mBaseError = getError(t);
                mutableMovieLiveData.setValue(new DataWrapper<>(null, mBaseError));
            }
        });
        return mutableMovieLiveData;
    }

    public MutableLiveData<DataWrapper<List<Video>>> getVideos(int movieId) {
        Call<VideosResponse> call = apiService.getVideos(movieId);
        call.enqueue(new Callback<VideosResponse>() {
            @Override
            public void onResponse(@NonNull Call<VideosResponse> call, @NonNull Response<VideosResponse> response) {
                VideosResponse mResponse = response.body();
                if (mResponse != null && mResponse.getVideos() != null) {
                    mutableVideosLiveData.setValue(new DataWrapper<>(mResponse.getVideos(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<VideosResponse> call, @NonNull Throwable t) {
                mBaseError = getError(t);
                mutableVideosLiveData.setValue(new DataWrapper<>(null, mBaseError));
            }
        });
        return mutableVideosLiveData;
    }

    public LiveData<DataWrapper<List<Movie>>> search(String query) {
        Call<MoviesResponse> call = apiService.search(query);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                MoviesResponse mResponse = response.body();
                if (mResponse != null && mResponse.getMovieList() != null) {
                    mutableSearchMovieLiveData.setValue(new DataWrapper<>(mResponse.getMovieList(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                mBaseError = getError(t);
                mutableSearchMovieLiveData.setValue(new DataWrapper<>(null, mBaseError));
            }
        });
        return mutableSearchMovieLiveData;
    }
}
