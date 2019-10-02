package com.mohnage7.movies.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.mohnage7.movies.R;
import com.mohnage7.movies.base.BaseViewHolder;
import com.mohnage7.movies.model.Video;
import com.mohnage7.movies.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<Video> videoList;

    public VideoAdapter(List<Video> videoList) {
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.bindViews(position);
    }

    @Override
    public int getItemCount() {
        if (videoList != null && !videoList.isEmpty()) {
            return videoList.size();
        } else {
            return 0;
        }
    }


    protected class MoviesViewHolder extends BaseViewHolder {
        @BindView(R.id.youtube_thumbnail)
        YouTubeThumbnailView youTubeThumbnailView;
        @BindView(R.id.play_button)
        ImageButton playButton;
        @BindView(R.id.tailer_title_txt_view)
        TextView trailerTitleTxtView;

        MoviesViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindViews(int position) {
            super.bindViews(position);
            final Video video = videoList.get(position);
            // set video title
            trailerTitleTxtView.setText(video.getName());
            // load video thumbnail
            final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                @Override
                public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                    playButton.setVisibility(View.GONE);
                }

                @Override
                public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                    youTubeThumbnailView.setVisibility(View.VISIBLE);
                    playButton.setVisibility(View.VISIBLE);
                }
            };
            // init player
            youTubeThumbnailView.initialize(Constants.YOUTUBE_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                    youTubeThumbnailLoader.setVideo(video.getKey());
                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
                 }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                    //write something for failure
                }
            });

            // play video in full screen activity when user clicks
            playButton.setOnClickListener(v -> {
                Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) itemView.getContext(), Constants.YOUTUBE_KEY, video.getKey());
                itemView.getContext().startActivity(intent);
            });
        }


        @Override
        public void clear() {
            playButton.setOnClickListener(null);
            trailerTitleTxtView.setText("");
        }
    }
}
