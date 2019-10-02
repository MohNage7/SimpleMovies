package com.mohnage7.movies.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mohnage7.movies.R;
import com.mohnage7.movies.base.BaseViewHolder;
import com.mohnage7.movies.model.Movie;
import com.mohnage7.movies.view.callback.OnMovieClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<Movie> movieList;
    private OnMovieClickListener onMovieClickListener;

    public SearchAdapter(List<Movie> moviesList, OnMovieClickListener onMovieClickListener) {
        this.movieList = moviesList;
        this.onMovieClickListener = onMovieClickListener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
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
        @BindView(R.id.title_tv)
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
            itemView.setOnClickListener(v -> onMovieClickListener.onMovieClick(movie, itemView));
        }


        @Override
        public void clear() {
            movieTitle.setText("");
        }
    }
}
