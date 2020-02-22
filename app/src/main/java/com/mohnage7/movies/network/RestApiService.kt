package com.mohnage7.movies.network


import androidx.lifecycle.LiveData

import com.mohnage7.movies.features.moviedetails.model.MovieVideos
import com.mohnage7.movies.features.movies.model.MoviesResponse
import com.mohnage7.movies.network.model.ApiResponse

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RestApiService {

    @GET("movie/{category}")
    fun getMovies(@Path("category") category: String): LiveData<ApiResponse<MoviesResponse>>

    @GET("movie/{id}/videos")
    fun getVideos(@Path("id") id: Int): LiveData<ApiResponse<MovieVideos>>

    @GET("search/movie")
    fun search(@Query("query") query: String): Call<MoviesResponse>
}
