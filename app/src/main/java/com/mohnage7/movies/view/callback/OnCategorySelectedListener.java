package com.mohnage7.movies.view.callback;

import androidx.annotation.NonNull;

import com.mohnage7.movies.model.Category;

public interface OnCategorySelectedListener {
    void onCategoryClick(@NonNull Category selectedCategory);
}
