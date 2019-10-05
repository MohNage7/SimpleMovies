package com.mohnage7.movies.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mohnage7.movies.api.ApiResponse;
import com.mohnage7.movies.api.RestApiService;
import com.mohnage7.movies.base.DataWrapper;
import com.mohnage7.movies.db.AppExecutors;
import com.mohnage7.movies.db.MovieDao;
import com.mohnage7.movies.db.MovieDatabase;
import com.mohnage7.movies.model.Movie;
import com.mohnage7.movies.model.MoviesResponse;
import com.mohnage7.movies.utils.RefreshRateLimiter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mohnage7.movies.utils.Constants.CACHE_TIMEOUT;

public class MoviesRepository {

    private final AppExecutors appExecutors;
    private RestApiService apiService;
    private MutableLiveData<DataWrapper<List<Movie>>> mutableSearchMovieLiveData = new MutableLiveData<>();
    private MovieDao movieDao;
    private RefreshRateLimiter refreshRateLimiter;

    @Inject
    public MoviesRepository(RestApiService apiService, MovieDatabase movieDatabase, AppExecutors appExecutors) {
        this.apiService = apiService;
        this.appExecutors = appExecutors;
        movieDao = movieDatabase.getMovieDao();
        refreshRateLimiter = new RefreshRateLimiter(TimeUnit.MINUTES, CACHE_TIMEOUT);
    }

    public LiveData<DataWrapper<List<Movie>>> getMovies(String filter) {
        return new NetworkBoundResource<List<Movie>, MoviesResponse>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull MoviesResponse item) {
                addFilterToEveryMovie(item.getMovieList(), filter);
                movieDao.insertAll(item.getMovieList());
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Movie> data) {
                return data == null || data.isEmpty() || refreshRateLimiter.shouldFetch(filter);
            }

            @NonNull
            @Override
            protected LiveData<List<Movie>> loadFromDb() {
                return movieDao.getAllMovies(filter);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<MoviesResponse>> createCall() {
                return apiService.getMovies(filter);
            }
        }.getAsLiveData();
    }

    private void addFilterToEveryMovie(List<Movie> movieList, String filter) {
        for (Movie movie : movieList) {
            movie.setFilter(filter);
        }
    }


    public LiveData<DataWrapper<List<Movie>>> search(String query) {
        mutableSearchMovieLiveData.setValue(new DataWrapper<>(DataWrapper.Status.LOADING, null, null));
        Call<MoviesResponse> call = apiService.search(query);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                MoviesResponse mResponse = response.body();
                if (mResponse != null) {
                    if (mResponse.getMovieList() != null && !mResponse.getMovieList().isEmpty()) {
                        mutableSearchMovieLiveData.setValue(new DataWrapper<>(DataWrapper.Status.SUCCESS, mResponse.getMovieList(), null));
                    } else {
                        mutableSearchMovieLiveData.setValue(new DataWrapper<>(DataWrapper.Status.ERROR, null, mResponse.getStatusMessage()));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable error) {
                mutableSearchMovieLiveData.setValue(new DataWrapper<>(DataWrapper.Status.ERROR, null, error.getMessage().equals("") ? error.getMessage() : "Unknown error\nCheck network connection"));
            }
        });
        return mutableSearchMovieLiveData;
    }
}
