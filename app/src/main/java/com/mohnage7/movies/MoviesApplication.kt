package com.mohnage7.movies

import android.app.Application

import com.mohnage7.movies.di.component.DaggerDataComponent
import com.mohnage7.movies.di.component.DataComponent
import com.mohnage7.movies.di.module.DataModule

class MoviesApplication : Application() {
    var dataComponent: DataComponent
        internal set

    override fun onCreate() {
        super.onCreate()
        instance = this
        initDataComponent()
    }

    private fun initDataComponent() {
        dataComponent = DaggerDataComponent.builder().dataModule(DataModule(this))
                .build()
        dataComponent.inject(this)
    }

    companion object {

        var instance: MoviesApplication? = null
            private set
    }
}
