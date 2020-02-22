package com.mohnage7.movies.features.movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

import com.mohnage7.movies.MoviesApplication
import com.mohnage7.movies.base.DataWrapper
import com.mohnage7.movies.features.categoryfilter.view.CategoryBottomSheet
import com.mohnage7.movies.features.movies.model.Movie
import com.mohnage7.movies.features.movies.repository.MoviesRepository

import javax.inject.Inject

import com.mohnage7.movies.features.categoryfilter.view.CategoryBottomSheet.POPULAR


class MoviesViewModel : ViewModel() {

    @Inject
    internal var repository: MoviesRepository? = null

    private val category = MutableLiveData<String>()
    private val searchBy = MutableLiveData<String>()
    val moviesList: LiveData<DataWrapper<List<Movie>>>
    private val searchMoviesList: LiveData<DataWrapper<List<Movie>>>

    init {
        MoviesApplication.getInstance().dataComponent.inject(this)
        category.setValue(Companion.getPOPULAR())
        moviesList = Transformations.switchMap(category) { category -> repository!!.getMovies(category) }
        searchMoviesList = Transformations.switchMap(searchBy) { query -> repository!!.search(query) }
    }

    fun search(): LiveData<DataWrapper<List<Movie>>> {
        return searchMoviesList
    }


    fun setFilterMovieBy(@CategoryBottomSheet.FilterBy filter: String) {
        category.setValue(filter)
    }

    fun setSearchBy(query: String) {
        searchBy.setValue(query)
    }
}
