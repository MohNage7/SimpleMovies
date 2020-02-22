package com.mohnage7.movies.features.moviedetails.repository

import android.content.Context
import androidx.lifecycle.LiveData

import com.mohnage7.movies.network.ApiResponse
import com.mohnage7.movies.network.RestApiService
import com.mohnage7.movies.base.DataWrapper
import com.mohnage7.movies.db.AppExecutors
import com.mohnage7.movies.db.MovieDatabase
import com.mohnage7.movies.db.VideosDao
import com.mohnage7.movies.features.moviedetails.model.MovieVideos
import com.mohnage7.movies.network.NetworkBoundResource

import javax.inject.Inject

import com.mohnage7.movies.utils.Constants.CACHE_TIMEOUT


class MovieDetailsRepository @Inject
constructor(private val apiService: RestApiService, private val appExecutors: AppExecutors, context: Context) {
    private val videosDao: VideosDao

    init {
        videosDao = MovieDatabase.getDatabaseInstance(context).videosDao
    }

    fun getVideos(movieId: Int): LiveData<DataWrapper<MovieVideos>> {
        return object : NetworkBoundResource<MovieVideos, MovieVideos>(appExecutors) {

            override fun saveCallResult(item: MovieVideos) {
                item.timestamp = (System.currentTimeMillis() / 1000).toInt()
                videosDao.insertAll(item)
            }

            override fun shouldFetch(data: MovieVideos?): Boolean {
                if (data == null)
                    return true
                else {
                    val currentTime = (System.currentTimeMillis() / 1000).toInt()
                    val isCacheExpired = currentTime - data.timestamp >= CACHE_TIMEOUT * 60
                    return (data.videos == null
                            || data.videos!!.isEmpty() || isCacheExpired)
                }
            }

            override fun loadFromDb(): LiveData<MovieVideos> {
                return videosDao.getAllVideos(movieId)
            }

            override fun createCall(): LiveData<ApiResponse<MovieVideos>> {
                return apiService.getVideos(movieId)
            }
        }.asLiveData
    }
}
