package com.example.leostoryapp.ui.main

import android.animation.ObjectAnimator
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.leostoryapp.R
import com.example.leostoryapp.data.remote.response.ListStoryItem
import com.example.leostoryapp.ui.detail.DetailActivity

class StoryAdapter(private val onItemClick: (ListStoryItem) -> Unit) :
    PagingDataAdapter<ListStoryItem, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)

            holder.itemView.alpha = 0f
            ObjectAnimator.ofFloat(holder.itemView, "alpha", 0f, 1f).apply {
                duration = 500
                startDelay = (position * 100).toLong()
                start()
            }
        }
    }

    inner class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.iv_item_photo)
        private val textView: TextView = itemView.findViewById(R.id.tv_item_name)

        fun bind(story: ListStoryItem) {
            textView.text = story.name
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .apply(RequestOptions())
                .into(imageView)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java).apply {
                    putExtra("STORY_ID", story.id)
                }
                itemView.context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}