package com.mohnage7.movies.features.movies.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mohnage7.movies.db.AppExecutors
import com.mohnage7.movies.db.CACHE_TIMEOUT
import com.mohnage7.movies.db.RefreshRateLimiter
import com.mohnage7.movies.db.dao.MovieDao
import com.mohnage7.movies.features.movies.model.Movie
import com.mohnage7.movies.features.movies.model.MoviesResponse
import com.mohnage7.movies.network.NetworkBoundResource
import com.mohnage7.movies.network.RestApiService
import com.mohnage7.movies.network.model.ApiResponse
import com.mohnage7.movies.network.model.DataWrapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class MoviesRepository
constructor(private val apiService: RestApiService,
            private val appExecutors: AppExecutors,
            private val movieDao: MovieDao) {

    private val mutableSearchMovieLiveData = MutableLiveData<DataWrapper<List<Movie>>>()
    private val refreshRateLimiter: RefreshRateLimiter<String> = RefreshRateLimiter(TimeUnit.MINUTES, CACHE_TIMEOUT.toLong())

    fun getMovies(category: String): LiveData<DataWrapper<List<Movie>>> {
        return object : NetworkBoundResource<List<Movie>, MoviesResponse>(appExecutors) {

            override fun saveCallResult(item: MoviesResponse) {
                addFilterToEveryMovie(item.movieList!!, category)
                movieDao.insertAll(item.movieList!!)
            }

            override fun shouldFetch(data: List<Movie>?): Boolean {
                return data == null || data.isEmpty() || refreshRateLimiter.shouldFetch(category)
            }

            override fun loadFromDb(): LiveData<List<Movie>> {
                return movieDao.getAllMovies(category)
            }

            override fun createCall(): LiveData<ApiResponse<MoviesResponse>> {
                return apiService.getMovies(category)
            }
        }.asLiveData
    }

    private fun addFilterToEveryMovie(movieList: List<Movie>, category: String) {
        for (movie in movieList) {
            movie.category = category
        }
    }


    fun search(query: String): LiveData<DataWrapper<List<Movie>>> {
        mutableSearchMovieLiveData.value = DataWrapper(DataWrapper.Status.LOADING, null, null)
        val call = apiService.search(query)
        call.enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
                response.body()?.let { mResponse ->
                    if (mResponse.movieList != null && !mResponse.movieList!!.isEmpty()) {
                        mutableSearchMovieLiveData.setValue(
                                DataWrapper(
                                        DataWrapper.Status.SUCCESS,
                                        mResponse.movieList,
                                        null))
                    } else {
                        mutableSearchMovieLiveData.setValue(
                                DataWrapper(
                                        DataWrapper.Status.ERROR,
                                        null, ""))
                    }
                }

            }

            override fun onFailure(call: Call<MoviesResponse>, error: Throwable) {
                mutableSearchMovieLiveData.value = DataWrapper(DataWrapper.Status.ERROR, null, if (error.message == "") error.message else "Unknown error\nCheck network connection")
            }
        })
        return mutableSearchMovieLiveData
    }
}
