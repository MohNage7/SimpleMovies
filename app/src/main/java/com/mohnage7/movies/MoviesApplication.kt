package com.mohnage7.movies

import android.app.Application
import com.mohnage7.movies.db.di.dataBaseModule
import com.mohnage7.movies.features.moviedetails.di.movieDetailsModule
import com.mohnage7.movies.features.movies.di.moviesModule
import com.mohnage7.movies.network.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class MoviesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoinInjection();
    }

    private fun startKoinInjection() {
        startKoin {
            androidContext(this@MoviesApplication)
            modules(listOf(networkModule, dataBaseModule, moviesModule, movieDetailsModule))
        }
    }
}
