package com.mohnage7.movies.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.mohnage7.movies.MoviesApplication;
import com.mohnage7.movies.base.DataWrapper;
import com.mohnage7.movies.model.Movie;
import com.mohnage7.movies.repository.MoviesRepository;
import com.mohnage7.movies.utils.Constants;

import java.util.List;

import javax.inject.Inject;

import static com.mohnage7.movies.utils.Constants.POPULAR;

public class MoviesViewModel extends ViewModel {

    @Inject
    MoviesRepository repository;

    private MutableLiveData<String> caregory = new MutableLiveData<>();
    private MutableLiveData<String> searchBy = new MutableLiveData<>();
    private LiveData<DataWrapper<List<Movie>>> moviesList;
    private LiveData<DataWrapper<List<Movie>>> searchMoviesList;

    public MoviesViewModel() {
        MoviesApplication.getInstance().getDataComponent().inject(this);
        caregory.setValue(POPULAR);
        moviesList = Transformations.switchMap(caregory, category -> repository.getMovies(category));
        searchMoviesList = Transformations.switchMap(searchBy, query -> repository.search(query));
    }

    public LiveData<DataWrapper<List<Movie>>> getMoviesList() {
        return moviesList;
    }

    public LiveData<DataWrapper<List<Movie>>> search() {
        return searchMoviesList;
    }


    public void setFilterMovieBy(@Constants.FilterBy String filter) {
        caregory.setValue(filter);
    }

    public void setSearchBy(String query) {
        searchBy.setValue(query);
    }
}
