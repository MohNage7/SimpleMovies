package com.mohnage7.movies.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.mohnage7.movies.MoviesApplication;
import com.mohnage7.movies.base.DataWrapper;
import com.mohnage7.movies.model.Movie;
import com.mohnage7.movies.repository.MoviesRepository;
import com.mohnage7.movies.utils.Constants;

import java.util.List;

import javax.inject.Inject;

public class MoviesViewModel extends ViewModel {

    @Inject
    MoviesRepository repository;

    public MoviesViewModel() {
        MoviesApplication.getInstance().getDataComponent().inject(this);
    }

    public LiveData<DataWrapper<List<Movie>>> getMoviesList(@Constants.FilterBy String filter) {
        return repository.getArticles(filter);
    }
}
