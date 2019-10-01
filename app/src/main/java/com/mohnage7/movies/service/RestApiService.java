package com.mohnage7.movies.service;


import com.mohnage7.movies.model.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestApiService {

    @GET("movie/{filter_by}")
    Call<MoviesResponse> getPopularMovies(@Path("filter_by") String filterBy);


}
