package com.mohnage7.movies.view.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import com.mohnage7.movies.base.BaseError;
import com.mohnage7.movies.model.Category;
import com.mohnage7.movies.model.Movie;
import com.mohnage7.movies.utils.Constants;
import com.mohnage7.movies.view.adapter.MoviesAdapter;
import com.mohnage7.movies.view.adapter.SearchAdapter;
import com.mohnage7.movies.view.callback.OnMovieClickListener;
import com.mohnage7.movies.view.callback.OnCategorySelectedListener;
import com.mohnage7.movies.viewmodel.MoviesViewModel;

import java.util.List;

import butterknife.BindView;

import static com.mohnage7.movies.utils.Constants.MOVIE_EXTRA;
import static com.mohnage7.movies.utils.Constants.POPULAR;
import static com.mohnage7.movies.view.ui.CategoryBottomSheet.SELECTED_CATEGORY;

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, OnMovieClickListener, OnCategorySelectedListener {

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
        moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        swipeRefreshLayout.setOnRefreshListener(this);
        getArticles(POPULAR);
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
        if (item.getItemId() == R.id.action_filter_by) {
            //showPopupMenu(vItem);
            CategoryBottomSheet categoryBottomSheet = new CategoryBottomSheet();
            if (selectedCategory != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(SELECTED_CATEGORY, selectedCategory);
                categoryBottomSheet.setArguments(bundle);
            }
            categoryBottomSheet.show(getSupportFragmentManager(), null);
        }
        return true;
    }


    private void getArticles(@Constants.FilterBy String filter) {
        showLoadingLayout();
        moviesViewModel.getMoviesList(filter).observe(this, dataWrapper -> {
            swipeRefreshLayout.setRefreshing(false);
            if (dataWrapper.getBaseError() != null) {
                handleMoviesListError(dataWrapper.getBaseError());
            } else {
                setupArticlesRecycler(dataWrapper.getData());
            }
        });
    }

    private void searchInMovies(String query) {
        moviesViewModel.search(query).observe(this, dataWrapper -> {
            swipeRefreshLayout.setRefreshing(false);
            if (dataWrapper.getBaseError() != null) {
                handleSearchError(dataWrapper.getBaseError());
            } else {
                setupSearchAdapter(dataWrapper.getData());
            }
        });
    }

    private void showLoadingLayout() {
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        moviesRecyclerView.setVisibility(View.GONE);
    }

    private void hideLoadingLayout() {
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

    private void setSearchViewsVisibility(boolean dataAvailable) {
        if (dataAvailable) {
            searchLayout.setVisibility(View.VISIBLE);
            searchRecyclerView.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
        } else {
            searchRecyclerView.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        }
    }

    private void handleMoviesListError(BaseError baseError) {
        setDataViewsVisibility(false);
        hideLoadingLayout();
        if (baseError.getErrorCode() == 500) {
            noDataTxtView.setText(getString(R.string.server_error));
        } else {
            noDataTxtView.setText(baseError.getErrorMessage());
        }
    }

    private void handleSearchError(BaseError baseError) {
        setSearchViewsVisibility(false);
        hideLoadingLayout();
        if (baseError.getErrorCode() == 500) {
            noSearchDataTxtView.setText(getString(R.string.server_error));
        } else {
            noSearchDataTxtView.setText(baseError.getErrorMessage());
        }
    }

    private void setupArticlesRecycler(List<Movie> articles) {
        hideLoadingLayout();
        setDataViewsVisibility(true);
        MoviesAdapter adapter = new MoviesAdapter(articles, this);
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
        getArticles(POPULAR);
    }

    @Override
    public void onMovieClick(Movie article, View view) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(MOVIE_EXTRA, article);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
        // change activity title
        toolbar.setTitle(selectedCategory.getName());
        // refresh movies list
        getArticles(selectedCategory.getCategoryPath());
    }
}
