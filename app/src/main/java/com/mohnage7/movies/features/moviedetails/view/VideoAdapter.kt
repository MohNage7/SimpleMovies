package com.mohnage7.movies.features.moviedetails.view

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeStandalonePlayer
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView
import com.mohnage7.movies.R
import com.mohnage7.movies.base.BaseViewHolder
import com.mohnage7.movies.features.moviedetails.model.Video
import com.mohnage7.movies.utils.Constants

import butterknife.BindView
import butterknife.ButterKnife

import com.mohnage7.movies.BuildConfig.YOUTUBE_KEY

class VideoAdapter(private val videoList: List<Video>?) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return MoviesViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindViews(position)
    }

    override fun getItemCount(): Int {
        return if (videoList != null && !videoList.isEmpty()) {
            videoList.size
        } else {
            0
        }
    }


    protected inner class MoviesViewHolder internal constructor(itemView: View) : BaseViewHolder(itemView) {
        @BindView(R.id.youtube_thumbnail)
        internal var youTubeThumbnailView: YouTubeThumbnailView? = null
        @BindView(R.id.play_button)
        internal var playButton: ImageButton? = null
        @BindView(R.id.tailer_title_txt_view)
        internal var trailerTitleTxtView: TextView? = null

        init {
            ButterKnife.bind(this, itemView)
        }

        override fun bindViews(position: Int) {
            super.bindViews(position)
            val video = videoList!![position]
            // set video title
            trailerTitleTxtView!!.text = video.name
            // load video thumbnail
            val onThumbnailLoadedListener = object : YouTubeThumbnailLoader.OnThumbnailLoadedListener {
                override fun onThumbnailError(youTubeThumbnailView: YouTubeThumbnailView, errorReason: YouTubeThumbnailLoader.ErrorReason) {
                    playButton!!.visibility = View.GONE
                }

                override fun onThumbnailLoaded(youTubeThumbnailView: YouTubeThumbnailView, s: String) {
                    youTubeThumbnailView.visibility = View.VISIBLE
                    playButton!!.visibility = View.VISIBLE
                }
            }
            // init player
            youTubeThumbnailView!!.initialize(YOUTUBE_KEY, object : YouTubeThumbnailView.OnInitializedListener {
                override fun onInitializationSuccess(youTubeThumbnailView: YouTubeThumbnailView, youTubeThumbnailLoader: YouTubeThumbnailLoader) {
                    youTubeThumbnailLoader.setVideo(video.key)
                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener)
                }

                override fun onInitializationFailure(youTubeThumbnailView: YouTubeThumbnailView, youTubeInitializationResult: YouTubeInitializationResult) {
                    //write something for failure
                }
            })

            // play video in full screen activity when user clicks
            playButton!!.setOnClickListener { v ->
                val intent = YouTubeStandalonePlayer.createVideoIntent(itemView.context as Activity, YOUTUBE_KEY, video.key!!)
                itemView.context.startActivity(intent)
            }
        }


        override fun clear() {
            playButton!!.setOnClickListener(null)
            trailerTitleTxtView!!.text = ""
        }
    }
}
