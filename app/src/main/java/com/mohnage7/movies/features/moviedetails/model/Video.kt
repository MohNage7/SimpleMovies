package com.mohnage7.movies.features.moviedetails.model


import com.google.gson.annotations.SerializedName
import com.mohnage7.movies.features.moviedetails.view.YOUTUBE_THUMBNAIL_BASE_URL
import com.mohnage7.movies.features.moviedetails.view.YOUTUBE_THUMBNAIL_URL_JPG

data class Video(@SerializedName("id")
                 var id: String? = null,
                 @SerializedName("key")
                 var key: String? = null,
                 @SerializedName("name")
                 var name: String? = null) {

    fun getVideoThumbnail(): String {
        return YOUTUBE_THUMBNAIL_BASE_URL + key +
                YOUTUBE_THUMBNAIL_URL_JPG
    }
}
