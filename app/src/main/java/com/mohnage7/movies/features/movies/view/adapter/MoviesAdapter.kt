package com.mohnage7.movies.features.movies.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.mohnage7.movies.BuildConfig
import com.mohnage7.movies.R
import com.mohnage7.movies.base.BaseViewHolder
import com.mohnage7.movies.features.movies.model.Movie
import com.mohnage7.movies.utils.Constants
import com.mohnage7.movies.features.movies.view.callback.OnMovieClickListener
import com.squareup.picasso.Picasso

import butterknife.BindView
import butterknife.ButterKnife


class MoviesAdapter(private val movieList: List<Movie>?, private val onMovieClickListener: OnMovieClickListener) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        view.layoutParams.height = (parent.width / 3 * 1.5).toInt()
        return MoviesViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindViews(position)
    }

    override fun getItemCount(): Int {
        return if (movieList != null && !movieList.isEmpty()) {
            movieList.size
        } else {
            0
        }
    }


    protected inner class MoviesViewHolder internal constructor(itemView: View) : BaseViewHolder(itemView) {
        @BindView(R.id.movie_img_view)
        internal var movieImageView: ImageView? = null
        @BindView(R.id.movie_title_txt_view)
        internal var movieTitle: TextView? = null


        init {
            ButterKnife.bind(this, itemView)
        }

        override fun bindViews(position: Int) {
            super.bindViews(position)
            val movie = movieList!![position]
            movieTitle!!.text = movie.title
            displayArticleImage(movie.posterPath)
            itemView.setOnClickListener { v -> onMovieClickListener.onMovieClick(movie, itemView) }
        }

        private fun displayArticleImage(imageUrl: String?) {
            if (imageUrl != null) {
                Picasso.get().load(BuildConfig.IMAGE_BASE_URL + imageUrl).error(R.drawable.placeholder).into(movieImageView)
            } else {
                Picasso.get().load(R.drawable.placeholder).into(movieImageView)
            }
        }


        override fun clear() {
            movieTitle!!.text = ""
            movieImageView!!.setImageDrawable(null)
        }
    }
}
