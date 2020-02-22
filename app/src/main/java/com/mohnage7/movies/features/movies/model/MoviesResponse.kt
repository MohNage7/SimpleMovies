package com.mohnage7.movies.features.movies.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by mohnage7 on 3/1/2018.
 */

class MoviesResponse {
    @SerializedName("results")
    @Expose
    var movieList: List<Movie>? = null
}
