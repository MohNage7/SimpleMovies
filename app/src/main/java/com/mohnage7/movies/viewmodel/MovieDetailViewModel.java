package com.mohnage7.movies.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.mohnage7.movies.MoviesApplication;
import com.mohnage7.movies.base.DataWrapper;
import com.mohnage7.movies.model.MovieVideos;
import com.mohnage7.movies.repository.MovieDetailsRepository;

import javax.inject.Inject;

public class MovieDetailViewModel extends ViewModel {

    @Inject
    MovieDetailsRepository repository;

    public MovieDetailViewModel() {
        MoviesApplication.getInstance().getDataComponent().inject(this);
    }


    public LiveData<DataWrapper<MovieVideos>> getVideosList(int movieId) {
        return repository.getVideos(movieId);
    }

}
