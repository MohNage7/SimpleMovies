package com.mohnage7.movies.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.mohnage7.movies.features.movies.model.Movie

@Dao
interface MovieDao {
    @Insert(onConflict = REPLACE)
    fun insertAll(movieList: List<Movie>)

    @Query("SELECT * FROM movie WHERE category = :category")
    fun getAllMovies(category: String): LiveData<List<Movie>>
}
