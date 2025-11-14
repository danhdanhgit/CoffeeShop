package com.example.coffeeshop.Adapter


import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coffeeshop.Activity.DetailActivity
import com.example.coffeeshop.Data.Entity.ProductResult
import com.example.coffeeshop.R

class MediaAdapter (
    private val context: DetailActivity,
    private val mediaList: ProductResult?
): RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {

    private val allMediaUrls: List<String> = mutableListOf<String>().apply {
        mediaList?.let {
            addAll(it.videos)
            addAll(it.images)
        }
    }

    inner class MediaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val videoView: VideoView = view.findViewById(R.id.videoView)
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val btnPlay: ImageView = view.findViewById(R.id.btnPlay)
    }

    override fun getItemCount(): Int {
        return allMediaUrls.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MediaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_media, parent, false)
        return MediaViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MediaViewHolder,
        position: Int
    ) {
        val mediaUrl = allMediaUrls[position]

        if (mediaUrl.substringBefore("?").endsWith(".mp4", ignoreCase = true)) {
            // Nếu là video
            holder.videoView.visibility = View.VISIBLE
            holder.btnPlay.visibility = View.VISIBLE
            holder.imageView.visibility = View.GONE

            holder.videoView.setVideoURI(Uri.parse(mediaUrl))
            holder.videoView.stopPlayback() // Dừng video nếu tái sử dụng view

            //Hiển thị frame đầu tiên
            holder.videoView.seekTo(1)

            holder.btnPlay.setOnClickListener {
                holder.btnPlay.visibility = View.GONE
                holder.videoView.start()
            }

            holder.videoView.setOnCompletionListener {
                holder.btnPlay.visibility = View.VISIBLE
                // Tua video về đầu để xem lại
                holder.videoView.seekTo(1)
            }
        } else {
            //Nếu là ảnh
            holder.videoView.visibility = View.GONE
            holder.btnPlay.visibility = View.GONE
            holder.imageView.visibility = View.VISIBLE

            //Dừng video nếu View này trước đó là video

            Glide.with(context)
                .load(mediaUrl)
                .into(holder.imageView)
        }


        fun pauseAllVideos() {
            //Khi cuộn để dừng video
            notifyDataSetChanged()
        }
    }
}