package com.mohnage7.movies.features.categoryfilter.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mohnage7.movies.R
import com.mohnage7.movies.base.BaseViewHolder
import com.mohnage7.movies.features.categoryfilter.callback.OnCategoryClickListener
import com.mohnage7.movies.features.categoryfilter.model.Category
import kotlinx.android.synthetic.main.item_category_filter.view.*

class CategoryAdapter(private val categoryList: List<Category>, private val onArticleClickListener: OnCategoryClickListener) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_filter, parent, false)
        return MoviesViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindViews(position)
    }

    override fun getItemCount(): Int {
        return if (categoryList.isNotEmpty()) {
            categoryList.size
        } else {
            0
        }
    }


    private inner class MoviesViewHolder internal constructor(itemView: View) : BaseViewHolder(itemView) {

        override fun bindViews(position: Int) {
            super.bindViews(position)

            val category = categoryList[position]

            itemView.categoryTitle.text = category.name
            itemView.categoryImgView.setImageDrawable(ContextCompat.getDrawable(itemView.context, category.drawable))

            if (category.isChecked) {
                val checkIcon = ContextCompat.getDrawable(itemView.context,R.drawable.ic_check)
                itemView.categoryTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, checkIcon, null)
            } else {
                itemView.categoryTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }

            itemView.setOnClickListener { v ->
                category.isChecked = true
                onArticleClickListener.onCategoryClick(category)
            }
        }


        override fun clear() {
            itemView.categoryTitle.text = ""
            itemView.categoryImgView.setImageDrawable(null)
        }
    }
}
