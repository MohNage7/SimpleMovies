package com.mohnage7.movies.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun clear()

    open fun bindViews(position: Int) {
        clear()
    }
}
