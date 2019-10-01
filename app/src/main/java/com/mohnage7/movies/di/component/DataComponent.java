package com.mohnage7.movies.di.component;


import com.mohnage7.movies.MoviesApplication;
import com.mohnage7.movies.di.module.DataModule;
import com.mohnage7.movies.viewmodel.MoviesViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {DataModule.class})
public interface DataComponent {


    void inject(MoviesViewModel moviesViewModel);

    void inject(MoviesApplication moviesApplication);
}
