package com.mohnage7.movies.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.mohnage7.movies.model.Movie;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MovieDao {
    @Insert(onConflict = REPLACE)
    void insertAll(List<Movie> movieList);

    @Query("SELECT * FROM movie WHERE category = :category")
    LiveData<List<Movie>> getAllMovies(String category);
}
