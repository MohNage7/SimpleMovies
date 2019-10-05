package com.mohnage7.movies.view.callback;

import android.view.View;

import com.mohnage7.movies.model.Movie;

public interface OnMovieClickListener {
    void onMovieClick(Movie movie, View view);
}
