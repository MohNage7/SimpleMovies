package com.mohnage7.movies.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

import com.mohnage7.movies.features.moviedetails.model.MovieVideos

import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface VideosDao {
    @Insert(onConflict = REPLACE)
    fun insertAll(movieVideos: MovieVideos)

    @Query("SELECT * FROM movie_videos WHERE id = :movieId")
    fun getAllVideos(movieId: Int): LiveData<MovieVideos>
}
