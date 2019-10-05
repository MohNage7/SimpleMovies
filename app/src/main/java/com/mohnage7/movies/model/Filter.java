package com.mohnage7.movies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.mohnage7.movies.utils.Constants;


public class Filter implements Parcelable {
    public static final Creator<Filter> CREATOR = new Creator<Filter>() {
        @Override
        public Filter createFromParcel(Parcel in) {
            return new Filter(in);
        }

        @Override
        public Filter[] newArray(int size) {
            return new Filter[size];
        }
    };
    private int drawable;
    private String name;
    private boolean checked;
    private String categoryPath;

    public Filter(String name, @Constants.FilterBy String categoryPath, int drawable) {
        this.drawable = drawable;
        this.categoryPath = categoryPath;
        this.name = name;
    }

    protected Filter(Parcel in) {
        drawable = in.readInt();
        name = in.readString();
        checked = in.readByte() != 0;
        categoryPath = in.readString();
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getCategoryPath() {
        return categoryPath;
    }

    public int getDrawable() {
        return drawable;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Filter filter = (Filter) o;
        return name.equals(filter.name) &&
                drawable == filter.drawable;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(drawable);
        dest.writeString(name);
        dest.writeByte((byte) (checked ? 1 : 0));
        dest.writeString(categoryPath);
    }
}
