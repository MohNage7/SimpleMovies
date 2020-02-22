package com.mohnage7.movies.features.moviedetails.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

import com.mohnage7.movies.MoviesApplication
import com.mohnage7.movies.base.DataWrapper
import com.mohnage7.movies.features.moviedetails.model.MovieVideos
import com.mohnage7.movies.features.moviedetails.repository.MovieDetailsRepository

import javax.inject.Inject

class MovieDetailViewModel : ViewModel() {

    @Inject
    internal var repository: MovieDetailsRepository? = null

    init {
        MoviesApplication.getInstance().dataComponent.inject(this)
    }


    fun getVideosList(movieId: Int): LiveData<DataWrapper<MovieVideos>> {
        return repository!!.getVideos(movieId)
    }

}
