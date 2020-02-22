package com.mohnage7.movies.features.movies.view.callback

import android.view.View

import com.mohnage7.movies.features.movies.model.Movie

interface OnMovieClickListener {
    fun onMovieClick(movie: Movie, view: View)
}
