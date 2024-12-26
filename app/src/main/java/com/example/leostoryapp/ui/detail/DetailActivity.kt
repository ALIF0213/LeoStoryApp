package com.example.leostoryapp.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.leostoryapp.databinding.ActivityDetailBinding
import com.example.leostoryapp.data.pref.UserPreference

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()
    private var storyId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyId = intent.getStringExtra("STORY_ID")
        Log.d("DetailActivity", "Received storyId: $storyId")


        if (storyId != null) {
            val userPreference = UserPreference(this)
            val token = userPreference.getToken()
            Log.d("DetailActivity", "Token: $token")

            if (token.isNullOrEmpty()) {
                finish()
            } else {
                token.let {
                    detailViewModel.fetchStoryDetail(storyId!!, it)
                }
            }

            observeDetailStory()
        }
    }

    private fun observeDetailStory() {
        detailViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        detailViewModel.storyDetail.observe(this) { story ->
            if (story != null) {
                Log.d("DetailActivity", "Story fetched: ${story.name}")
                binding.tvDetailName.text = story.name
                binding.tvDetailDescription.text = story.description

                Glide.with(this)
                    .load(story.photoUrl)
                    .into(binding.ivDetailPhoto)
            }
        }

        detailViewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                Log.e("DetailActivity", "Error: $it")
                binding.tvDetailDescription.text = it
            }
        }
    }

}
