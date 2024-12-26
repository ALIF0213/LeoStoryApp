package com.example.leostoryapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.leostoryapp.R
import com.example.leostoryapp.data.local.database.StoryDatabase
import com.example.leostoryapp.data.pref.UserPreference
import com.example.leostoryapp.data.remote.retrofit.ApiConfig
import com.example.leostoryapp.data.repository.StoryRepository
import com.example.leostoryapp.databinding.ActivityMainBinding
import com.example.leostoryapp.ui.addstory.AddStoryActivity
import com.example.leostoryapp.ui.detail.DetailActivity
import com.example.leostoryapp.ui.login.LoginActivity
import com.example.leostoryapp.ui.maps.MapsActivity

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var loadingStateAdapter: LoadingStateAdapter
    private lateinit var token: String
    private lateinit var storyViewModel: StoryViewModel

    private val storyRepository: StoryRepository by lazy {
        val database = StoryDatabase.getDatabase(this)
        val apiService = ApiConfig.instance
        StoryRepository(database, apiService, UserPreference(this))
    }

    private val addStoryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            Log.d("MainActivity", "Refreshing story list after adding a story.")
            storyAdapter.refresh()
        } else {
            Log.d("MainActivity", "Add story activity result not OK.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Story"

        val userPreference = UserPreference(this)
        token = userPreference.getToken() ?: ""

        if (token.isEmpty()) {
            redirectToLogin()
            return
        }

        setupRecyclerView()
        setupViewModel()
        setupObservers()

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            addStoryLauncher.launch(intent)
        }
    }

    private fun setupViewModel() {
        storyViewModel = StoryViewModelFactory(storyRepository).create(StoryViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                Log.d("MainActivity", "Logout menu selected.")
                logout()
                true
            }

            R.id.action_maps -> {
                Log.d("MainActivity", "Maps menu selected.")
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        Log.d("MainActivity", "Performing logout.")
        val userPreference = UserPreference(this)
        userPreference.clearSession()

        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    private fun redirectToLogin() {
        Log.d("MainActivity", "Redirecting to login.")
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    private fun setupRecyclerView() {
        storyAdapter = StoryAdapter { story ->
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("STORY_ID", story.id)
            }
            startActivity(intent)
        }

        loadingStateAdapter = LoadingStateAdapter { storyAdapter.retry() }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storyAdapter.withLoadStateHeaderAndFooter(
                header = loadingStateAdapter,
                footer = loadingStateAdapter
            )
        }
    }

    private fun setupObservers() {
        storyViewModel.getStories.observe(this@MainActivity) { pagingData ->
            storyAdapter.submitData(lifecycle, pagingData)
        }

        storyAdapter.addLoadStateListener { loadState ->
            binding.progressBar.visibility = if (loadState.source.refresh is LoadState.Loading) View.VISIBLE else View.GONE

            if (loadState.source.refresh is LoadState.NotLoading && storyAdapter.itemCount < 1) {
                binding.recyclerView.visibility = View.GONE
                binding.emptyView.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.emptyView.visibility = View.GONE
            }
        }
    }



    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        super.onBackPressed()
        Log.d("MainActivity", "Back button pressed.")
        moveTaskToBack(true)
    }
}