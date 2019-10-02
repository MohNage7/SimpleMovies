package com.mohnage7.movies.utils;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class Constants {
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String MOVIE_EXTRA = "movie_extra";
    public static final String API_KEY = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJkYzgwNDFkZmUyYmU1MWRiNTJlMGZmYWFkYWJlYmM4ZCIsInN1YiI6IjVhOTcyMzQwMGUwYTI2MDE1ZjAwNTMyMCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.sogWARNxbqkf9fduFd1vetw8KUyDn5xY1Bb_4sVJS1o";
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String UP_COMING = "upcoming";
    public static final String YOUTUBE_KEY = "AIzaSyDTsUcgRF_0mn-6bUoRRB3jqvoL0iPl85k";

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
