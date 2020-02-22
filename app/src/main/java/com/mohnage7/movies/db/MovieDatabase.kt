package com.mohnage7.movies.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mohnage7.movies.db.converters.VideoConverter
import com.mohnage7.movies.db.dao.MovieDao
import com.mohnage7.movies.db.dao.VideosDao
import com.mohnage7.movies.features.moviedetails.model.MovieVideos
import com.mohnage7.movies.features.movies.model.Movie

const val DATA_BASE_NAME = "movies_db"
const val CACHE_TIMEOUT = 1 // 1 minute

@Database(entities = [Movie::class, MovieVideos::class], version = 1, exportSchema = false)
@TypeConverters(VideoConverter::class)
abstract class MovieDatabase : RoomDatabase() {

    abstract val movieDao: MovieDao
    abstract val videosDao: VideosDao
}
