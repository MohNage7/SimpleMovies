package com.mohnage7.movies.view.callback;

import androidx.annotation.NonNull;

import com.mohnage7.movies.model.Filter;

public interface OnCategorySelectedListener {
    void onCategoryClick(@NonNull Filter selectedFilter);
}
