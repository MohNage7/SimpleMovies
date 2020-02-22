package com.mohnage7.movies.features.categoryfilter.view

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

import com.mohnage7.movies.R
import com.mohnage7.movies.base.BaseViewHolder
import com.mohnage7.movies.features.categoryfilter.model.Category
import com.mohnage7.movies.features.movies.view.callback.OnCategoryClickListener

import butterknife.BindView
import butterknife.ButterKnife

class CategoryAdapter(private val categoryList: List<Category>?, private val onArticleClickListener: OnCategoryClickListener) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_filter, parent, false)
        return MoviesViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindViews(position)
    }

    override fun getItemCount(): Int {
        return if (categoryList != null && !categoryList.isEmpty()) {
            categoryList.size
        } else {
            0
        }
    }


    protected inner class MoviesViewHolder internal constructor(itemView: View) : BaseViewHolder(itemView) {
        @BindView(R.id.category_image)
        internal var categoryImgView: ImageView? = null
        @BindView(R.id.category_name_tv)
        internal var categoryTitle: TextView? = null


        init {
            ButterKnife.bind(this, itemView)
        }

        override fun bindViews(position: Int) {
            super.bindViews(position)
            val category = categoryList!![position]
            categoryTitle!!.text = category.name
            categoryImgView!!.setImageDrawable(ContextCompat.getDrawable(itemView.context, category.drawable))
            if (category.isChecked) {
                val img = itemView.context.resources.getDrawable(R.drawable.ic_check)
                categoryTitle!!.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null)
            } else {
                categoryTitle!!.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }
            itemView.setOnClickListener { v ->
                category.isChecked = true
                onArticleClickListener.onCategoryClick(category)
            }
        }


        override fun clear() {
            categoryTitle!!.text = ""
            categoryImgView!!.setImageDrawable(null)
        }
    }
}
