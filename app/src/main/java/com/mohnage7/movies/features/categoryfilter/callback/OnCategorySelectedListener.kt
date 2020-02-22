package com.mohnage7.movies.features.movies.view.callback

import com.mohnage7.movies.features.categoryfilter.model.Category

interface OnCategorySelectedListener {
    fun onCategoryClick(selectedCategory: Category)
}
