package com.mohnage7.movies.features.movies.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by mohnage7 on 2/28/2018.
 */

@Entity(tableName = "movie")
data class Movie(@SerializedName("vote_average")
                 @Expose
                 var voteAverage: Double? = null,
                 @SerializedName("title")
                 @Expose
                 var title: String? = null,
                 @SerializedName("poster_path")
                 @Expose
                 var posterPath: String? = null,
                 @SerializedName("backdrop_path")
                 @Expose
                 var backdropPath: String? = null,
                 @SerializedName("overview")
                 @Expose
                 var overview: String? = null,
                 @SerializedName("release_date")
                 @Expose
                 var releaseDate: String? = null,
                 var category: String? = null,

                 @PrimaryKey
                 @SerializedName("id")
                 var id: Int? = null) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(voteAverage)
        parcel.writeString(title)
        parcel.writeString(posterPath)
        parcel.writeString(backdropPath)
        parcel.writeString(overview)
        parcel.writeString(releaseDate)
        parcel.writeString(category)
        parcel.writeValue(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }
}
