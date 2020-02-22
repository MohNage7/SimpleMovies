package com.mohnage7.movies.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mohnage7.movies.features.moviedetails.model.Video

class VideoConverter {

    @TypeConverter
    fun fromVideoList(videosList: List<Video>?): String? {
        if (videosList == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<Video>>() {

        }.type
        return gson.toJson(videosList, type)
    }

    @TypeConverter
    fun toVideoList(video: String?): List<Video>? {
        if (video == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<Video>>() {

        }.type
        return gson.fromJson<List<Video>>(video, type)
    }
}
