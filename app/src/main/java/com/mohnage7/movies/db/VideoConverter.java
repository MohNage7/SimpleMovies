package com.mohnage7.movies.db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mohnage7.movies.model.Video;

import java.lang.reflect.Type;
import java.util.List;

public class VideoConverter {

    @TypeConverter
    public String fromVideoList(List<Video> videosList) {
        if (videosList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Video>>() {
        }.getType();
        String json = gson.toJson(videosList, type);
        return json;
    }

    @TypeConverter
    public List<Video> toVideoList(String video) {
        if (video == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Video>>() {}.getType();
        List<Video> videoList = gson.fromJson(video, type);
        return videoList;
    }
}
