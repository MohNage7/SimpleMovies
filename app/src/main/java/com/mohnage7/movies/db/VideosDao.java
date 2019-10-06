package com.mohnage7.movies.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.mohnage7.movies.model.MovieVideos;
import com.mohnage7.movies.model.Video;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface VideosDao {
    @Insert(onConflict = REPLACE)
    void insertAll(MovieVideos movieVideos);

    @Query("SELECT * FROM movie_videos WHERE id = :movieId")
    LiveData<MovieVideos> getAllVideos(int movieId);
}
