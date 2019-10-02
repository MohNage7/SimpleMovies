package com.mohnage7.movies.service;


import com.mohnage7.movies.model.MoviesResponse;
import com.mohnage7.movies.model.VideosResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApiService {

    @GET("movie/{filter_by}")
    Call<MoviesResponse> getPopularMovies(@Path("filter_by") String filterBy);


    @GET("movie/{id}/videos")
    Call<VideosResponse> getVideos(@Path("id") int id);

    @GET("search/movie")
    Call<MoviesResponse> search(@Query("query") String query);
}
