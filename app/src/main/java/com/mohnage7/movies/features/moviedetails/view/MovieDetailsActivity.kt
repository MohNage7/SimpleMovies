package com.mohnage7.movies.features.moviedetails.view

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView

import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.mohnage7.movies.BuildConfig
import com.mohnage7.movies.R
import com.mohnage7.movies.base.BaseActivity
import com.mohnage7.movies.features.movies.model.Movie
import com.mohnage7.movies.features.moviedetails.model.Video
import com.mohnage7.movies.features.moviedetails.viewmodel.MovieDetailViewModel
import com.squareup.picasso.Picasso

import butterknife.BindView
import butterknife.ButterKnife


class MovieDetailsActivity : BaseActivity() {
    @BindView(R.id.movie_title_txt_view)
    internal var titleTxtView: TextView? = null
    @BindView(R.id.movie_description)
    internal var descTxtView: TextView? = null
    @BindView(R.id.videos_tv)
    internal var videosTxtView: TextView? = null
    @BindView(R.id.movie_rating)
    internal var movieRatingTxtView: TextView? = null
    @BindView(R.id.movie_date_txt_view)
    internal var movieReleaseDateTxtView: TextView? = null
    @BindView(R.id.toolbar)
    internal var toolbar: Toolbar? = null
    @BindView(R.id.moviesRecyclerView)
    internal var recyclerView: RecyclerView? = null
    @BindView(R.id.back_drop_iv)
    internal var backdropImgView: ImageView? = null
    @BindView(R.id.app_bar)
    internal var mAppBarLayout: AppBarLayout? = null
    @BindView(R.id.collapsing_toolbar)
    internal var collapsingToolbarLayout: CollapsingToolbarLayout? = null
    @BindView(R.id.shimmerFrameLayout)
    internal var shimmerFrameLayout: ShimmerFrameLayout? = null

    private var movie: Movie? = null
    private var viewModel: MovieDetailViewModel? = null

    override fun layoutRes(): Int {
        return R.layout.activity_movie_details
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inject views
        ButterKnife.bind(this)
        // init view model
        viewModel = ViewModelProviders.of(this).get(MovieDetailViewModel::class.java)

        setupToolbar()
        // get movie object
        getMovieFromIntent()
        // fill views with data
        setViews()
    }

    private fun setupToolbar() {
        toolbar!!.setBackgroundColor(Color.TRANSPARENT)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        // set status bar color as transparent to be able to see poster below it.
        val window = this.window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.TRANSPARENT
        }

        mAppBarLayout!!.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            internal var isShow = false
            internal var scrollRange = -1

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

    private fun setViews() {
        titleTxtView!!.text = movie!!.title
        descTxtView!!.text = movie!!.overview
        movieRatingTxtView!!.text = movie!!.voteAverage.toString()
        movieReleaseDateTxtView!!.text = getString(R.string.release_date, movie!!.releaseDate)
        Picasso.get().load(BuildConfig.IMAGE_BASE_URL + movie!!.backdropPath!!).into(backdropImgView)
    }

    private fun showLoadingLayout() {
        shimmerFrameLayout!!.startShimmer()
        shimmerFrameLayout!!.visibility = View.VISIBLE
        recyclerView!!.visibility = View.GONE
    }

    private fun hideLoadingLayout() {
        shimmerFrameLayout!!.stopShimmer()
        shimmerFrameLayout!!.visibility = View.GONE
        recyclerView!!.visibility = View.VISIBLE
    }


    /**
     * this method gets movie object from coming intent
     */
    private fun getMovieFromIntent() {
        val intent = intent
        if (intent != null) {
            movie = intent.getParcelableExtra(MOVIE_EXTRA)
            if (movie != null) {
                viewModel!!.getVideosList(movie!!.id!!).observe(this, { dataWrapper ->
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
                        else -> {
                        }
                    }
                })
            } else {
                hideLoadingLayout()
                showToast(R.string.movie_load_failure)
                finish()
            }
        }
    }

    private fun setupRecycler(videoList: List<Video>?) {
        val videoAdapter = VideoAdapter(videoList)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.adapter = videoAdapter
    }

    private fun handleError(message: String?) {
        videosTxtView!!.visibility = View.GONE
        showToast(message!!)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // exits activity with image transaction animation
        overridePendingTransition(R.anim.no_animation, R.anim.anim_slide_down)
    }

    companion object {
        val MOVIE_EXTRA = "movie_extra"
    }
}
