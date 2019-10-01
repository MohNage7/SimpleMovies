package com.mohnage7.movies.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mohnage7.movies.R;
import com.mohnage7.movies.base.BaseActivity;
import com.mohnage7.movies.base.BaseError;
import com.mohnage7.movies.model.Category;
import com.mohnage7.movies.model.Movie;
import com.mohnage7.movies.utils.Constants;
import com.mohnage7.movies.view.adapter.MoviesAdapter;
import com.mohnage7.movies.view.callback.OnArticleClickListener;
import com.mohnage7.movies.view.callback.OnCategorySelectedListener;
import com.mohnage7.movies.viewmodel.MoviesViewModel;

import java.util.List;

import butterknife.BindView;

import static com.mohnage7.movies.utils.Constants.MOVIE_EXTRA;
import static com.mohnage7.movies.utils.Constants.POPULAR;
import static com.mohnage7.movies.view.ui.CategoryBottomSheet.SELECTED_CATEGORY;

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, OnArticleClickListener, OnCategorySelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_data_layout)
    RelativeLayout noDataLayout;
    @BindView(R.id.no_data_tv)
    TextView noDataTxtView;


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
        if (!swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(true);
        moviesViewModel.getMoviesList(filter).observe(this, dataWrapper -> {
            swipeRefreshLayout.setRefreshing(false);
            if (dataWrapper.getBaseError() != null) {
                handleError(dataWrapper.getBaseError());
            } else {
                setupArticlesRecycler(dataWrapper.getData());
            }
        });
    }

    private void setDataViewsVisibility(boolean dataAvailable) {
        if (dataAvailable) {
            recyclerView.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        }
    }


    private void handleError(BaseError baseError) {
        setDataViewsVisibility(false);
        if (baseError.getErrorCode() == 500) {
            noDataTxtView.setText(getString(R.string.server_error));
        } else {
            noDataTxtView.setText(baseError.getErrorMessage());
        }
    }

    private void setupArticlesRecycler(List<Movie> articles) {
        setDataViewsVisibility(true);
        MoviesAdapter adapter = new MoviesAdapter(articles, this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
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
