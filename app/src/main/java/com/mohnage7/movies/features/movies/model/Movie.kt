package com.mohnage7.movies.features.movies.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import com.mohnage7.movies.BuildConfig.IMAGE_BASE_URL

/**
 * Created by mohnage7 on 2/28/2018.
 */

@Entity(tableName = "movie")
class Movie : Parcelable {
    @SerializedName("vote_average")
    @Expose
    var voteAverage: Double? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("poster_path")
    @Expose
    var posterPath: String? = null
    @SerializedName("backdrop_path")
    @Expose
    var backdropPath: String? = null
    @SerializedName("overview")
    @Expose
    var overview: String? = null
    @SerializedName("release_date")
    @Expose
    var releaseDate: String? = null
    var category: String? = null

    @PrimaryKey
    @SerializedName("id")
    var id: Int? = null

    constructor() {}

    protected constructor(`in`: Parcel) {
        if (`in`.readByte().toInt() == 0) {
            id = null
        } else {
            id = `in`.readInt()
        }
        if (`in`.readByte().toInt() == 0) {
            voteAverage = null
        } else {
            voteAverage = `in`.readDouble()
        }
        title = `in`.readString()
        posterPath = `in`.readString()
        backdropPath = `in`.readString()
        overview = `in`.readString()
        releaseDate = `in`.readString()
        category = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }


    override fun writeToParcel(dest: Parcel, flags: Int) {
        if (id == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeInt(id)
        }
        if (voteAverage == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeDouble(voteAverage!!)
        }
        dest.writeString(title)
        dest.writeString(posterPath)
        dest.writeString(backdropPath)
        dest.writeString(overview)
        dest.writeString(releaseDate)
        dest.writeString(category)
    }

    companion object {
        val CREATOR: Parcelable.Creator<Movie> = object : Parcelable.Creator<Movie> {
            override fun createFromParcel(`in`: Parcel): Movie {
                return Movie(`in`)
            }

            override fun newArray(size: Int): Array<Movie> {
                return arrayOfNulls(size)
            }
        }
    }
}
