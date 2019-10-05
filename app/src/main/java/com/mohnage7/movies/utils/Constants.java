package com.mohnage7.movies.utils;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class Constants {
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String MOVIE_EXTRA = "movie_extra";
    public static final String API_KEY = "";
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String UP_COMING = "upcoming";
    public static final String YOUTUBE_KEY = "";
    public static final int CONNECTION_TIMEOUT = 10; // 10 seconds
    public static final int READ_TIMEOUT = 2; // 2 seconds
    public static final int WRITE_TIMEOUT = 2; // 2 seconds
    public static final int CACHE_TIMEOUT = 1; // 2 minutes
    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w342";


    private Constants() {
        // restrict class creation
    }

    @Retention(SOURCE)
    @StringDef({
            POPULAR,
            TOP_RATED,
            UP_COMING
    })
    public @interface FilterBy {
    }
}
