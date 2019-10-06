package com.mohnage7.movies.view.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.mohnage7.movies.R;
import com.mohnage7.movies.base.BaseActivity;
import com.mohnage7.movies.model.Movie;
import com.mohnage7.movies.model.Video;
import com.mohnage7.movies.utils.Constants;
import com.mohnage7.movies.view.adapter.VideoAdapter;
import com.mohnage7.movies.viewmodel.MovieDetailViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mohnage7.movies.utils.Constants.MOVIE_EXTRA;

public class MovieDetailsActivity extends BaseActivity {


    @BindView(R.id.movie_title_txt_view)
    TextView titleTxtView;
    @BindView(R.id.movie_description)
    TextView descTxtView;
    @BindView(R.id.videos_tv)
    TextView videosTxtView;
    @BindView(R.id.movie_rating)
    TextView movieRatingTxtView;
    @BindView(R.id.movie_date_txt_view)
    TextView movieReleaseDateTxtView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.back_drop_iv)
    ImageView backdropImgView;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.shimmer_loading_layout)
    ShimmerFrameLayout shimmerFrameLayout;

    private Movie movie;
    private MovieDetailViewModel viewModel;

    @Override
    protected int layoutRes() {
        return R.layout.activity_movie_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inject views
        ButterKnife.bind(this);
        // init view model
        viewModel = ViewModelProviders.of(this).get(MovieDetailViewModel.class);

        setupToolbar();
        // get movie object
        getMovieFromIntent();
        // fill views with data
        setViews();
    }

    private void setupToolbar() {
        toolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // set status bar color as transparent to be able to see poster below it.
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                    collapsingToolbarLayout.setTitle(movie.getTitle());
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle("");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    private void setViews() {
        titleTxtView.setText(movie.getTitle());
        descTxtView.setText(movie.getOverview());
        movieRatingTxtView.setText(String.valueOf(movie.getVoteAverage()));
        movieReleaseDateTxtView.setText(getString(R.string.release_date, movie.getReleaseDate()));
        Picasso.get().load(Constants.IMAGE_BASE_URL + movie.getBackdropPath()).into(backdropImgView);
    }

    private void showLoadingLayout() {
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }
    private void hideLoadingLayout() {
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }


    /**
     * this method gets movie object from coming intent
     */
    private void getMovieFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            movie = intent.getParcelableExtra(MOVIE_EXTRA);
            if (movie != null) {
                viewModel.getVideosList(movie.getId()).observe(this, dataWrapper -> {
                    switch (dataWrapper.status) {
                        case LOADING:
                            showLoadingLayout();
                            break;
                        case SUCCESS:
                            hideLoadingLayout();
                            setupRecycler(dataWrapper.data.getVideos());
                            break;
                        case ERROR:
                            hideLoadingLayout();
                            handleError(dataWrapper.message);
                            break;
                        default:
                            break;
                    }
                });
            } else {
                hideLoadingLayout();
                showToast(R.string.movie_load_failure);
                finish();
            }
        }
    }

    private void setupRecycler(List<Video> videoList) {
        VideoAdapter videoAdapter = new VideoAdapter(videoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(videoAdapter);
    }

    private void handleError(String message) {
        videosTxtView.setVisibility(View.GONE);
        showToast(message);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // exits activity with image transaction animation
        overridePendingTransition(R.anim.no_animation, R.anim.anim_slide_down);
    }
}
