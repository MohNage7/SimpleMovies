package com.mohnage7.movies.features.moviedetails.view

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.youtube.player.YouTubeStandalonePlayer
import com.mohnage7.movies.BuildConfig.YOUTUBE_KEY
import com.mohnage7.movies.R
import com.mohnage7.movies.base.BaseViewHolder
import com.mohnage7.movies.features.moviedetails.model.Video
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_video.view.*


const val YOUTUBE_THUMBNAIL_BASE_URL = "https://img.youtube.com/vi/"
const val YOUTUBE_THUMBNAIL_URL_JPG = "/0.jpg"

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


    private inner class MoviesViewHolder internal constructor(itemView: View) : BaseViewHolder(itemView) {

        override fun bindViews(position: Int) {
            super.bindViews(position)
            val video = videoList!![position]
            // set video title
            itemView.trailerTitleTxtView.text = video.name

            Picasso.get()
                    .load(video.getVideoThumbnail())
                    .into(itemView.youTubeThumbnailView, object : Callback {
                        override fun onError(e: Exception?) {
                            itemView.playButton.visibility = View.GONE
                        }

                        override fun onSuccess() {
                            itemView.playButton.visibility = View.VISIBLE
                        }
                    })

            // play video in full screen activity when user clicks
            itemView.playButton!!.setOnClickListener {
                val intent = YouTubeStandalonePlayer.createVideoIntent(itemView.context as Activity, YOUTUBE_KEY, video.key!!)
                itemView.context.startActivity(intent)
            }
        }


        override fun clear() {
            itemView.playButton.setOnClickListener(null)
            itemView.trailerTitleTxtView.text = ""
            itemView.youTubeThumbnailView.setImageDrawable(null)
        }
    }
}
