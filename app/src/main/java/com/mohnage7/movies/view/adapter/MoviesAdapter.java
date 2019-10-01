package com.mohnage7.movies.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mohnage7.movies.R;
import com.mohnage7.movies.base.BaseViewHolder;
import com.mohnage7.movies.model.Movie;
import com.mohnage7.movies.view.callback.OnArticleClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<Movie> movieList;
    private OnArticleClickListener onArticleClickListener;

    public MoviesAdapter(List<Movie> moviesList, OnArticleClickListener onArticleClickListener) {
        this.movieList = moviesList;
        this.onArticleClickListener = onArticleClickListener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        view.getLayoutParams().height = (int) (parent.getWidth() / 2 *
                1.5);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.bindViews(position);
    }

    @Override
    public int getItemCount() {
        if (movieList != null && !movieList.isEmpty()) {
            return movieList.size();
        } else {
            return 0;
        }
    }


    protected class MoviesViewHolder extends BaseViewHolder {
        @BindView(R.id.movie_img_view)
        ImageView movieImageView;
        @BindView(R.id.movie_title_txt_view)
        TextView movieTitle;


        MoviesViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindViews(int position) {
            super.bindViews(position);
            Movie movie = movieList.get(position);
            movieTitle.setText(movie.getTitle());
            displayArticleImage(movie.getPosterPath(itemView.getContext()));
            itemView.setOnClickListener(v -> onArticleClickListener.onMovieClick(movie, itemView));
        }

        private void displayArticleImage(String imageUrl) {
            if (imageUrl != null) {
                Picasso.get().load(imageUrl).placeholder(R.drawable.placeholder).into(movieImageView);
            } else {
                Picasso.get().load(R.drawable.placeholder).into(movieImageView);
            }
        }


        @Override
        public void clear() {
            movieTitle.setText("");
            movieImageView.setImageDrawable(null);
        }
    }
}
