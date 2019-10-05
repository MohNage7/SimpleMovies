
package com.mohnage7.movies.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.mohnage7.movies.db.VideoConverter;

import java.util.List;

@Entity(tableName = "movie_videos")

public class MovieVideos {
    @PrimaryKey
    @SerializedName("id")
    private Long id;
    @ColumnInfo(name = "timestamp")
    private int timestamp;
    @TypeConverters(VideoConverter.class)
    @SerializedName("results")
    private List<Video> videos;

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

}
