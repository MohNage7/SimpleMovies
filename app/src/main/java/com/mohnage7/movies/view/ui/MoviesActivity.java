package com.mohnage7.movies.view.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.mohnage7.movies.R;
import com.mohnage7.movies.base.BaseActivity;
import com.mohnage7.movies.model.Category;
import com.mohnage7.movies.model.Movie;
import com.mohnage7.movies.utils.Constants;
import com.mohnage7.movies.view.adapter.MoviesAdapter;
import com.mohnage7.movies.view.adapter.SearchAdapter;
import com.mohnage7.movies.view.callback.OnCategorySelectedListener;
import com.mohnage7.movies.view.callback.OnMovieClickListener;
import com.mohnage7.movies.viewmodel.MoviesViewModel;

import java.util.List;

import butterknife.BindView;

import static com.mohnage7.movies.utils.Constants.MOVIE_EXTRA;
import static com.mohnage7.movies.utils.Constants.POPULAR;
import static com.mohnage7.movies.view.ui.CategoryBottomSheet.SELECTED_CATEGORY;

public class MoviesActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, OnMovieClickListener, OnCategorySelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView moviesRecyclerView;
    @BindView(R.id.search_recycler_view)
    RecyclerView searchRecyclerView;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_data_layout)
    RelativeLayout noDataLayout;
    @BindView(R.id.no_data_tv)
    TextView noDataTxtView;
    @BindView(R.id.no_search_data_tv)
    TextView noSearchDataTxtView;
    @BindView(R.id.shimmer_loading_layout)
    ShimmerFrameLayout shimmerFrameLayout;
    @BindView(R.id.search_card_view)
    CardView searchLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private MoviesViewModel moviesViewModel;
    private Category selectedCategory;

    @Override
    protected int layoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        // set swipe listener
        swipeRefreshLayout.setOnRefreshListener(this);
        // init view model
        moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        // load movies from network or db source
        getMovies(getSelectedCategory().getCategoryPath());
        // listen to search data
        moviesViewModel.search().observe(this, dataWrapper -> {
            switch (dataWrapper.status) {
                case LOADING:
                    showSearchLoading();
                    break;
                case ERROR:
                    handleSearchError(dataWrapper.message);
                    break;
                case SUCCESS:
                    setupSearchAdapter(dataWrapper.data);
                    break;
                default:
                    break;
            }
        });
    }

    private void showSearchLoading() {
        searchLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        noSearchDataTxtView.setVisibility(View.GONE);
        searchRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) menuItem.getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        EditText searchEditText = searchView.findViewById(R.id.search_src_text);
        searchEditText.setHintTextColor(ContextCompat.getColor(this, R.color.soft_grey));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        searchView.setQueryHint(getString(R.string.hint_search));
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.trim().length() > 1) {
                    searchView.clearFocus();
                    searchInMovies(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchLayout.setVisibility(View.GONE);
                searchView.onActionViewExpanded();
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_categories) {
            CategoryBottomSheet categoryBottomSheet = new CategoryBottomSheet();
            Bundle bundle = new Bundle();
            bundle.putParcelable(SELECTED_CATEGORY, getSelectedCategory());
            categoryBottomSheet.setArguments(bundle);
            categoryBottomSheet.show(getSupportFragmentManager(), null);
        }
        return true;
    }

    /**
     * @return default @selectedCategory if data is going to be fetched for the first time
     * or the category that has been selected by the user.
     */
    private Category getSelectedCategory() {
        if (selectedCategory == null)
            selectedCategory = new Category(getString(R.string.popular), POPULAR, R.drawable.ic_popular);
        return selectedCategory;
    }


    private void getMovies(@Constants.FilterBy String category) {
        moviesViewModel.getMoviesList().observe(this, dataWrapper -> {
            switch (dataWrapper.status) {
                case LOADING:
                    showLoadingLayout();
                    break;
                case SUCCESS:
                    hideLoadingLayout();
                    setupMoviesRecycler(dataWrapper.data);
                    break;
                case ERROR:
                    hideLoadingLayout();
                    handleMoviesListError(dataWrapper.message);
                    break;
                default:
                    break;
            }
        });
        moviesViewModel.setFilterMovieBy(category);
    }

    private void searchInMovies(String query) {
        moviesViewModel.setSearchBy(query);
    }

    private void showLoadingLayout() {
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        moviesRecyclerView.setVisibility(View.GONE);
        noDataLayout.setVisibility(View.GONE);
    }

    private void hideLoadingLayout() {
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        moviesRecyclerView.setVisibility(View.VISIBLE);
    }


    private void setDataViewsVisibility(boolean dataAvailable) {
        if (dataAvailable) {
            moviesRecyclerView.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
        } else {
            moviesRecyclerView.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        }
    }


    private void handleMoviesListError(String message) {
        setDataViewsVisibility(false);
        noDataTxtView.setText(message);
    }

    private void handleSearchError(String message) {
        setSearchViewsVisibility(false);
        noSearchDataTxtView.setText(message == null || message.isEmpty() ? getString(R.string.no_data_found) : message);
    }

    private void setSearchViewsVisibility(boolean dataAvailable) {
        searchLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        if (dataAvailable) {
            searchRecyclerView.setVisibility(View.VISIBLE);
            noSearchDataTxtView.setVisibility(View.GONE);
        } else {
            searchRecyclerView.setVisibility(View.GONE);
            noSearchDataTxtView.setVisibility(View.VISIBLE);
        }
    }

    private void setupMoviesRecycler(List<Movie> moviesList) {
        hideLoadingLayout();
        setDataViewsVisibility(true);
        MoviesAdapter adapter = new MoviesAdapter(moviesList, this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        moviesRecyclerView.setLayoutManager(layoutManager);
        moviesRecyclerView.setAdapter(adapter);
    }

    private void setupSearchAdapter(List<Movie> moviesList) {
        setSearchViewsVisibility(true);
        SearchAdapter adapter = new SearchAdapter(moviesList, this);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchRecyclerView.setAdapter(adapter);
    }


    @Override
    public void onRefresh() {
        moviesViewModel.setFilterMovieBy(selectedCategory.getCategoryPath());
    }

    @Override
    public void onMovieClick(Movie movie, View view) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(MOVIE_EXTRA, movie);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_up, R.anim.no_animation);
    }

    @Override
    public void onCategoryClick(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
        // change activity title
        toolbar.setTitle(selectedCategory.getName());
        // refresh movies list
        moviesViewModel.setFilterMovieBy(selectedCategory.getCategoryPath());
    }
}
