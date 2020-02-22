package com.mohnage7.movies.features.moviedetails.view

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.mohnage7.movies.BuildConfig
import com.mohnage7.movies.R
import com.mohnage7.movies.base.BaseActivity
import com.mohnage7.movies.features.moviedetails.model.Video
import com.mohnage7.movies.features.moviedetails.viewmodel.MovieDetailViewModel
import com.mohnage7.movies.features.movies.model.Movie
import com.mohnage7.movies.network.model.DataWrapper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_movie_details.*
import kotlinx.android.synthetic.main.layout_loading_videos.*
import org.koin.android.viewmodel.ext.android.viewModel


class MovieDetailsActivity : BaseActivity() {

    private var movie: Movie? = null
    private val movieDetailViewModel: MovieDetailViewModel by viewModel()

    override fun layoutRes(): Int {
        return R.layout.activity_movie_details
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        movie = getMovieFromIntent()
        loadVideos(movie)
        setViews(movie)
    }

    private fun loadVideos(movie: Movie?) {
        if (movie != null) {
            movieDetailViewModel.getVideosList(movie.id!!).observe(this, Observer { dataWrapper ->
                when (dataWrapper.status) {
                    DataWrapper.Status.LOADING -> showLoadingLayout()
                    DataWrapper.Status.SUCCESS -> {
                        hideLoadingLayout()
                        setupRecycler(dataWrapper.data!!.videos)
                    }
                    DataWrapper.Status.ERROR -> {
                        hideLoadingLayout()
                        handleError(dataWrapper.message)
                    }
                }
            })
        } else {
            hideLoadingLayout()
            showToast(R.string.movie_load_failure)
            finish()
        }
    }

    private fun setupToolbar() {
        toolbar.setBackgroundColor(Color.TRANSPARENT)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
        }
        // set status bar color as transparent to be able to see poster below it.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.apply {
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                statusBarColor = Color.TRANSPARENT
            }
        }
        setToolbarScrollListener()
    }

    private fun setToolbarScrollListener() {
        mAppBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true
                    collapsingToolbarLayout!!.title = movie!!.title
                } else if (isShow) {
                    collapsingToolbarLayout!!.title = ""
                    isShow = false
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    private fun setViews(movie: Movie?) {
        movie?.let {
            titleTxtView.text = it.title
            descTxtView.text = it.overview
            movieRatingTxtView.text = it.voteAverage.toString()
            movieReleaseDateTxtView.text = getString(R.string.release_date, it.releaseDate)
            it.backdropPath?.let { backdrop ->
                Picasso.get().load(BuildConfig.IMAGE_BASE_URL + backdrop).into(backdropImgView)
            }
        }

    }

    private fun showLoadingLayout() {
        shimmerFrameLayout.startShimmer()
        shimmerFrameLayout.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun hideLoadingLayout() {
        shimmerFrameLayout.stopShimmer()
        shimmerFrameLayout.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }


    /**
     * this method gets movie object from coming intent
     */
    private fun getMovieFromIntent(): Movie {
        return intent.getParcelableExtra(MOVIE_EXTRA)
    }

    private fun setupRecycler(videoList: List<Video>?) {
        val videoAdapter = VideoAdapter(videoList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = videoAdapter
    }

    private fun handleError(message: String?) {
        videosTxtView.visibility = View.GONE
        showToast(message!!)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // exits activity with image transaction animation
        overridePendingTransition(R.anim.no_animation, R.anim.anim_slide_down)
    }

    companion object {
        const val MOVIE_EXTRA = "movie_extra"
    }
}
