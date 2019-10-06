package com.mohnage7.movies.api;


import androidx.lifecycle.LiveData;

import com.mohnage7.movies.model.MovieVideos;
import com.mohnage7.movies.model.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApiService {

    @GET("movie/{category}")
    LiveData<ApiResponse<MoviesResponse>> getMovies(@Path("category") String category);


    @GET("movie/{id}/videos")
    LiveData<ApiResponse<MovieVideos>> getVideos(@Path("id") int id);

    @GET("search/movie")
    Call<MoviesResponse> search(@Query("query") String query);
}
