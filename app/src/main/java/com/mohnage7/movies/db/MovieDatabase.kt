package com.mohnage7.movies.db

import android.content.Context

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.mohnage7.movies.features.movies.model.Movie
import com.mohnage7.movies.features.moviedetails.model.MovieVideos

@Database(entities = [Movie::class, MovieVideos::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    abstract val movieDao: MovieDao
    abstract val videosDao: VideosDao

    companion object {

        private val DATA_BASE_NAME = "movies_db"
        private var INSTANCE: MovieDatabase? = null

        @Synchronized
        fun getDatabaseInstance(context: Context): MovieDatabase {
            // insure that no other reference is created on different threads.
            if (INSTANCE == null) {
                // create our one and only db object.
                INSTANCE = Room.databaseBuilder(context.applicationContext, MovieDatabase::class.java, DATA_BASE_NAME).fallbackToDestructiveMigration().build()
            }
            return INSTANCE
        }
    }
}
