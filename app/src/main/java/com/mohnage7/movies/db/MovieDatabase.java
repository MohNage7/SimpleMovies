package com.mohnage7.movies.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mohnage7.movies.model.Movie;
import com.mohnage7.movies.model.MovieVideos;

@Database(entities = {Movie.class, MovieVideos.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static final String DATA_BASE_NAME = "movies_db";
    private static MovieDatabase INSTANCE;

    public static synchronized MovieDatabase getDatabaseInstance(Context context) {
        // insure that no other reference is created on different threads.
        if (INSTANCE == null) {
            // create our one and only db object.
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    // fallbackToDestructiveMigration is a migration strategy that destroy and re-creating existing db
                    // fallbackToDestructiveMigration is only used for small applications like we are implementing now
                    // for real projects we need to implement non-destructive migration strategy.
                    MovieDatabase.class, DATA_BASE_NAME).fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }

    public abstract MovieDao getMovieDao();
    public abstract VideosDao getVideosDao();
}
