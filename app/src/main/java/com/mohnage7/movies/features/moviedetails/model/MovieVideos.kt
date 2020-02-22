package com.mohnage7.movies.features.moviedetails.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie_videos")
data class MovieVideos(@PrimaryKey
                  @SerializedName("id")
                  var id: Long? = null,
                  @ColumnInfo(name = "timestamp")
                  var timestamp: Int = 0,
                  @SerializedName("results")
                  var videos: List<Video>? = null)