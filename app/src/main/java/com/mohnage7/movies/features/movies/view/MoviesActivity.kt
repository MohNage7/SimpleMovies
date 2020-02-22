package com.mohnage7.movies.features.movies.view

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView

import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.facebook.shimmer.ShimmerFrameLayout
import com.mohnage7.movies.R
import com.mohnage7.movies.base.BaseActivity
import com.mohnage7.movies.features.movies.view.adapter.MoviesAdapter
import com.mohnage7.movies.features.movies.view.adapter.SearchAdapter
import com.mohnage7.movies.features.categoryfilter.model.Category
import com.mohnage7.movies.features.movies.model.Movie
import com.mohnage7.movies.features.movies.view.callback.OnCategorySelectedListener
import com.mohnage7.movies.features.movies.view.callback.OnMovieClickListener
import com.mohnage7.movies.features.categoryfilter.view.CategoryBottomSheet
import com.mohnage7.movies.features.moviedetails.view.MovieDetailsActivity
import com.mohnage7.movies.features.movies.viewmodel.MoviesViewModel

import butterknife.BindView

import com.mohnage7.movies.features.categoryfilter.view.CategoryBottomSheet.POPULAR
import com.mohnage7.movies.features.categoryfilter.view.CategoryBottomSheet.SELECTED_CATEGORY
import com.mohnage7.movies.features.moviedetails.view.MovieDetailsActivity.MOVIE_EXTRA

class MoviesActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener, OnMovieClickListener, OnCategorySelectedListener {

    @BindView(R.id.toolbar)
    internal var toolbar: Toolbar? = null
    @BindView(R.id.recycler_view)
    internal var moviesRecyclerView: RecyclerView? = null
    @BindView(R.id.search_recycler_view)
    internal var searchRecyclerView: RecyclerView? = null
    @BindView(R.id.swipe_layout)
    internal var swipeRefreshLayout: SwipeRefreshLayout? = null
    @BindView(R.id.no_data_layout)
    internal var noDataLayout: RelativeLayout? = null
    @BindView(R.id.no_data_tv)
    internal var noDataTxtView: TextView? = null
    @BindView(R.id.no_search_data_tv)
    internal var noSearchDataTxtView: TextView? = null
    @BindView(R.id.shimmer_loading_layout)
    internal var shimmerFrameLayout: ShimmerFrameLayout? = null
    @BindView(R.id.search_card_view)
    internal var searchLayout: CardView? = null
    @BindView(R.id.progress_bar)
    internal var progressBar: ProgressBar? = null

    private var moviesViewModel: MoviesViewModel? = null
    private var selectedCategory: Category? = null

    override fun layoutRes(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        // set swipe listener
        swipeRefreshLayout!!.setOnRefreshListener(this)
        // init view model
        moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel::class.java)
        // load movies from network or db source
        getMovies(getSelectedCategory().categoryPath)
        // listen to search data
        moviesViewModel!!.search().observe(this, { dataWrapper ->
            when (dataWrapper.status) {
                DataWrapper.Status.LOADING -> showSearchLoading()
                DataWrapper.Status.ERROR -> handleSearchError(dataWrapper.message)
                DataWrapper.Status.SUCCESS -> setupSearchAdapter(dataWrapper.data)
                else -> {
                }
            }
        })
    }

    private fun showSearchLoading() {
        searchLayout!!.visibility = View.VISIBLE
        progressBar!!.visibility = View.VISIBLE
        noSearchDataTxtView!!.visibility = View.GONE
        searchRecyclerView!!.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        val menuItem = menu.findItem(R.id.action_search)
        val searchView = menuItem.actionView as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchEditText = searchView.findViewById<EditText>(R.id.search_src_text)
        searchEditText.setHintTextColor(ContextCompat.getColor(this, R.color.soft_grey))
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.componentName))
        searchView.queryHint = getString(R.string.hint_search)
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.trim { it <= ' ' }.length > 1) {
                    searchView.clearFocus()
                    searchInMovies(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                searchLayout!!.visibility = View.GONE
                searchView.onActionViewExpanded()
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_categories) {
            val categoryBottomSheet = CategoryBottomSheet()
            val bundle = Bundle()
            bundle.putParcelable(Companion.getSELECTED_CATEGORY(), getSelectedCategory())
            categoryBottomSheet.arguments = bundle
            categoryBottomSheet.show(supportFragmentManager, null)
        }
        return true
    }

    /**
     * @return default @selectedCategory if data is going to be fetched for the first time
     * or the category that has been selected by the user.
     */
    private fun getSelectedCategory(): Category {
        if (selectedCategory == null)
            selectedCategory = Category(getString(R.string.popular), Companion.getPOPULAR(), R.drawable.ic_popular)
        return selectedCategory
    }


    private fun getMovies(@CategoryBottomSheet.FilterBy category: String?) {
        moviesViewModel!!.moviesList.observe(this, { dataWrapper ->
            when (dataWrapper.status) {
                DataWrapper.Status.LOADING -> showLoadingLayout()
                DataWrapper.Status.SUCCESS -> {
                    hideLoadingLayout()
                    setupMoviesRecycler(dataWrapper.data)
                }
                DataWrapper.Status.ERROR -> {
                    hideLoadingLayout()
                    handleMoviesListError(dataWrapper.message)
                }
                else -> {
                }
            }
        })
        moviesViewModel!!.setFilterMovieBy(category)
    }

    private fun searchInMovies(query: String) {
        moviesViewModel!!.setSearchBy(query)
    }

    private fun showLoadingLayout() {
        shimmerFrameLayout!!.startShimmer()
        shimmerFrameLayout!!.visibility = View.VISIBLE
        moviesRecyclerView!!.visibility = View.GONE
        noDataLayout!!.visibility = View.GONE
    }

    private fun hideLoadingLayout() {
        if (swipeRefreshLayout!!.isRefreshing)
            swipeRefreshLayout!!.isRefreshing = false
        shimmerFrameLayout!!.stopShimmer()
        shimmerFrameLayout!!.visibility = View.GONE
        moviesRecyclerView!!.visibility = View.VISIBLE
    }


    private fun setDataViewsVisibility(dataAvailable: Boolean) {
        if (dataAvailable) {
            moviesRecyclerView!!.visibility = View.VISIBLE
            noDataLayout!!.visibility = View.GONE
        } else {
            moviesRecyclerView!!.visibility = View.GONE
            noDataLayout!!.visibility = View.VISIBLE
        }
    }


    private fun handleMoviesListError(message: String?) {
        setDataViewsVisibility(false)
        noDataTxtView!!.text = message
    }

    private fun handleSearchError(message: String?) {
        setSearchViewsVisibility(false)
        noSearchDataTxtView!!.text = if (message == null || message.isEmpty()) getString(R.string.no_data_found) else message
    }

    private fun setSearchViewsVisibility(dataAvailable: Boolean) {
        searchLayout!!.visibility = View.VISIBLE
        progressBar!!.visibility = View.GONE
        if (dataAvailable) {
            searchRecyclerView!!.visibility = View.VISIBLE
            noSearchDataTxtView!!.visibility = View.GONE
        } else {
            searchRecyclerView!!.visibility = View.GONE
            noSearchDataTxtView!!.visibility = View.VISIBLE
        }
    }

    private fun setupMoviesRecycler(moviesList: List<Movie>?) {
        hideLoadingLayout()
        setDataViewsVisibility(true)
        val adapter = MoviesAdapter(moviesList, this)
        val layoutManager = GridLayoutManager(this, 3)
        moviesRecyclerView!!.layoutManager = layoutManager
        moviesRecyclerView!!.adapter = adapter
    }

    private fun setupSearchAdapter(moviesList: List<Movie>?) {
        setSearchViewsVisibility(true)
        val adapter = SearchAdapter(moviesList, this)
        searchRecyclerView!!.layoutManager = LinearLayoutManager(this)
        searchRecyclerView!!.adapter = adapter
    }


    override fun onRefresh() {
        moviesViewModel!!.setFilterMovieBy(selectedCategory!!.categoryPath)
    }

    override fun onMovieClick(movie: Movie, view: View) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable(MOVIE_EXTRA, movie)
        intent.putExtras(bundle)
        startActivity(intent)
        overridePendingTransition(R.anim.anim_slide_up, R.anim.no_animation)
    }

    override fun onCategoryClick(selectedCategory: Category) {
        this.selectedCategory = selectedCategory
        // change activity title
        toolbar!!.title = selectedCategory.name
        // refresh movies list
        moviesViewModel!!.setFilterMovieBy(selectedCategory.categoryPath)
    }
}
