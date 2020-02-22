package com.mohnage7.movies.features.moviedetails.di

import com.mohnage7.movies.features.moviedetails.repository.MovieDetailsRepository
import com.mohnage7.movies.features.moviedetails.viewmodel.MovieDetailViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val movieDetailsModule = module {
    factory { MovieDetailsRepository(get(), get(), get()) }
    viewModel { MovieDetailViewModel(get()) }
}