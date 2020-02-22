package com.mohnage7.movies.features.movies.di

import com.mohnage7.movies.features.movies.repository.MoviesRepository
import com.mohnage7.movies.features.movies.viewmodel.MoviesViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val moviesModule = module {
    factory { MoviesRepository(get(), get(), get()) }
    viewModel { MoviesViewModel(get()) }
}