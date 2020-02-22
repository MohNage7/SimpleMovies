package com.mohnage7.movies.features.moviedetails.model


import com.google.gson.annotations.SerializedName

class Video {

    @SerializedName("id")
    var id: String? = null
    @SerializedName("key")
    var key: String? = null
    @SerializedName("name")
    var name: String? = null
}
