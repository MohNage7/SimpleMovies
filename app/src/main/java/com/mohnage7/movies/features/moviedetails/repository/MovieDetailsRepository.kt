package com.mohnage7.movies.features.moviedetails.repository

import androidx.lifecycle.LiveData
import com.mohnage7.movies.db.AppExecutors
import com.mohnage7.movies.db.CACHE_TIMEOUT
import com.mohnage7.movies.db.dao.VideosDao
import com.mohnage7.movies.features.moviedetails.model.MovieVideos
import com.mohnage7.movies.network.NetworkBoundResource
import com.mohnage7.movies.network.RestApiService
import com.mohnage7.movies.network.model.ApiResponse
import com.mohnage7.movies.network.model.DataWrapper


class MovieDetailsRepository
constructor(private val apiService: RestApiService, private val appExecutors: AppExecutors, private val videosDao: VideosDao) {


    fun getVideos(movieId: Int): LiveData<DataWrapper<MovieVideos>> {
        return object : NetworkBoundResource<MovieVideos, MovieVideos>(appExecutors) {

            override fun saveCallResult(item: MovieVideos) {
                item.timestamp = getCurrentTime()
                videosDao.insertAll(item)
            }

            override fun shouldFetch(data: MovieVideos?): Boolean {
                if (data == null)
                    return true
                else {
                    val currentTime = getCurrentTime()
                    val isCacheExpired = currentTime - data.timestamp >= CACHE_TIMEOUT * 60
                    return (data.videos == null || data.videos!!.isEmpty() || isCacheExpired)
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

    private fun getCurrentTime(): Int {
        return (System.currentTimeMillis() / 1000).toInt()
    }
}
