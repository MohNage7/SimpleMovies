package com.mohnage7.movies.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.mohnage7.movies.R;
import com.mohnage7.movies.base.BaseActivity;
import com.mohnage7.movies.model.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mohnage7.movies.utils.Constants.MOVIE_EXTRA;

public class MovieDetailsActivity extends BaseActivity {


    @BindView(R.id.movie_title_txt_view)
    TextView titleTxtView;
    @BindView(R.id.movie_description)
    TextView descTxtView;
    @BindView(R.id.movie_poster_img_view)
    ImageView posterImageView;
    @BindView(R.id.movie_rating)
    TextView movieRatingTxtView;
    @BindView(R.id.movie_date_txt_view)
    TextView movieReleaseDatetxtView;

    private Movie movie;

    @Override
    protected int layoutRes() {
        return R.layout.movie_details_old;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // inject views
        ButterKnife.bind(this);
        // get movie object
        getMovieFromIntent();
        // fill views with data
        setViews();
    }

    private void setViews() {
        titleTxtView.setText(movie.getTitle());
        descTxtView.setText(movie.getOverview());
        movieRatingTxtView.setText(String.valueOf(movie.getVoteAverage()));
        movieReleaseDatetxtView.setText(getString(R.string.release_date, movie.getReleaseDate()));
        Picasso.get().load(movie.getPosterPath(this)).into(posterImageView);
    }


    /**
     * this method gets movie object from coming intent
     */
    private void getMovieFromIntent() {
        Intent intent = getIntent();
        if (intent != null)
            movie = intent.getParcelableExtra(MOVIE_EXTRA);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // exits activity with image transaction animation
        supportFinishAfterTransition();
    }


//    @OnClick(R.id.trailers_txt_view)
//    public void startTrailersActivity() {
//        Intent intent = new Intent(this, TrailersActivity.class);
//        intent.putExtra(MOVIE_ID, movie.getId());
//        startActivity(intent);
//    }


}
