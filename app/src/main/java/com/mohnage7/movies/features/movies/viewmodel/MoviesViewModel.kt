package com.mohnage7.movies.features.movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mohnage7.movies.features.categoryfilter.view.CategoryBottomSheet
import com.mohnage7.movies.features.categoryfilter.view.POPULAR
import com.mohnage7.movies.features.movies.model.Movie
import com.mohnage7.movies.features.movies.repository.MoviesRepository
import com.mohnage7.movies.network.model.DataWrapper


class MoviesViewModel(private val repository: MoviesRepository) : ViewModel() {

    private val category = MutableLiveData<String>()
    private val searchBy = MutableLiveData<String>()
    val moviesList: LiveData<DataWrapper<List<Movie>>>
    private val searchMoviesList: LiveData<DataWrapper<List<Movie>>>

    init {
        category.value = POPULAR
        moviesList = Transformations.switchMap(category) { category -> repository.getMovies(category) }
        searchMoviesList = Transformations.switchMap(searchBy) { query -> repository.search(query) }
    }

    fun search(): LiveData<DataWrapper<List<Movie>>> {
        return searchMoviesList
    }


    fun setFilterMovieBy(@CategoryBottomSheet.FilterBy filter: String) {
        category.value = filter
    }

    fun setSearchBy(query: String) {
        searchBy.setValue(query)
    }
}
