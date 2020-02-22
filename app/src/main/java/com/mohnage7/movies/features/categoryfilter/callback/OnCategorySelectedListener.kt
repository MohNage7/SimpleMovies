package com.mohnage7.movies.features.categoryfilter.callback

import com.mohnage7.movies.features.categoryfilter.model.Category

interface OnCategorySelectedListener {
    fun onCategorySelected(selectedCategory: Category)
}
