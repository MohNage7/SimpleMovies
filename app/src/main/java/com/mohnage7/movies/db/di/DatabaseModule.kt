package com.mohnage7.movies.db.di

import androidx.room.Room
import com.mohnage7.movies.db.AppExecutors
import com.mohnage7.movies.db.DATA_BASE_NAME
import com.mohnage7.movies.db.MovieDatabase
import org.koin.dsl.module

val dataBaseModule = module {
    single { Room.databaseBuilder(get(), MovieDatabase::class.java, DATA_BASE_NAME).build()}
    single { get<MovieDatabase>().movieDao }
    single { get<MovieDatabase>().videosDao }
    single { AppExecutors() }
}