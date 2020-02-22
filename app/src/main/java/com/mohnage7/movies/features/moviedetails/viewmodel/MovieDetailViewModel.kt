package com.mohnage7.movies.features.moviedetails.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mohnage7.movies.features.moviedetails.model.MovieVideos
import com.mohnage7.movies.features.moviedetails.repository.MovieDetailsRepository
import com.mohnage7.movies.network.model.DataWrapper

class MovieDetailViewModel(private val repository: MovieDetailsRepository) : ViewModel() {

    fun getVideosList(movieId: Int): LiveData<DataWrapper<MovieVideos>> {
        return repository.getVideos(movieId)
    }
}
