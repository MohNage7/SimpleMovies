package com.mohnage7.movies.features.moviedetails.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

import com.google.gson.annotations.SerializedName
import com.mohnage7.movies.db.VideoConverter

@Entity(tableName = "movie_videos")
class MovieVideos {
    @PrimaryKey
    @SerializedName("id")
    var id: Long? = null
    @ColumnInfo(name = "timestamp")
    var timestamp: Int = 0
    @TypeConverters(VideoConverter::class)
    @SerializedName("results")
    var videos: List<Video>? = null

}
