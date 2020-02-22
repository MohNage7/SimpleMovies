package com.mohnage7.movies.features.categoryfilter.model

import android.os.Parcel
import android.os.Parcelable

import com.mohnage7.movies.features.categoryfilter.view.CategoryBottomSheet


class Category : Parcelable {
    var drawable: Int = 0
        private set
    var name: String? = null
        private set
    var isChecked: Boolean = false
    var categoryPath: String? = null
        private set

    constructor(name: String, @CategoryBottomSheet.FilterBy categoryPath: String, drawable: Int) {
        this.drawable = drawable
        this.categoryPath = categoryPath
        this.name = name
    }

    protected constructor(`in`: Parcel) {
        drawable = `in`.readInt()
        name = `in`.readString()
        isChecked = `in`.readByte().toInt() != 0
        categoryPath = `in`.readString()
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val category = o as Category?
        return name == category!!.name && drawable == category.drawable
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(drawable)
        dest.writeString(name)
        dest.writeByte((if (isChecked) 1 else 0).toByte())
        dest.writeString(categoryPath)
    }

    companion object {
        val CREATOR: Parcelable.Creator<Category> = object : Parcelable.Creator<Category> {
            override fun createFromParcel(`in`: Parcel): Category {
                return Category(`in`)
            }

            override fun newArray(size: Int): Array<Category> {
                return arrayOfNulls(size)
            }
        }
    }
}
