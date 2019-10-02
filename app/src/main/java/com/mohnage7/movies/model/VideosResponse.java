
package com.mohnage7.movies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideosResponse {

    @SerializedName("id")
    private Long Id;
    @SerializedName("results")
    private List<Video> trailers;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public List<Video> getVideos() {
        return trailers;
    }

    public void setTrailers(List<Video> trailers) {
        this.trailers = trailers;
    }

}
