package com.mohnage7.movies.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.mohnage7.movies.api.ApiResponse;
import com.mohnage7.movies.api.RestApiService;
import com.mohnage7.movies.base.DataWrapper;
import com.mohnage7.movies.db.AppExecutors;
import com.mohnage7.movies.db.MovieDatabase;
import com.mohnage7.movies.db.VideosDao;
import com.mohnage7.movies.model.MovieVideos;

import javax.inject.Inject;

import static com.mohnage7.movies.utils.Constants.CACHE_TIMEOUT;

public class MovieDetailsRepository  {

    private final AppExecutors appExecutors;
    private RestApiService apiService;
    private VideosDao videosDao;

    @Inject
    public MovieDetailsRepository(RestApiService apiService, AppExecutors appExecutors, Context context) {
        this.apiService = apiService;
        this.appExecutors = appExecutors;
        videosDao = MovieDatabase.getDatabaseInstance(context).getVideosDao();
    }

    public LiveData<DataWrapper<MovieVideos>> getVideos(int movieId) {
        return new NetworkBoundResource<MovieVideos, MovieVideos>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull MovieVideos item) {
                item.setTimestamp((int) (System.currentTimeMillis() / 1000));
                videosDao.insertAll(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable MovieVideos data) {
                if (data == null) return true;
                else {
                    int currentTime = (int) (System.currentTimeMillis() / 1000);
                    boolean isCacheExpired = (currentTime - data.getTimestamp()) >= CACHE_TIMEOUT * 60;
                    return data.getVideos() == null
                            || data.getVideos().isEmpty() || isCacheExpired;
                }
            }

            @NonNull
            @Override
            protected LiveData<MovieVideos> loadFromDb() {
                return videosDao.getAllVideos(movieId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<MovieVideos>> createCall() {
                return apiService.getVideos(movieId);
            }
        }.getAsLiveData();
    }
}
